package com.haroldadmin.tmdb_repository.actor.remote

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.tmdb_repository.ErrorResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

internal interface PersonService {

    @GET("person/{personId}")
    fun getPerson(@Path("personId") id: Int): Deferred<NetworkResponse<PersonResponse, ErrorResponse>>

}