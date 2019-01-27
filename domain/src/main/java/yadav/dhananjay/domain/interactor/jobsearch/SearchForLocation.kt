package yadav.dhananjay.domain.interactor.jobsearch

import yadav.dhananjay.domain.exception.NullParamsException
import yadav.dhananjay.domain.interactor.base.ObservableUseCase
import yadav.dhananjay.domain.model.Places
import yadav.dhananjay.domain.repository.factory.IGithubRepositoryFactory
import io.reactivex.Observable
import javax.inject.Inject

open class SearchForLocation @Inject constructor(
        private val githubRepositoryFactory: IGithubRepositoryFactory
) : ObservableUseCase<List<Places>, SearchForLocation.Params>() {

    public override fun buildUseCaseObservable(params: Params?): Observable<List<Places>> {
        if (params == null) throw NullParamsException()

        return githubRepositoryFactory.getRepository().getLocation(params.query)

    }

    data class Params constructor(val query: String)
}
