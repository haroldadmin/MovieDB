package com.kshitijchauhan.haroldadmin.moviedb.repository.remote.service.account

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountService {

    @GET("account")
    fun getAccountDetails(): Single<AccountDetailsResponse>

    @GET("account/{accountId}/watchlist/movies")
    fun getMoviesWatchList(@Path("accountId") accountId: Int): Single<MovieWatchlistResponse>

    @GET("account/{accountId}/favorite/movies")
    fun getFavouriteMovies(@Path("accountId") accountId: Int): Single<FavouriteMoviesResponse>

    @POST("account/{accountId}/favorite")
    fun markMediaAsFavorite(@Path("accountId") accountId: Int,
                            @Body request: MarkMediaAsFavoriteRequest): Single<MarkAsFavoriteResponse>

    @POST("account/{accountId}/watchlist")
    fun addMediaToWatchlist(@Path("accountId") accountId: Int,
                            @Body request: AddMediaToWatchlistRequest): Single<AddToWatchlistResponse>
}

