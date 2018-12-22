package com.kshitijchauhan.haroldadmin.moviedb.remote.service.search

import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("${Config.API_VERSION}/search/movie")
    fun searchForMovie(@Query("query") query: String,
                       @Query("page") page: Int = 1): Single<SearchResponse>

}