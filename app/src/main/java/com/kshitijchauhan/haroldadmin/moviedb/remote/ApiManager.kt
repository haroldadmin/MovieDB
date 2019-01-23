package com.kshitijchauhan.haroldadmin.moviedb.remote

import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.AccountService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.AddMediaToWatchlistRequest
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.MarkMediaAsFavoriteRequest
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.AuthenticationService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.CreateSessionRequest
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.discover.DiscoveryService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie.MovieService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.search.SearchService

class ApiManager (
    private val authenticationService: AuthenticationService,
    private val discoveryService: DiscoveryService,
    private val searchService: SearchService,
    private val movieService: MovieService,
    private val accountService: AccountService) {

    fun createGuestSession() = authenticationService.getGuestSessionToken()

    fun getPopularMovies() = discoveryService.getPopularMovies()

    fun getMoviesInTheatres(movieRegion: String = "IN") = discoveryService.getMoviesInTheatre(region = movieRegion)

    fun search(query: String) = searchService.searchForMovie(query)

    fun getMovieDetails(movieId: Int) = movieService.getMovieDetails(movieId)

    fun getAccountStatesForMovie(movieId: Int) = movieService.getAccountStatesForMovie(movieId)

    fun getVideosForMovie(movieId: Int) = movieService.getVideosForMovie(movieId)

    fun getCreditsForMovie(movieId: Int) = movieService.getCreditsForMovie(movieId)

    fun getTopRatedMovies() = discoveryService.getTopRatedMovies()

    fun getRequestToken() = authenticationService.getRequestToken()

    fun createSession(request: CreateSessionRequest) = authenticationService.createNewSession(request)

    fun getAccountDetails() = accountService.getAccountDetails()

    fun getMoviesWatchList(accountId: Int) = accountService.getMoviesWatchList(accountId)

    fun getFavouriteMovies(accountId: Int) = accountService.getFavouriteMovies(accountId)

    fun toggleMediaFavouriteStatus(accountId: Int, request: MarkMediaAsFavoriteRequest) =
            accountService.markMediaAsFavorite(accountId, request)

    fun toggleMediaWatchlistStatus(accountId: Int, request: AddMediaToWatchlistRequest) =
            accountService.addMediaToWatchlist(accountId, request)
}