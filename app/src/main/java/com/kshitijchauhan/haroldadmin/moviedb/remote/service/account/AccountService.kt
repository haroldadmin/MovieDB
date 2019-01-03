package com.kshitijchauhan.haroldadmin.moviedb.remote.service.account

import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface AccountService {

    @GET("${Config.API_VERSION}/account")
    fun getAccountDetails(): Single<AccountDetailsResponse>

    @GET("${Config.API_VERSION}/account/{accountId}/watchlist/movies")
    fun getMoviesWatchList(@Path("accountId") accountId: Int): Single<MovieWatchlistResponse>

}