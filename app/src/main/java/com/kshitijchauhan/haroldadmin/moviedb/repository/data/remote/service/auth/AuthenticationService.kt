package com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.auth

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationService {

    @GET("authentication/guest_session/new")
    fun getGuestSessionToken(): Single<GuestSessionResponse>

    @GET("authentication/token/new")
    fun getRequestToken(): Single<RequestTokenResponse>

    @POST("authentication/session/new")
    fun createNewSession(@Body request: CreateSessionRequest): Single<CreateSessionResponse>

    @DELETE("authentication/session")
    fun deleteSession(@Body request: DeleteSessionRequest): Single<DeleteSessionRequest>
}