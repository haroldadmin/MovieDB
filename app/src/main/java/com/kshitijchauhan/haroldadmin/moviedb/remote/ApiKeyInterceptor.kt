package com.kshitijchauhan.haroldadmin.moviedb.remote

import com.kshitijchauhan.haroldadmin.moviedb.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url().newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY)
            .build()

        chain.request()
            .newBuilder()
            .url(newUrl)
            .build()
            .also { newRequest ->
                return chain.proceed(newRequest)
            }
    }
}