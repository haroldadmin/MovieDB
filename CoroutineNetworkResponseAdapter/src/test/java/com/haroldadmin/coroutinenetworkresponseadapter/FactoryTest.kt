package com.haroldadmin.coroutinenetworkresponseadapter

import com.google.common.reflect.TypeToken
import kotlinx.coroutines.Deferred
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.lang.reflect.Type

internal class FactoryTest {
    val mockWebServer = MockWebServer()
    val callAdapterFactory = CoroutinesNetworkResponseAdapterFactory()
    lateinit var retrofit: Retrofit

    @Before
    fun setup() {
       retrofit = Retrofit.Builder()
           .baseUrl(mockWebServer.url("/"))
           .addConverterFactory(StringConverterFactory())
           .addCallAdapterFactory(callAdapterFactory)
           .build()
    }


    @Test
    fun `When body type is not Deferred NetworkResponse then adapter is null`() {
        val bodyClass: Type = typeOf<Deferred<String>>()
        assertEquals(null, callAdapterFactory.get(bodyClass, emptyArray(), retrofit))
    }

    @Test
    fun `When body type is Deferred NetworkResponse then adapter handles this case properly`() {
        val bodyClass: Type = typeOf<Deferred<NetworkResponse<String, String>>>()
        assertTrue(callAdapterFactory.get(bodyClass, emptyArray(), retrofit)!!.responseType() == String::class.java)
    }
}