package com.kshitijchauhan.haroldadmin.moviedb.repository.actors

import com.haroldadmin.cnradapter.NetworkResponse
import com.kshitijchauhan.haroldadmin.moviedb.core.extensions.log
import com.kshitijchauhan.haroldadmin.moviedb.core.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.people.PersonService
import com.kshitijchauhan.haroldadmin.moviedb.repository.toActor
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class RemoteActorsSource(
    private val personService: PersonService) {

    suspend fun getActor(id: Int): Single<Resource<Actor>> {
        log("Retrieving single actor from api")
        val person = withContext(Dispatchers.IO)
        return personService.getPerson(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMap { response ->
                Single.just<Resource<Actor>>(when(response) {
                    is NetworkResponse.Success -> {
                        Resource.Success(response.body.toActor())
                    }
                    is NetworkResponse.ServerError -> {
                        Resource.Error(response.body?.statusMessage ?: "Server Error")
                    }
                    is NetworkResponse.NetworkError -> {
                        Resource.Error(response.error.localizedMessage ?: "Network Error")
                    }
                })
            }
    }

    fun getAllActors(ids: List<Int>): Single<List<Resource<Actor>>> {
        log("Getting list of actors from api")
        return Flowable.fromIterable(ids)
            .flatMapSingle { id ->
                personService.getPerson(id)
            }
            .flatMapSingle { response ->
                Single.just(when(response) {
                    is NetworkResponse.Success -> {
                        Resource.Success(response.body.toActor())
                    }
                    is NetworkResponse.ServerError -> {
                        Resource.Error<Actor>(response.body?.statusMessage ?: "Server Error")
                    }
                    is NetworkResponse.NetworkError -> {
                        Resource.Error(response.error.localizedMessage ?: "Network Error")
                    }
                })
            }
            .toList()
    }
}