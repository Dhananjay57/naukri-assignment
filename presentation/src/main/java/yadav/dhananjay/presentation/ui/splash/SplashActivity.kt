package yadav.dhananjay.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import yadav.dhananjay.data.local.sharedpref.IPreferenceStorage
import yadav.dhananjay.presentation.injection.ViewModelFactory
import yadav.dhananjay.presentation.ui.searchjob.SearchJobActivity
import yadav.dhananjay.presentation.ui.searchjob.SearchLocationActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var pref: IPreferenceStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        splashViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SplashViewModel::class.java)

        splashViewModel.getSplashCompletedLiveData().observe(this,
                Observer {
                    if (pref.savedLocation != null) {
                        startActivity(Intent(this, SearchJobActivity::class.java))
                    } else {
                        startActivity(Intent(this, SearchLocationActivity::class.java))
                    }
                    finish()
                })
    }
}