package yadav.dhananjay.domain.interactor.jobsearch

import yadav.dhananjay.domain.exception.NullParamsException
import yadav.dhananjay.domain.interactor.base.ObservableUseCase
import yadav.dhananjay.domain.model.GitJob
import yadav.dhananjay.domain.repository.factory.IGithubRepositoryFactory
import io.reactivex.Observable
import javax.inject.Inject


open class SearchForJob @Inject constructor(
        private val githubRepositoryFactory: IGithubRepositoryFactory
) : ObservableUseCase<List<GitJob>, SearchForJob.Params>() {

    public override fun buildUseCaseObservable(params: Params?): Observable<List<GitJob>> {
        if (params == null) throw NullParamsException()

        return githubRepositoryFactory
                .getRepository()
                .getJobs(params.query, params.lat, params.long, params.page)
                .flatMap { jobs ->
                    jobs.forEach { it.type = GitJob.getRandomJobType() }
                    Observable.just(jobs)
                }
    }

    data class Params constructor(val query: String,
                                  val lat: Double, val long: Double,
                                  val page: Int)
}