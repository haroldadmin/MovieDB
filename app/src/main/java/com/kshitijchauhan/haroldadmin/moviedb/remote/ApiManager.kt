package com.kshitijchauhan.haroldadmin.moviedb.remote

import com.kshitijchauhan.haroldadmin.moviedb.di.AppScope
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.AuthenticationService
import javax.inject.Inject

@AppScope
class ApiManager @Inject constructor(private val authenticationService: AuthenticationService) {

    fun createGuestSession() = authenticationService.getGuestSessionToken()

}