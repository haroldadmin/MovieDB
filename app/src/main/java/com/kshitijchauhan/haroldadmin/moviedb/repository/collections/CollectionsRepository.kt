package com.kshitijchauhan.haroldadmin.moviedb.repository.collections

import com.kshitijchauhan.haroldadmin.moviedb.repository.NetworkBoundResource
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.LocalMoviesSource
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable
import io.reactivex.Single

class CollectionsRepository(
    private val localCollectionsSource: LocalCollectionsSource,
    private val remoteCollectionsSource: RemoteCollectionsSource,
    private val localMoviesResource: LocalMoviesSource
) {

    /**
     * This method should be used with care because the upstream observables it is working on may not emit onComplete,
     * and the downstream might suffer because of it
     */
    fun getCollectionFlowable(accountId: Int = 0, type: CollectionType, region: String = ""): NetworkBoundResource<List<Movie>> {

        return object : NetworkBoundResource<List<Movie>>() {
            override fun fetchFromNetwork(): Flowable<Resource<List<Movie>>> {
                return remoteCollectionsSource.getCollection(accountId, type, region)
                    .flatMap { response ->
                        Single.just(
                            when (response) {
                                is Resource.Success -> Resource.Success(response.data.movies ?: emptyList())
                                is Resource.Error -> Resource.Error(response.errorMessage)
                                is Resource.Loading -> Resource.Loading<List<Movie>>()
                            }
                        )
                    }
                    .toFlowable()
            }

            override fun fetchFromDatabase(): Flowable<Resource<List<Movie>>> {
                return localCollectionsSource.getCollectionFlowable(type)
                    .switchMap { collection ->
                        localCollectionsSource.getMoviesForCollectionFlowable(collection.contents)
                    }
                    .map { moviesInCollection ->
                        Resource.Success(moviesInCollection)
                    }
            }

            override fun shouldRefresh(): Single<Boolean> {
                return localCollectionsSource.isCollectionInDatabase(type)
                    .map { count -> count == 0 }
            }

            override fun saveToDatabase(movies: List<Movie>) {
                return localMoviesResource.saveMoviesToDatabase(movies)
            }
        }
    }

    fun forceRefreshCollection(accountId: Int = 0, type: CollectionType, region: String = ""): NetworkBoundResource<Collection> {
        log("Force refreshing collection")
        return object : NetworkBoundResource<Collection>() {
            override fun fetchFromNetwork(): Flowable<Resource<Collection>> {
                return remoteCollectionsSource.getCollection(accountId, type, region).toFlowable()
            }

            override fun fetchFromDatabase(): Flowable<Resource<Collection>> {
                return localCollectionsSource
                    .getCollectionFlowable(type)
                    .map { collection -> Resource.Success(collection) }
            }

            override fun shouldRefresh(): Single<Boolean> {
                return Single.just(true)
            }

            override fun saveToDatabase(collection: Collection) {
                localCollectionsSource.saveCollection(collection)
            }
        }
    }
}