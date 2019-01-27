package yadav.dhananjay.data.factory

import yadav.dhananjay.data.remote.repository.GithubRemoteRepository
import yadav.dhananjay.domain.repository.IGithubRepository
import yadav.dhananjay.domain.repository.factory.IGithubRepositoryFactory
import javax.inject.Inject

class GithubRepositoryFactory @Inject constructor(
        private val githubRemoteRepository: GithubRemoteRepository
        ) : IGithubRepositoryFactory {

    override fun getRepository(): IGithubRepository {
        return githubRemoteRepository
    }
}