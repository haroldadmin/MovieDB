package com.haroldadmin.tmdb_repository

import com.kshitijchauhan.haroldadmin.moviedb.core.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundResource<T>(private val coroutineScope: CoroutineScope) {

    val result = flow<Resource<T>> {

        emit(Resource.Loading())

        val diskDeferredResource = coroutineScope.async { fetchFromDisk() }
        val networkDeferredResource = coroutineScope.async { fetchFromNetwork() }

        val diskResource = diskDeferredResource.await()
        emit(diskResource)

        val networkResource = networkDeferredResource.await()
        if (shouldRefreshDiskModel(diskResource, networkResource)) {
            emit(networkResource)
            assert(networkResource is Resource.Success)
            saveToDisk((networkResource as Resource.Success).data)
        }
    }

    abstract suspend fun fetchFromNetwork(): Resource<T>
    abstract suspend fun fetchFromDisk(): Resource<T>
    abstract suspend fun shouldRefreshDiskModel(diskResource: Resource<T>, networkResource: Resource<T>): Boolean
    abstract suspend fun saveToDisk(resource: T)
}