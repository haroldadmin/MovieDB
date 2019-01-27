package com.kshitijchauhan.haroldadmin.moviedb.repository.actors

import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.people.PersonResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.people.PersonService
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable
import io.reactivex.Single

class RemoteActorsSource(
    private val personService: PersonService) {

    fun getActor(id: Int): Single<Actor> {
        log("Retrieving single actor from api")
        return personService.getPerson(id)
            .map { response ->
                mapPersonResponseToActor(response)
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

    private fun mapPersonResponseToActor(personResponse: PersonResponse): Actor {
        return Actor(
            personResponse.id,
            personResponse.profilePath ?: "",
            personResponse.name
        )
    }

}