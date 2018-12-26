package com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie

import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    @GET("${Config.API_VERSION}/movie/{movieId}")
    fun getMovieDetails(@Path("movieId") movieId: Int): Single<Movie>
}