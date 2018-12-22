package com.kshitijchauhan.haroldadmin.moviedb.remote.service.discover

import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoveryService {

    @GET("${Config.API_VERSION}/discover/movie")
    fun discoverMovies(@Query("sort_by") sortBy: String = SortParameters.PopularityDsc,
                       @Query("page") page: Int = 1): Single<DiscoverMoviesResponse>

}