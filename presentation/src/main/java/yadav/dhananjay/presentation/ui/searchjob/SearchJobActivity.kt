package yadav.dhananjay.presentation.ui.searchjob

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import dagger.android.AndroidInjection
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_search_job.*
import timber.log.Timber
import yadav.dhananjay.data.local.sharedpref.IPreferenceStorage
import yadav.dhananjay.domain.model.GitJob
import yadav.dhananjay.presentation.injection.ViewModelFactory
import yadav.dhananjay.presentation.state.StatedResource
import javax.inject.Inject


class SearchJobActivity : AppCompatActivity() {
    private val ADD_TASK_REQUEST = 1
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var searchForJobViewModel: SearchForJobViewModel

    @Inject
    lateinit var pref: IPreferenceStorage

    private val gitJobAdapter: GitJobAdapter by lazy {
        GitJobAdapter()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(yadav.dhananjay.presentation.R.layout.activity_search_job)
        AndroidInjection.inject(this)

        searchForJobViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SearchForJobViewModel::class.java)

        searchForJobViewModel.getSearchJobLiveData().observe(this,
                Observer<StatedResource> {
                    it?.let { handleDataState(it) }
                })
        tvPlace.text = pref.savedLocation?.name + " , " + pref.savedLocation?.country
        tvAddressChange.setOnClickListener {
            val intent = Intent(this, SearchLocationActivity::class.java)
            intent.putExtra("FromJob", true)
            startActivityForResult(intent, ADD_TASK_REQUEST)
        }

        tvRemoveFilter.setOnClickListener {
            tvRemoveFilter.visibility = View.GONE
            chipGroup.removeAllViews()
            this.searchForJobViewModel.fetchJobs(etSearchJob.query.toString().trim(), pref.savedLocation!!.lat, pref.savedLocation!!.long, 1)
        }

        setupJobRecyclerView()
        setupSearchEt()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    private fun setupSearchEt() {
        etSearchJob.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                this@SearchJobActivity.searchForJobViewModel.fetchJobs(etSearchJob.query.toString().trim(), pref.savedLocation!!.lat, pref.savedLocation!!.long, 1)
                chipGroup.removeAllViews()
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

    }

    private fun setupJobRecyclerView() {
        rvJobs.layoutManager = LinearLayoutManager(this)
        rvJobs.adapter = gitJobAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_TASK_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                tvPlace.text = pref.savedLocation?.name
                if (etSearchJob.query.isNotEmpty()) {
                    this.searchForJobViewModel.fetchJobs(etSearchJob.query.toString().trim(), pref.savedLocation!!.lat, pref.savedLocation!!.long, 1)
                    chipGroup.removeAllViews()
                }
            }
        }
    }


    private fun handleDataState(statedResource: StatedResource) {
        when (statedResource) {
            is StatedResource.Success<*> -> {
                Timber.d("Success")

                jobProgressLoadingState.visibility = View.GONE
                rvJobs.visibility = View.VISIBLE

                val jobs = statedResource.data as List<GitJob>
                gitJobAdapter.submitList(jobs)

                val typeMap = jobs.groupBy { it.type }

                chipGroup.removeAllViews()
                typeMap.forEach {
                    val chip = Chip(chipGroup.context)
                    val filterType = it.key
                    chip.text = it.key + " (" + it.value.size + ")";

                    val themeColorStateList = ColorStateList(
                            arrayOf(intArrayOf()
                            ),
                            intArrayOf(Color.parseColor("#EFEFEF"))
                    )
                    chip.chipBackgroundColor = themeColorStateList
                    chip.isClickable = false

                    chip.setOnClickListener {
                        tvRemoveFilter.visibility = View.VISIBLE
                        searchForJobViewModel.filterJobs(filterType)
                    }

                    chipGroup.addView(chip)
                }

                if (jobs.size > 0)
                    tvNumberOfJobs.text = jobs.size.toString() + " Jobs Around You"
                else
                    tvNumberOfJobs.text = "No jobs found"

            }
            is StatedResource.JustLoadig -> {
                Timber.d("Loading")
                jobProgressLoadingState.visibility = View.VISIBLE
                rvJobs.visibility = View.GONE
                tvNumberOfJobs.text = "Loading.."
            }
            is StatedResource.Error -> {
                Timber.d("Error: %s", statedResource.message)

                jobProgressLoadingState.visibility = View.GONE
                rvJobs.visibility = View.GONE
                tvNumberOfJobs.text = "No results found"
            }
        }
    }
}
