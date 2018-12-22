package com.kshitijchauhan.haroldadmin.moviedb.remote.service

import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.*
import io.reactivex.Single
import retrofit2.http.*

interface AuthenticationService {

    @GET("/authentication/guest_session/new")
    fun getGuestSessionToken(@Query("api_key") apiKey: String): Single<GuestSessionResponse>

    @GET("/authentication/token/new")
    fun getRequestToken(@Query("api_key") apiKey: String): Single<RequestTokenResponse>

    @POST("/authentication/session/new")
    fun createNewSession(@Body request: CreateSessionRequest): Single<CreateSessionResponse>

    @DELETE("/authentication/session")
    fun deleteSession(@Body request: DeleteSessionRequest): Single<DeleteSessionRequest>
}