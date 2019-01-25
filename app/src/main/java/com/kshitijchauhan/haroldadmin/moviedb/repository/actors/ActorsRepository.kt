package com.kshitijchauhan.haroldadmin.moviedb.repository.actors

import com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.model.Actor
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable

class ActorsRepository(
    private val localActorsSource: LocalActorsSource,
    private val remoteActorsSource: RemoteActorsSource
) {

    fun getActor(id: Int): Flowable<Actor> {

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

    fun getAllActors(ids: List<Int>): Flowable<List<Actor>> {

        return Flowable.fromIterable(ids)
            .flatMap { id ->
                log("Retrieving actor for id: $id")
                this.getActor(id)
            }
            .toList()
            .doOnSuccess {
                log("List of actors: $it")
            }
            .toFlowable()
    }
}