package com.kshitijchauhan.haroldadmin.moviedb.remote.service.search

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("search/movie")
    fun searchForMovie(@Query("query") query: String,
                       @Query("page") page: Int = 1): Single<SearchResponse>

}