package yadav.dhananjay.domain.repository.factory

import yadav.dhananjay.domain.repository.IGithubRepository


interface  IGithubRepositoryFactory{

    fun getRepository(): IGithubRepository
}