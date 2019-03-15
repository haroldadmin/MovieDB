package com.kshitijchauhan.haroldadmin.moviedb.repository.actors

import com.kshitijchauhan.haroldadmin.moviedb.core.NetworkBoundResource
import com.kshitijchauhan.haroldadmin.moviedb.core.Resource
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith

class ActorsRepository(
    private val localActorsSource: LocalActorsSource,
    private val remoteActorsSource: RemoteActorsSource
) {

    fun getActorResource(id: Int): NetworkBoundResource<Actor> {
        return object : NetworkBoundResource<Actor>() {
            override fun fetchFromNetwork(): Flowable<Resource<Actor>> {
                return remoteActorsSource.getActor(id).toFlowable()
            }

            override fun fetchFromDatabase(): Flowable<Resource<Actor>> {
                return localActorsSource
                    .getActorFlowable(id)
                    .map { actor ->
                        Resource.Success(actor)
                    }
            }

            override fun shouldRefresh(): Single<Boolean> {
                val isInDb = localActorsSource
                    .isActorInDatabase(id)
                    .map { count -> count > 0 }

                val isModelComplete = localActorsSource.getActor(id)
                    .map { actor -> actor.isModelComplete }

                return isInDb.zipWith(isModelComplete) {
                    dbStatus, modelStatus -> !(dbStatus && modelStatus)
                }
            }

            override fun saveToDatabase(actor: Actor) {
                localActorsSource.saveActorToDatabase(actor)
            }
        }
    }

    fun forceRefreshActorResource(id: Int): NetworkBoundResource<Actor> {
        return object : NetworkBoundResource<Actor>() {
            override fun fetchFromNetwork(): Flowable<Resource<Actor>> {
                return remoteActorsSource.getActor(id).toFlowable()
            }

            override fun fetchFromDatabase(): Flowable<Resource<Actor>> {
                return localActorsSource.getActorFlowable(id)
                    .map { actor ->
                        Resource.Success(actor)
                    }
            }

            override fun shouldRefresh(): Single<Boolean> {
                return Single.just(true)
            }

            override fun saveToDatabase(actor: Actor) {
                localActorsSource.saveActorToDatabase(actor)
            }
        }
    }
}