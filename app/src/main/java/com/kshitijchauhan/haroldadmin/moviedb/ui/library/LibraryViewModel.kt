package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionType
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionsRepository
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException

class LibraryViewModel(
    private val collectionsRepository: CollectionsRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _favouriteMovies = MutableLiveData<List<Movie>>()
    private val _watchlistedMovies = MutableLiveData<List<Movie>>()
    private val _message = SingleLiveEvent<String>()

    val watchListMoviesUpdate: LiveData<List<Movie>>
        get() = _watchlistedMovies

    val favouriteMovies: LiveData<List<Movie>>
        get() = _favouriteMovies

    val message: LiveData<String>
        get() = _message


    fun getFavouriteMovies(accountId: Int) {
        collectionsRepository.getMoviesInCollectionFlowable(accountId, CollectionType.Favourite)
            .subscribeOn(Schedulers.io())
            .subscribe(
                // onSuccess
                { favouriteMovies ->
                    _favouriteMovies.postValue(favouriteMovies)
                },
                { error ->
                    handleError(error)
                }
            )
            .disposeWith(compositeDisposable)
    }

    fun getWatchlistedMovies(accountId: Int) {
        collectionsRepository.getMoviesInCollectionFlowable(accountId, CollectionType.Watchlist)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { watchlist ->
                    _watchlistedMovies.postValue(watchlist)
                },
                { error ->
                    handleError(error)
                }
            )
            .disposeWith(compositeDisposable)
    }

    private fun handleError(error: Throwable) {
        log(error.localizedMessage)
        when (error) {
            is IOException -> _message.postValue("Please check your internet connection")
            is TimeoutException -> _message.postValue("Request timed out")
            else -> _message.postValue("An error occurred")
        }
    }

    fun forceRefreshCollection(accountId: Int, type: CollectionType) {
        collectionsRepository.forceRefreshCollection(accountId, type)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { log("Successfully refresh collection of type: ${type.name}") },
                { error -> handleError(error) }
            )
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
