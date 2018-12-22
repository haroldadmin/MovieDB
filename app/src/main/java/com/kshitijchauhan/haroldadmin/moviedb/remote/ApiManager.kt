package com.kshitijchauhan.haroldadmin.moviedb.remote

import com.kshitijchauhan.haroldadmin.moviedb.BuildConfig
import com.kshitijchauhan.haroldadmin.moviedb.di.AppScope
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.AuthenticationService
import javax.inject.Inject

@AppScope
class ApiManager @Inject constructor(private val authenticationService: AuthenticationService) {

    private val apiKey: String = BuildConfig.API_KEY

    fun createGuestSession() = authenticationService.getGuestSessionToken(apiKey)

}