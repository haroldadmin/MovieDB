package com.haroldadmin.coroutinenetworkresponseadapter

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.http.GET

internal class DeferredTest {

    interface Service {
        @GET("/")
        fun getText(): Deferred<NetworkResponse<String, String>>
    }

    val server = MockWebServer()
    lateinit var service: Service

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(StringConverterFactory())
            .addCallAdapterFactory(CoroutinesNetworkResponseAdapterFactory())
            .build()
        service = retrofit.create(Service::class.java)
    }


    @Test
    fun `Successful response is treated as NetworkResponse Success`() = runBlocking {
        server.enqueue(MockResponse().setBody("Hi!").setResponseCode(200))
        val response = service.getText().await()
        assertTrue(response is NetworkResponse.Success<String>)
        assertEquals("Hi!", (response as NetworkResponse.Success<String>).body)
    }

    @Test
    fun `Empty response is treated as NetworkResponse ServerError`() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(404))
        val response = service.getText().await()
        assertTrue(response is NetworkResponse.ServerError<String>)
        assertTrue((response as NetworkResponse.ServerError<String>).body == null)
        assertTrue((response as NetworkResponse.ServerError<String>).code == 404)
    }

    @Test
    fun `IO Error is treated as NetworkResponse NetworkError`() = runBlocking {
        server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST))
        val response = service.getText().await()
        assertTrue(response is NetworkResponse.NetworkError)
    }
}