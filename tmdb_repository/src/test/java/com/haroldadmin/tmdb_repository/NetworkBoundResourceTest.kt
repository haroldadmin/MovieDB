package com.haroldadmin.tmdb_repository

import com.kshitijchauhan.haroldadmin.moviedb.core.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NetworkBoundResourceTest {

    private val mainThreadSurrogate = newSingleThreadContext("Main thread")
    lateinit var job: Job
    lateinit var coroutineScope: CoroutineScope

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        job = SupervisorJob()
        coroutineScope = CoroutineScope(Dispatchers.Main + job)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `First emission should be resource loading`() = runBlocking {
        val resource = object : NetworkBoundResource<String>(coroutineScope) {
            override suspend fun fetchFromNetwork(): Resource<String> = withContext(Dispatchers.IO) {
                Resource.Success("Hello from network!")
            }

            override suspend fun fetchFromDisk(): Resource<String> = withContext(Dispatchers.IO) {
                Resource.Success("Hello from disk!")
            }

            override suspend fun shouldRefreshDiskModel(
                diskResource: Resource<String>,
                networkResource: Resource<String>
            ): Boolean = withContext(Dispatchers.IO) {
                true
            }

            override suspend fun saveToDisk(resource: String) = withContext(Dispatchers.IO) {}

        }

        resource.result.take(1).collect {
            assertTrue(it is Resource.Loading<String>)
        }
    }

    @Test(expected = AssertionError::class)
    fun `Should throw error when network is unsuccesful and should save to disk is true`() = runBlocking {
        val resource = object : NetworkBoundResource<String>(coroutineScope) {
            override suspend fun fetchFromNetwork(): Resource<String> = withContext(Dispatchers.IO) {
                Resource.Error<String>("Error on network!")
            }

            override suspend fun fetchFromDisk(): Resource<String> = withContext(Dispatchers.IO) {
                Resource.Success("Hello from disk!")
            }

            override suspend fun shouldRefreshDiskModel(
                diskResource: Resource<String>,
                networkResource: Resource<String>
            ): Boolean = withContext(Dispatchers.IO) {
                true
            }

            override suspend fun saveToDisk(resource: String) = withContext(Dispatchers.IO) {}

        }
        resource.result.collect { println(it) }
    }

    @Test
    fun `When network errors and disk is successful only disk is emitted`() = runBlocking {
        val resource = object : NetworkBoundResource<String>(coroutineScope) {
            override suspend fun fetchFromNetwork(): Resource<String> = withContext(Dispatchers.IO) {
                Resource.Error<String>("Error from network!")
            }

            override suspend fun fetchFromDisk(): Resource<String> = withContext(Dispatchers.IO) {
                Resource.Success("Hello from disk!")
            }

            override suspend fun shouldRefreshDiskModel(
                diskResource: Resource<String>,
                networkResource: Resource<String>
            ): Boolean = withContext(Dispatchers.IO) {
                false
            }

            override suspend fun saveToDisk(resource: String) = withContext(Dispatchers.IO) {}
        }

        val numberOfElements = resource.result.filter { it is Resource.Error }.count()
        assertEquals(0, numberOfElements)
    }
}