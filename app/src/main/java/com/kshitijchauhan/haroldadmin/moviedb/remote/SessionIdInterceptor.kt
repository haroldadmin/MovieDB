package com.kshitijchauhan.haroldadmin.moviedb.remote

import okhttp3.Interceptor
import okhttp3.Response

class SessionIdInterceptor(val sessionId: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url()
            .newBuilder()
            .addQueryParameter("session_id", sessionId)
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