package com.kshitijchauhan.haroldadmin.moviedb.remote

import com.kshitijchauhan.haroldadmin.moviedb.di.AppScope
import com.kshitijchauhan.haroldadmin.moviedb.model.Movie
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.AuthenticationService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.discover.DiscoveryService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.search.SearchService
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

@AppScope
class ApiManager @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val discoveryService: DiscoveryService,
    private val searchService: SearchService
) {

    fun createGuestSession() = authenticationService.getGuestSessionToken()

    fun getPopularMovies() = discoveryService.discoverMovies()

    fun search(query: String) = searchService.searchForMovie(query)
}