package com.kshitijchauhan.haroldadmin.moviedb.remote.service.discover

import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoveryService {

    @GET("${Config.API_VERSION}/discover/movie")
    fun tmdbDiscoverMovies(@Query("sort_by") sortBy: String = SortParameters.PopularityDsc,
                           @Query("page") page: Int = 1): Single<DiscoverMoviesResponse>

    @GET("${Config.API_VERSION}/movie/popular")
    fun getPopularMovies(): Single<DiscoverMoviesResponse>

    @GET("${Config.API_VERSION}/movie/top_rated")
    fun getTopRatedMovies(): Single<DiscoverMoviesResponse>

}