package yadav.dhananjay.presentation.ui.searchjob

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import yadav.dhananjay.data.local.sharedpref.IPreferenceStorage
import yadav.dhananjay.domain.interactor.jobsearch.GetFilteredJobs
import yadav.dhananjay.domain.interactor.jobsearch.SearchForJob
import yadav.dhananjay.domain.model.GitJob
import yadav.dhananjay.presentation.state.StatedResource
import javax.inject.Inject

class SearchForJobViewModel @Inject constructor(
        private val searchForJob: SearchForJob,
        private val searchFilteredJob: GetFilteredJobs,
        private val preferenceStorage: IPreferenceStorage
) : ViewModel() {

    private val searchJobLiveData: MutableLiveData<StatedResource> = MutableLiveData()
    private val jobs = mutableListOf<GitJob>()


    override fun onCleared() {
        super.onCleared()

        searchForJob.dispose()
    }

    fun getSearchJobLiveData(): LiveData<StatedResource> {
        return searchJobLiveData
    }

    fun fetchJobs(query: String, lat: Double, long: Double, page: Int) {
        val spoofedLat = 37.3229978
        val spoofedLong = -122.0321823

        searchJobLiveData.postValue(StatedResource.JustLoadig())

        searchForJob.execute(SearchForJobObserver(),
                SearchForJob.Params(query, spoofedLat, spoofedLong, page))
    }

    fun filterJobs(selectedFilter: String) {
        searchFilteredJob.execute(SearchForJobObserver(), GetFilteredJobs.Params(jobs, selectedFilter))
    }

    fun removeFilter() {
        searchJobLiveData.postValue(StatedResource.Success(jobs))
    }

    inner class SearchForJobObserver : DisposableObserver<List<GitJob>>() {
        override fun onComplete() {
            Timber.d("onComplete: SearchForJobObserver")
        }

        override fun onNext(t: List<GitJob>) {
            Timber.d("onNext: SearchForJobObserver: %s", t.toString())

            jobs.clear()
            jobs.addAll(t)

            searchJobLiveData.postValue(StatedResource.Success(t))
        }

        override fun onError(e: Throwable) {
            Timber.d(e)

            searchJobLiveData.postValue(StatedResource.Error(e.localizedMessage))
        }
    }
}