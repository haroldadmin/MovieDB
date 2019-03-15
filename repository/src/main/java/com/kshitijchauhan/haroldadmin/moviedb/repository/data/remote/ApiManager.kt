package com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote

import com.kshitijchauhan.haroldadmin.moviedb.core.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.AccountDetailsResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.AccountService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.ToggleMediaWatchlistStatusRequest
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.ToggleMediaFavouriteStatusRequest
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.auth.AuthenticationService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.auth.CreateSessionRequest
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.discover.DiscoveryService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.movie.MovieService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.search.SearchService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.utils.NetworkResponse
import io.reactivex.Single

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

    fun getRequestToken(): Single<Resource<String>> {
        return authenticationService
            .getRequestToken()
            .flatMap { response ->
                Single.just(when (response) {
                    is NetworkResponse.Success -> {
                        Resource.Success(response.body.requestToken)
                    }
                    is NetworkResponse.ServerError -> {
                        Resource.Error<String>(response.body?.statusMessage ?: "Server Error")
                    }
                    is NetworkResponse.NetworkError -> {
                        Resource.Error(response.error.localizedMessage ?: "Network Error")
                    }
                })
            }
    }

    fun createSession(request: CreateSessionRequest): Single<Resource<String>> {
        return authenticationService.createNewSession(request)
            .flatMap { response ->
                Single.just(when (response) {
                    is NetworkResponse.Success -> {
                        Resource.Success(response.body.sessionId)
                    }
                    is NetworkResponse.ServerError -> {
                        Resource.Error<String>(response.body?.statusMessage ?: "Server Error")
                    }
                    is NetworkResponse.NetworkError -> {
                        Resource.Error(response.error.localizedMessage ?: "Network Error")
                    }
                })
            }
    }

    fun getAccountDetails(): Single<Resource<AccountDetailsResponse>> {
        return accountService
            .getAccountDetails()
            .flatMap { response ->
                Single.just(when (response) {
                    is NetworkResponse.Success -> {
                        Resource.Success(response.body)
                    }
                    is NetworkResponse.ServerError -> {
                        Resource.Error<AccountDetailsResponse>(response.body?.statusMessage ?: "Server Error")
                    }
                    is NetworkResponse.NetworkError -> {
                        Resource.Error(response.error.localizedMessage ?: "Network Error")
                    }
                })
            }
    }

    fun getMoviesWatchList(accountId: Int) = accountService.getMoviesWatchList(accountId)

    fun getFavouriteMovies(accountId: Int) = accountService.getFavouriteMovies(accountId)

    fun toggleMediaFavouriteStatus(accountId: Int, request: ToggleMediaFavouriteStatusRequest) =
            accountService.toggleMediaFavouriteStatus(accountId, request)

    fun toggleMediaWatchlistStatus(accountId: Int, request: ToggleMediaWatchlistStatusRequest) =
            accountService.toggleMediaWatchlistStatus(accountId, request)
}