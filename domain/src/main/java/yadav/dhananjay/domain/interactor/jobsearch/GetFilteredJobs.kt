package yadav.dhananjay.domain.interactor.jobsearch

import yadav.dhananjay.domain.exception.NullParamsException
import yadav.dhananjay.domain.interactor.base.ObservableUseCase
import yadav.dhananjay.domain.model.GitJob
import io.reactivex.Observable
import javax.inject.Inject


open class GetFilteredJobs @Inject constructor()
    : ObservableUseCase<List<GitJob>, GetFilteredJobs.Params>() {

    public override fun buildUseCaseObservable(params: Params?): Observable<List<GitJob>> {
        if (params == null) throw NullParamsException()

        return Observable.fromArray(params.unfilteredJobs)
                .flatMap { unfilteredJobs ->
                    val filteredJobs = unfilteredJobs.filter { it.type.equals(params.selectedType) }
                    Observable.just(filteredJobs)
                }
    }

    data class Params constructor(val unfilteredJobs: List<GitJob>,
                                  val selectedType: String)
}