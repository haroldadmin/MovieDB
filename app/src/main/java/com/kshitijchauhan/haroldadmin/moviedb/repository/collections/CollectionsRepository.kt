package com.kshitijchauhan.haroldadmin.moviedb.repository.collections

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
    fun getCollectionFlowable(accountId: Int, type: CollectionType): Flowable<Collection> {
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

    /**
     * This version of the method is needed to ensure that downstream knows this observable has completed emission.
     * Otherwise, the downstream observers never know when this observable will complete
     */
    fun getCollection(accountId: Int = 0, type: CollectionType): Single<Collection> {
        return localCollectionsSource.isCollectionInDatabase(type)
            .flatMap<Collection>{ count ->
                if (count > 0) {
                    log("Collection already exists in database")
                    localCollectionsSource.getCollection(type)
                } else {
                    log("Getting collection from network")
                    remoteCollectionsSource.getCollection(accountId, type)
                        .doOnSuccess { collection ->
                            log("Writing collection to the database")
                            localCollectionsSource.saveCollection(collection)
                        }
                        .flatMap<Collection> {
                            localCollectionsSource.getCollection(type)
                        }
                }
            }
    }
}