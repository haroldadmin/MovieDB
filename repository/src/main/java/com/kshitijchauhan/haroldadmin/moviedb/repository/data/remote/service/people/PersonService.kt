package com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.people

import com.haroldadmin.cnradapter.NetworkResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.common.ErrorResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

internal interface PersonService {

    @GET("person/{personId}")
    fun getPerson(@Path("personId") id: Int): Deferred<NetworkResponse<PersonResponse, ErrorResponse>>

}