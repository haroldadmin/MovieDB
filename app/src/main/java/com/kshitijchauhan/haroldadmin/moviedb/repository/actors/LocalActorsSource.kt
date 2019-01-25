package com.kshitijchauhan.haroldadmin.moviedb.repository.actors

import com.kshitijchauhan.haroldadmin.moviedb.repository.local.dao.ActorsDao
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Actor
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable

class LocalActorsSource(private val actorsDao: ActorsDao) {

    fun getActor(id: Int) = actorsDao.getActor(id)
        .doOnSuccess {
            log("Retrieved actor from database: $it")
        }

    fun getActorBlocking(id: Int) = actorsDao.getActorBlocking(id)

    fun getActors(ids: List<Int>): Flowable<List<Actor>> {
        log("Getting list of actors from database")
        return actorsDao.getActors(ids)
    }

    fun isActorInDatabase(id: Int) = actorsDao.isActorInDatabase(id)

    fun saveActorToDatabase(actor: Actor) = actorsDao.saveActor(actor)

    fun saveAllActorsToDatabase(actors: List<Actor>) = actorsDao.saveAllActors(actors)
}