package com.kshitijchauhan.haroldadmin.moviedb.repository.actors

import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.people.PersonService
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.toActor
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RemoteActorsSource(
    private val personService: PersonService) {

    fun getActor(id: Int): Single<Actor> {
        log("Retrieving single actor from api")
        return personService.getPerson(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { personResponse ->
                personResponse.toActor()
            }
    }

    fun getAllActors(ids: List<Int>): Single<List<Actor>> {
        log("Getting list of actors from api")
        return Flowable.fromIterable(ids)
            .flatMapSingle { id ->
                getActor(id)
            }
            .toList()
            .doOnSuccess {
                log("Retrieved actor from api: $it")
            }
    }
}