package com.kshitijchauhan.haroldadmin.moviedb.remote

import com.kshitijchauhan.haroldadmin.moviedb.di.AppScope
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.AuthenticationService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.discover.DiscoveryService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie.MovieService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.search.SearchService
import javax.inject.Inject

@AppScope
class ApiManager @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val discoveryService: DiscoveryService,
    private val searchService: SearchService,
    private val movieService: MovieService) {

    fun createGuestSession() = authenticationService.getGuestSessionToken()

    fun getPopularMovies() = discoveryService.getPopularMovies()

    fun search(query: String) = searchService.searchForMovie(query)

    fun getMovieDetails(movieId: Int) = movieService.getMovieDetails(movieId)

    fun getTopRatedMovies() = discoveryService.getTopRatedMovies()
}