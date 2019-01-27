package yadav.dhananjay.domain.interactor.splash

import yadav.dhananjay.domain.interactor.base.CompletableUseCase
import io.reactivex.Completable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class SplashDelayUseCase @Inject constructor()
    : CompletableUseCase<Void>()
{
    override fun buildUseCaseCompletable(params: Void?): Completable {
     return Completable.timer(2, TimeUnit.SECONDS)
    }

}