package com.kshitijchauhan.haroldadmin.moviedb.ui.actor_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.core.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.ActorsRepository
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.mvrxlite.base.MVRxLiteViewModel
import io.reactivex.disposables.CompositeDisposable

class ActorDetailsViewModel(
    private val actorId: Int,
    private val actorsRepository: ActorsRepository,
    initialState: UIState.ActorDetailsScreenState
) : MVRxLiteViewModel<UIState.ActorDetailsScreenState>(initialState) {

    private val compositeDisposable = CompositeDisposable()
    private val _message = MutableLiveData<String>()

    val message: LiveData<String>
        get() = _message

    init {
        getActorDetails()
    }

    fun getActorDetails() {
        actorsRepository.getActorResource(actorId)
            .init(compositeDisposable)
            .subscribe { actor ->
                setState { copy(actorResource = actor) }
            }
            .disposeWith(compositeDisposable)
    }

    fun forceRefreshActorDetails() {
        actorsRepository.forceRefreshActorResource(actorId)
            .init(compositeDisposable)
            .subscribe { actor ->
                setState { copy(actorResource = actor) }
            }
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
