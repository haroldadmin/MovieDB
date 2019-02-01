package com.kshitijchauhan.haroldadmin.moviedb.ui.actor_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.ActorsRepository
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException

class ActorDetailsViewModel(
    private val actorId: Int,
    private val actorsRepository: ActorsRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _actor = MutableLiveData<Actor>()
    private val _message = MutableLiveData<String>()

    val actor: LiveData<Actor>
        get() = _actor

    val message: LiveData<String>
        get() = _message

    fun getActorDetails() {
        actorsRepository.getActorFlowable(actorId)
            .subscribeOn(Schedulers.io())
            .doOnNext {
                if (!it.isModelComplete) {
                    log("Actor model is incomplete, force refreshing")
                    forceRefreshActorDetails(actorId)
                }
            }
            .subscribe(
                { actor ->
                    log("Retrieved actor model from db: $actor")
                    _actor.postValue(actor)
                },
                { error ->
                    handleError(error, "get-actor-details")
                }
            )
            .disposeWith(compositeDisposable)
    }

    private fun handleError(error: Throwable, caller: String) {
        error.localizedMessage?.let {
            log("ERROR $caller -> $it")
        } ?: log("ERROR $caller ->")
            .also {
                error.printStackTrace()
            }
        when (error) {
            is IOException -> _message.postValue("Please check your internet connection")
            is TimeoutException -> _message.postValue("Request timed out")
            else -> _message.postValue("An error occurred")
        }
    }

    fun forceRefreshActorDetails(id: Int) {
        actorsRepository.forceRefreshActor(actorId)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    log("Successfully refreshed actor model")
                },
                { error ->
                    handleError(error, "force-refresh-actor")
                }
            )
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
