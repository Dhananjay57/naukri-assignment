package yadav.dhananjay.data.remote.retrofit.service

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import yadav.dhananjay.domain.model.GitJob
import yadav.dhananjay.domain.model.PlacesResponse

interface GithubService {

    @GET("positions.json")
    fun getJobs(@Query("description") description: String,
                @Query("lat") lat: Double,
                @Query("long") long: Double,
                @Query("page") page: Int)
            : Observable<List<GitJob>>

    @GET("https://www.meteoblue.com/en/server/search/query3")
    fun getLocation(@Query("query") query: String): Observable<PlacesResponse>
}