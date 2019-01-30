package com.kshitijchauhan.haroldadmin.moviedb.repository.actors

import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable
import io.reactivex.Single

class ActorsRepository(
    private val localActorsSource: LocalActorsSource,
    private val remoteActorsSource: RemoteActorsSource
) {

    fun getActorFlowable(id: Int): Flowable<Actor> {
        return localActorsSource.isActorInDatabase(id)
            .flatMapPublisher<Actor> { count ->
                if (count > 0) {
                    log("Actor already exists in database")
                    localActorsSource.getActor(id).toFlowable()
                } else {
                    log("Retrieving actor from api")
                    remoteActorsSource.getActor(id).toFlowable()
                        .doOnNext { actor ->
                            log("Saving actor to database")
                            localActorsSource.saveActorToDatabase(actor)
                        }
                        .flatMapSingle {
                            localActorsSource.getActor(id)
                        }
                }
            }
    }

    fun getAllActorsFlowable(ids: List<Int>): Flowable<List<Actor>> {
        return Flowable.fromIterable(ids)
            .flatMap { id ->
                log("Retrieving actor for id: $id")
                this.getActorFlowable(id)
            }
            .toList()
            .doOnSuccess {
                log("List of actors: $it")
            }
            .toFlowable()
    }

    fun getActor(id: Int): Single<Actor> {
        return localActorsSource.isActorInDatabase(id)
            .flatMap { count ->
                if (count > 0) {
                    log("Retrieving actor from the database")
                    localActorsSource.getActor(id)
                } else {
                    log("Retrieving actor from the network")
                    remoteActorsSource.getActor(id)
                        .doOnSuccess { actor ->
                            localActorsSource.saveActorToDatabase(actor)
                        }
                        .flatMap {
                            localActorsSource.getActor(id)
                        }
                }
            }
    }

    fun getAllActors(ids: List<Int>, itemsCount: Int = ids.size): Single<List<Actor>> {
        return Flowable.fromIterable(ids)
            .take(itemsCount.toLong())
            .flatMapSingle { id ->
                this.getActor(id)
            }
            .toList()
    }
}