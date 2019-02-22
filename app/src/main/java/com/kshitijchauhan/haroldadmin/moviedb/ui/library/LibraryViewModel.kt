package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionType
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionsRepository
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import com.kshitijchauhan.haroldadmin.mvrxlite.base.MVRxLiteViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException

class LibraryViewModel(
    private val collectionsRepository: CollectionsRepository,
    accountId: Int,
    initialState: UIState.LibraryScreenState
) : MVRxLiteViewModel<UIState.LibraryScreenState>(initialState) {

    private val compositeDisposable = CompositeDisposable()
    private val _message = SingleLiveEvent<String>()

    val message: LiveData<String>
        get() = _message

    init {
        if (accountId != -1) {
            getFavouriteMovies(accountId)
            getWatchlistedMovies(accountId)
        }
    }

    fun getFavouriteMovies(accountId: Int) {
        collectionsRepository.getCollectionFlowable(accountId, CollectionType.Favourite)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { favouriteMovies ->
                    setState { copy(favouriteMoviesResource = favouriteMovies) }
                },
                onError = { error -> handleError(error, "get-favourite-movies") }
            )
            .disposeWith(compositeDisposable)
    }

    fun getWatchlistedMovies(accountId: Int) {
        collectionsRepository.getCollectionFlowable(accountId, type = CollectionType.Watchlist)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { watchlistMovies ->
                    setState { copy(watchlistedMoviesResource = watchlistMovies) }
                },
                onError = { error -> handleError(error, "get-watchlist-movies") }
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

    fun forceRefreshCollection(accountId: Int, type: CollectionType) {
        collectionsRepository.forceRefreshCollection(accountId, type)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
