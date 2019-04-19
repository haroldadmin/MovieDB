package com.haroldadmin.coroutinenetworkresponseadapter

import com.google.common.reflect.TypeToken
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type

internal class CancelTest {
    val mockWebServer = MockWebServer()
    val factory = CoroutinesNetworkResponseAdapterFactory()
    val retrofit = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(StringConverterFactory())
        .addCallAdapterFactory(factory)
        .build()

    @Test
    fun noCancelOnResponse() {
        val deferredString: Type= typeOf<Deferred<NetworkResponse<String, String>>>()
        val adapter = factory.get(deferredString, emptyArray(), retrofit)!! as CallAdapter<String, Deferred<NetworkResponse<String, String>>>
        val call = CompletableCall<String>()
        val deferred = adapter.adapt(call)
        call.complete("hey")
        assertFalse(call.isCanceled)
        assertTrue(deferred.isCompleted)
    }

    @Test fun noCancelOnError() {
        val deferredString = typeOf<Deferred<NetworkResponse<String, String>>>()
        val adapter = factory.get(deferredString, emptyArray(), retrofit)!! as CallAdapter<String, Deferred<NetworkResponse<String, String>>>
        val call = CompletableCall<String>()
        val deferred = adapter.adapt(call)
        call.completeWithException(IOException())
        assertFalse(call.isCanceled)
    }

    @Test fun cancelOnCancel() {
        val deferredString = typeOf<Deferred<NetworkResponse<String, String>>>()
        val adapter = factory.get(deferredString, emptyArray(), retrofit)!! as CallAdapter<String, Deferred<NetworkResponse<String, String>>>
        val call = CompletableCall<String>()
        val deferred = adapter.adapt(call)
        assertFalse(call.isCanceled)
        deferred.cancel()
        assertTrue(call.isCanceled)
    }
}