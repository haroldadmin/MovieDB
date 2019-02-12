package com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.movie

import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.common.ErrorResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.utils.NetworkResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    @GET("movie/{movieId}")
    fun getMovieDetails(@Path("movieId") movieId: Int): Single<NetworkResponse<MovieResponse, ErrorResponse>>

    @GET("movie/{movieId}/account_states")
    fun getAccountStatesForMovie(@Path("movieId") movieId: Int): Single<NetworkResponse<MovieStatesResponse, ErrorResponse>>

    @GET("movie/{movieId}/videos")
    fun getVideosForMovie(@Path("movieId") movieId: Int): Single<NetworkResponse<MovieVideosResponse, ErrorResponse>>

    @GET("movie/{movieId}/credits")
    fun getCreditsForMovie(@Path("movieId") movieId: Int): Single<NetworkResponse<MovieCreditsResponse, ErrorResponse>>
}