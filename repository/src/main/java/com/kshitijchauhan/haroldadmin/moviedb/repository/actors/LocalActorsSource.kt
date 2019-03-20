package com.kshitijchauhan.haroldadmin.moviedb.repository.actors

import io.reactivex.Flowable

internal class LocalActorsSource(private val actorsDao: ActorsDao) {

    fun getActor(id: Int) = actorsDao.getActor(id)

    fun getActorFlowable(id: Int) = actorsDao.getActorFlowable(id)

    fun getActorBlocking(id: Int) = actorsDao.getActorBlocking(id)

    fun getActors(ids: List<Int>): Flowable<List<Actor>> {
        return actorsDao.getActors(ids)
    }

    fun isActorInDatabase(id: Int) = actorsDao.isActorInDatabase(id)

    fun saveActorToDatabase(actor: Actor) = actorsDao.saveActor(actor)

    fun saveAllActorsToDatabase(actors: List<Actor>) = actorsDao.saveAllActors(actors)

    fun updateActorInDatabase(actor: Actor) = actorsDao.updateActor(actor)
}