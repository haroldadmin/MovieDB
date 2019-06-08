package com.haroldadmin.tmdb_repository.movie.remote

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.tmdb_repository.ErrorResponse
import com.haroldadmin.tmdb_repository.movie.remote.models.*
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    @GET("movie/{movieId}")
    fun getMovieDetails(@Path("movieId") movieId: Int): Deferred<NetworkResponse<MovieResponse, ErrorResponse>>

    @GET("movie/{movieId}/account_states")
    fun getAccountStatesForMovie(@Path("movieId") movieId: Int): Deferred<NetworkResponse<MovieStatesResponse, ErrorResponse>>

    @GET("movie/{movieId}/videos")
    fun getVideosForMovie(@Path("movieId") movieId: Int): Deferred<NetworkResponse<MovieVideosResponse, ErrorResponse>>

    @GET("movie/{movieId}/credits")
    fun getCreditsForMovie(@Path("movieId") movieId: Int): Deferred<NetworkResponse<MovieCreditsResponse, ErrorResponse>>

    @GET("movie/{movieId}/similar")
    fun getSimilarMoviesForMovie(@Path("movieId") movieId: Int): Deferred<NetworkResponse<SimilarMoviesResponse, ErrorResponse>>
}
