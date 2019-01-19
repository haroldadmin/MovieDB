package com.kshitijchauhan.haroldadmin.moviedb.remote.service.discover

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoveryService {

    @GET("discover/movie")
    fun tmdbDiscoverMovies(@Query("sort_by") sortBy: String = SortParameters.PopularityDsc,
                           @Query("page") page: Int = 1): Single<DiscoverMoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(): Single<DiscoverMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(): Single<DiscoverMoviesResponse>

}