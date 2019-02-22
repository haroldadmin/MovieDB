package com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionType
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionsRepository
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import com.kshitijchauhan.haroldadmin.mvrxlite.base.MVRxLiteViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException

class InTheatresViewModel(
    private val collectionsRepository: CollectionsRepository,
    initialState: UIState.InTheatresScreenState
) : MVRxLiteViewModel<UIState.InTheatresScreenState>(initialState) {

    private val _message = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    val message: LiveData<String>
        get() = _message

    fun getMoviesInTheatres() {
        collectionsRepository.getCollectionFlowable(type = CollectionType.InTheatres)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { inTheatreMovies ->
                    setState { copy(inTheatresMoviesResource = inTheatreMovies) }
                },
                onError = { error -> handleError(error, "get-movies-in-theatres") }
            )
            .disposeWith(compositeDisposable)
    }

    fun forceRefreshInTheatresCollection() {
        collectionsRepository.forceRefreshCollection(type = CollectionType.InTheatres)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}