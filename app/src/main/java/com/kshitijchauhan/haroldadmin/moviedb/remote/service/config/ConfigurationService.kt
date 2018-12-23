package com.kshitijchauhan.haroldadmin.moviedb.remote.service.config

import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import io.reactivex.Single
import retrofit2.http.GET

interface ConfigurationService {

    @GET("${Config.API_VERSION}/configuration")
    fun getConfiguration(): Single<ConfigurationResponse>

}