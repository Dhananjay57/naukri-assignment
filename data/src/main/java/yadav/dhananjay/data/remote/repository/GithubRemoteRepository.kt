package yadav.dhananjay.data.remote.repository

import io.reactivex.Observable
import yadav.dhananjay.data.remote.retrofit.service.GithubService
import yadav.dhananjay.domain.model.GitJob
import yadav.dhananjay.domain.model.Places
import yadav.dhananjay.domain.repository.IGithubRepository
import javax.inject.Inject

class GithubRemoteRepository @Inject constructor(
        private val githubService: GithubService
) : IGithubRepository {

    override fun getJobs(description: String, lat: Double, long: Double, page: Int): Observable<List<GitJob>> {
        return githubService.getJobs(description, lat, long, page)
    }

    override fun getLocation(description: String): Observable<List<Places>> {
        return githubService.getLocation(description).map { it.results }
    }
}