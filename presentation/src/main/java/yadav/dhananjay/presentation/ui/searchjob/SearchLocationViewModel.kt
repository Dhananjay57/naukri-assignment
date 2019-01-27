package yadav.dhananjay.presentation.ui.searchjob

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yadav.dhananjay.domain.interactor.jobsearch.SearchForLocation
import yadav.dhananjay.domain.model.Places
import yadav.dhananjay.presentation.state.StatedResource
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import javax.inject.Inject

class SearchLocationViewModel @Inject constructor(
        private val searchForLocation: SearchForLocation
) : ViewModel() {

    private val searchLocationLiveData: MutableLiveData<StatedResource> = MutableLiveData()

    init {
//        fetchLocation("New Delhi")
    }

    override fun onCleared() {
        super.onCleared()

        searchForLocation.dispose()
    }

    fun getSearchLocationLiveData(): LiveData<StatedResource> {
        return searchLocationLiveData
    }

    fun fetchLocation(query: String) {
        searchLocationLiveData.postValue(StatedResource.JustLoadig())

        searchForLocation.execute(SearchForLocationObserver(), SearchForLocation.Params(query))
    }

    inner class SearchForLocationObserver : DisposableObserver<List<Places>>() {
        override fun onComplete() {
            Timber.d("onComplete: SearchForJobObserver")
        }

        override fun onNext(t: List<Places>) {
            Timber.d("onNext: SearchForJobObserver: %s", t.toString())

            searchLocationLiveData.postValue(StatedResource.Success(t))
        }

        override fun onError(e: Throwable) {
            Timber.d(e)

            searchLocationLiveData.postValue(StatedResource.Error(e.localizedMessage))
        }
    }
}