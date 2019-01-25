package com.kshitijchauhan.haroldadmin.moviedb.repository.actors

import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Actor
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ActorsRepository(
    private val localActorsSource: LocalActorsSource,
    private val remoteActorsSource: RemoteActorsSource
) {

    fun getActor(id: Int, compositeDisposable: CompositeDisposable): Flowable<Actor> {
        remoteActorsSource.getActor(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .doOnSuccess { actor ->
                localActorsSource.saveActorToDatabase(actor)
            }
            .subscribe()
            .disposeWith(compositeDisposable)

        return localActorsSource.getActor(id)
    }

    fun getAllActors(ids: List<Int>, compositeDisposable: CompositeDisposable): Flowable<List<Actor>> {
        remoteActorsSource.getAllActors(ids)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .subscribe(
                { actors: List<Actor> ->
                    log("Writing retrieved ${actors.size} actors to the database")
                    localActorsSource.saveAllActorsToDatabase(actors)
                }, { error ->
                    log(error.localizedMessage)
                }
            )
            .disposeWith(compositeDisposable)

        return localActorsSource.getActors(ids)
            .doOnNext {
                log("Retrieved list of actors from database: $it")
            }
    }
}