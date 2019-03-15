package com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.utils

import okhttp3.Interceptor
import okhttp3.Response

class SessionIdInterceptor(private var sessionId: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        if (sessionId.isEmpty()) {
            return chain.proceed(chain.request())
        }

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

    fun setSessionId(newId: String) {
        this.sessionId = newId
    }
}