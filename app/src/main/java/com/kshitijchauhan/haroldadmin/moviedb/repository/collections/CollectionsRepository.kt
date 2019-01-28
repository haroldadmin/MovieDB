package com.kshitijchauhan.haroldadmin.moviedb.repository.collections

import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable
import io.reactivex.Single

class CollectionsRepository(
    private val localCollectionsSource: LocalCollectionsSource,
    private val remoteCollectionsSource: RemoteCollectionsSource
) {

    /**
     * This method should be used with care because the upstream observables it is working on may not emit onComplete,
     * and the downstream might suffer because of it
     */
    fun getCollectionFlowable(accountId: Int = 0, type: CollectionType): Flowable<Collection> {
        return localCollectionsSource.isCollectionInDatabase(type)
            .flatMapPublisher { count ->
                if (count > 0) {
                    log("Collection already exists in database")
                    localCollectionsSource.getCollectionFlowable(type)
                } else {
                    log("Getting collection from network")
                    remoteCollectionsSource.getCollectionFlowable(accountId, type)
                        .doOnNext { collection ->
                            log("Writing collection to database")
                            localCollectionsSource.saveCollection(collection)
                        }
                        .switchMap {
                            localCollectionsSource.getCollectionFlowable(type)
                        }
                }
            }
    }

    fun getCollection(accountId: Int = 0, type: CollectionType): Single<Collection> {
        return localCollectionsSource.isCollectionInDatabase(type)
            .flatMap { count ->
                if (count > 0) {
                    log("Collection already exists in database")
                    localCollectionsSource.getCollection(type)
                } else {
                    log("Getting collection from network")
                    remoteCollectionsSource.getCollection(accountId, type)
                        .doOnSuccess { collection ->
                            log("Writing collection to database")
                            localCollectionsSource.saveCollection(collection)
                        }
                        .flatMap {
                            localCollectionsSource.getCollection(type)
                        }
                }
            }
    }

    fun getMoviesInCollection(accountId: Int = 0, type: CollectionType): Single<List<Movie>> {
        return localCollectionsSource.isCollectionInDatabase(type)
            .flatMap<List<Movie>> { count ->
                if (count > 0) {
                    log("Collection already exists in database")
                    localCollectionsSource.getCollection(type)
                        .flatMap { collection ->
                            localCollectionsSource.getMoviesForCollection(collection)
                        }
                        .doOnSuccess { list ->
                            log("Returning list of ${list.size} movies ")
                        }
                } else {
                    log("Getting collection from network")
                    remoteCollectionsSource.getCollection(accountId, type)
                        .doOnSuccess { collection ->
                            log("Writing collection to the database")
                            localCollectionsSource.saveCollection(collection)
                        }
                        .flatMap { collection ->
                            localCollectionsSource.getMoviesForCollection(collection)
                        }
                }
            }
    }

    fun getMoviesInCollectionFlowable(accountId: Int = 0, type: CollectionType): Flowable<List<Movie>> {
        return localCollectionsSource.isCollectionInDatabase(type)
            .flatMapPublisher { count ->
                if (count > 0) {
                    log("Collection already exists in database")
                    localCollectionsSource.getCollectionFlowable(type)
                        .flatMap { collection ->
                            localCollectionsSource.getMoviesForCollectionFlowable(collection)
                        }
                } else {
                    log("Fetching collection from the network")
                    remoteCollectionsSource.getCollectionFlowable(accountId, type)
                        .doOnNext { collection ->
                            log("Writing collection to the database")
                            localCollectionsSource.saveCollection(collection)
                        }
                        .flatMap { collection ->
                            localCollectionsSource.getMoviesForCollectionFlowable(collection)
                        }
                }
            }
    }

    fun forceRefreshCollection(accountId: Int = 0, type: CollectionType): Single<Collection> {
        log("Force refreshing collection")
        return remoteCollectionsSource.getCollection(accountId, type)
            .doOnSuccess { collection ->
                log("Writing collection to the database")
                localCollectionsSource.saveCollection(collection)
            }
    }
}