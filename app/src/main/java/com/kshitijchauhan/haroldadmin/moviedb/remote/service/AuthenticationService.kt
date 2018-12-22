package com.kshitijchauhan.haroldadmin.moviedb.remote.service

import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.*
import io.reactivex.Single
import retrofit2.http.*

interface AuthenticationService {

    @GET("3/authentication/guest_session/new")
    fun getGuestSessionToken(@Query("api_key") apiKey: String): Single<GuestSessionResponse>

    @GET("3/authentication/token/new")
    fun getRequestToken(@Query("api_key") apiKey: String): Single<RequestTokenResponse>

    @POST("3/authentication/session/new")
    fun createNewSession(@Body request: CreateSessionRequest): Single<CreateSessionResponse>

    @DELETE("3/authentication/session")
    fun deleteSession(@Body request: DeleteSessionRequest): Single<DeleteSessionRequest>
}