package com.kshitijchauhan.haroldadmin.moviedb.ui.actor_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.ActorsRepository
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import io.reactivex.disposables.CompositeDisposable

class ActorDetailsViewModel(
    private val actorId: Int,
    private val actorsRepository: ActorsRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _actor = MediatorLiveData<Resource<Actor>>()
    private val _message = MutableLiveData<String>()

    val actor: LiveData<Resource<Actor>>
        get() = _actor

    val message: LiveData<String>
        get() = _message

    fun getActorDetails() {
        actorsRepository.getActorResource(actorId)
            .init(compositeDisposable)
            .subscribe { actor -> _actor.postValue(actor) }
            .disposeWith(compositeDisposable)
    }

    fun forceRefreshActorDetails() {
        actorsRepository.forceRefreshActorResource(actorId)
            .init(compositeDisposable)
            .subscribe { actor -> _actor.postValue(actor) }
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
