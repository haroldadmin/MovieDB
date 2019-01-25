package com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.config

import io.reactivex.Single
import retrofit2.http.GET

interface ConfigurationService {

    @GET("configuration")
    fun getConfiguration(): Single<ConfigurationResponse>

}