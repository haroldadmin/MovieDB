package com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.auth

import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.common.ErrorResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.utils.NetworkResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationService {

    @GET("authentication/guest_session/new")
    fun getGuestSessionToken(): Single<NetworkResponse<GuestSessionResponse, ErrorResponse>>

    @GET("authentication/token/new")
    fun getRequestToken(): Single<NetworkResponse<RequestTokenResponse, ErrorResponse>>

    @POST("authentication/session/new")
    fun createNewSession(@Body request: CreateSessionRequest): Single<NetworkResponse<CreateSessionResponse, ErrorResponse>>

    @DELETE("authentication/session")
    fun deleteSession(@Body request: DeleteSessionRequest): Single<NetworkResponse<DeleteSessionRequest, ErrorResponse>>
}