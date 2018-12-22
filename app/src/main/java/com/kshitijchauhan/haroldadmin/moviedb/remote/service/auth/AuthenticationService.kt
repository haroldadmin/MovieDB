package com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth

import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import io.reactivex.Single
import retrofit2.http.*

interface AuthenticationService {

    @GET("${Config.API_VERSION}/authentication/guest_session/new")
    fun getGuestSessionToken(): Single<GuestSessionResponse>

    @GET("${Config.API_VERSION}/authentication/token/new")
    fun getRequestToken(): Single<RequestTokenResponse>

    @POST("${Config.API_VERSION}/authentication/session/new")
    fun createNewSession(@Body request: CreateSessionRequest): Single<CreateSessionResponse>

    @DELETE("${Config.API_VERSION}/authentication/session")
    fun deleteSession(@Body request: DeleteSessionRequest): Single<DeleteSessionRequest>
}