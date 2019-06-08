package com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.search

import com.haroldadmin.cnradapter.NetworkResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.common.ErrorResponse
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

internal interface SearchService {

    @GET("search/movie")
    fun searchForMovie(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): Deferred<NetworkResponse<SearchResponse, ErrorResponse>>

}