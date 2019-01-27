package yadav.dhananjay.domain.repository

import io.reactivex.Observable
import yadav.dhananjay.domain.model.GitJob
import yadav.dhananjay.domain.model.Places

interface IGithubRepository {

    fun getJobs(description: String,
                lat: Double, long: Double,
                page: Int): Observable<List<GitJob>>

    fun getLocation(description: String): Observable<List<Places>> }