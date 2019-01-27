package yadav.dhananjay.presentation.ui.searchjob

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_search_location.*
import timber.log.Timber
import yadav.dhananjay.data.local.sharedpref.IPreferenceStorage
import yadav.dhananjay.domain.model.Places
import yadav.dhananjay.presentation.BuildConfig
import yadav.dhananjay.presentation.R
import yadav.dhananjay.presentation.injection.ViewModelFactory
import yadav.dhananjay.presentation.state.StatedResource
import yadav.dhananjay.presentation.utils.openSoftKeyboard
import java.util.*
import javax.inject.Inject


class SearchLocationActivity : AppCompatActivity(), LocationSearchAdapter.AdapterCallback {

    private lateinit var selectedLocation: Places
    private val searchedLocation = mutableListOf<Places>()

    override fun onOtherClick() {
        etSearchLocation.openSoftKeyboard(this@SearchLocationActivity)
    }

    override fun onLocationClick(place: Places) {
        pref.savedLocation = place
        selectedLocation = place

        if (this.fromJobActivity!!) {
            setResult(Activity.RESULT_OK)
        } else {
            startActivity(Intent(this, SearchJobActivity::class.java))
        }
        finish()
    }

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    protected var mLastLocation: Location? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var pref: IPreferenceStorage

    private lateinit var searchForLocationViewModel: SearchLocationViewModel

    private var fromJobActivity: Boolean? = null

    private val locationAdapter: LocationSearchAdapter by lazy {
        LocationSearchAdapter(this)
    }
    private var city: String = ""
    private var country: String = ""

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(yadav.dhananjay.presentation.R.layout.activity_search_location)
        AndroidInjection.inject(this)

        searchForLocationViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SearchLocationViewModel::class.java)

        searchForLocationViewModel.getSearchLocationLiveData().observe(this,
                Observer<StatedResource> {
                    it?.let { handleDataState(it) }
                })

        fromJobActivity = intent.getBooleanExtra("FromJob", false)
        setupPlacesRecyclerView()
        setupSearchEt()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        tvSearchMyLocation.setOnClickListener {
            if (!checkPermissions()) {
                requestPermissions()
            } else {
                getLastLocation()
            }
        }
    }

    private fun setupSearchEt() {
        etSearchLocation.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3

                if (event?.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (etSearchLocation.right - etSearchLocation.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {

                        if (::selectedLocation.isInitialized) {
                            onLocationClick(selectedLocation)
                        }
                        return true
                    }
                }
                return false
            }
        })


        etSearchLocation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                etSearchLocation.threshold = 3
                this@SearchLocationActivity.searchForLocationViewModel.fetchLocation(etSearchLocation.text.toString().trim())

            }

        })
    }

    private fun setupPlacesRecyclerView() {
        rvLocation.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rvLocation.adapter = locationAdapter

        val defaultLocations = mutableListOf<Places>()
        defaultLocations.add(Places("New Delhi", "India", 128.6139, 77.2090, false))
        defaultLocations.add(Places("Mumbai", "India", 19.0760, 72.8777, false))
        defaultLocations.add(Places("Kolkata", "India", 22.5726, 88.3639, false))
        defaultLocations.add(Places("Bangalore", "India", 12.9716, 77.5946, false))
        defaultLocations.add(Places("Other City", "India", 11.11, 11.11, true))

        locationAdapter.submitList(defaultLocations)
    }

    private fun handleDataState(statedResource: StatedResource) {
        when (statedResource) {
            is StatedResource.Success<*> -> {
                Timber.d("Success")
//                jobProgressLoadingState.visibility = View.GONE
//                locationAdapter.submitList(statedResource.data as List<Places>)
//                jobTextErrorState.visibility = View.GONE
//
//                gitJobAdapter.submitList(statedResource.data as List<GitJob>)

                searchedLocation.addAll(statedResource.data as List<Places>)

                etSearchLocation.setAdapter(ArrayAdapter(
                        this@SearchLocationActivity,
                        R.layout.support_simple_spinner_dropdown_item,
                        searchedLocation.map { it.name }
                ))

                etSearchLocation.setOnItemClickListener { p0, p1, p2, p3 ->
                    val loc = searchedLocation.findLast { it.name.equals(etSearchLocation.text.toString()) }
                    selectedLocation = Places(loc?.name!!, loc?.country!!, loc?.lat, loc?.long, false)
                }

            }
            is StatedResource.JustLoadig -> {
                Timber.d("Loading")

//                jobProgressLoadingState.visibility = View.VISIBLE
//                jobTextErrorState.visibility = View.GONE
            }
            is StatedResource.Error -> {
                Timber.d("Error: %s", statedResource.message)
//
//                jobProgressLoadingState.visibility = View.GONE
//                jobTextErrorState.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mFusedLocationClient!!.lastLocation
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLastLocation = task.result
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses = geocoder.getFromLocation((mLastLocation)!!.latitude, (mLastLocation)!!.longitude, 1)
                        city = addresses[0].locality
                        val state = addresses[0].adminArea
                        val zip = addresses[0].postalCode
                        country = addresses[0].countryName
                        etSearchLocation.setText(city + " ," + country)

                        selectedLocation = Places(city, country, mLastLocation!!.latitude, mLastLocation!!.longitude, false)
                    } else {
                        Log.w(TAG, "getLastLocation:exception", task.exception)
                        showMessage(getString(yadav.dhananjay.presentation.R.string.no_location_detected))
                    }
                }
    }

    private fun showMessage(text: String) {
        val container = findViewById<View>(yadav.dhananjay.presentation.R.id.main_activity_container)
        if (container != null) {
            Toast.makeText(this@SearchLocationActivity, text, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Shows a [].
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * *
     * @param actionStringId   The text of the action item.
     * *
     * @param listener         The listener associated with the Snackbar action.
     */
    private fun showSnackbar(mainTextStringId: Int, actionStringId: Int,
                             listener: View.OnClickListener) {

        Toast.makeText(this@SearchLocationActivity, getString(mainTextStringId), Toast.LENGTH_LONG).show()
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this@SearchLocationActivity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")

            showSnackbar(yadav.dhananjay.presentation.R.string.permission_rationale, android.R.string.ok,
                    View.OnClickListener {
                        // Request permission
                        startLocationPermissionRequest()
                    })

        } else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation()
            } else {
                showSnackbar(yadav.dhananjay.presentation.R.string.permission_denied_explanation, yadav.dhananjay.presentation.R.string.settings,
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null)
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        })
            }
        }
    }

    companion object {
        private val TAG = "LocationProvider"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }
}