package yadav.dhananjay.presentation.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yadav.dhananjay.domain.interactor.splash.SplashDelayUseCase
import io.reactivex.observers.DisposableCompletableObserver
import timber.log.Timber
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        private val splashDelayedUseCase: SplashDelayUseCase
) : ViewModel() {

    private val splashCompletedLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        splashDelayedUseCase.execute(SplashDelayedObserver())
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun getSplashCompletedLiveData(): MutableLiveData<Boolean> {
        return splashCompletedLiveData
    }

    inner class SplashDelayedObserver : DisposableCompletableObserver() {
        override fun onComplete() {
            splashCompletedLiveData.postValue(true)
        }

        override fun onError(e: Throwable) {
            Timber.d("onError: $e")
        }
    }

}