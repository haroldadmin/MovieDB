package com.kshitijchauhan.haroldadmin.moviedb.remote

import com.kshitijchauhan.haroldadmin.moviedb.di.AppScope
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.AuthenticationService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.discover.DiscoveryService
import javax.inject.Inject

@AppScope
class ApiManager @Inject constructor(private val authenticationService: AuthenticationService,
                                     private val discoveryService: DiscoveryService) {

    fun createGuestSession() = authenticationService.getGuestSessionToken()

    fun getPopularMovies() = discoveryService.discoverMovies()
}