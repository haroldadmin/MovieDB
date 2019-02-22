package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionType
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionsRepository
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MoviesRepository
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import com.kshitijchauhan.haroldadmin.mvrxlite.base.MVRxLiteViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException

class HomeViewModel(
    private val collectionsRepository: CollectionsRepository,
    private val moviesRepository: MoviesRepository,
    initialState: UIState.HomeScreenState
) : MVRxLiteViewModel<UIState.HomeScreenState>(initialState) {

    private val compositeDisposable = CompositeDisposable()
    private var currentQuery: Disposable? = null
    private val _message = MutableLiveData<String>()

    val message: LiveData<String>
        get() = _message

    init {
        getPopularMovies()
        getTopRatedMovies()
    }

    fun getPopularMovies() {
        collectionsRepository.getCollectionFlowable(type = CollectionType.Popular)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { popularMovies ->
                    setState { copy(popularMoviesResource = popularMovies) }
                },
                onError = { error -> handleError(error, "get-popular-movies") }
            )
            .disposeWith(compositeDisposable)
    }

    fun getTopRatedMovies() {
        collectionsRepository.getCollectionFlowable(type = CollectionType.TopRated)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { topRatedMovies ->
                    setState { copy(topRatedMoviesResource = topRatedMovies) }
                },
                onError = { error -> handleError(error, "get-popular-movies") }
            )
            .disposeWith(compositeDisposable)
    }

    fun forceRefreshCollection(collectionType: CollectionType) {
        collectionsRepository.forceRefreshCollection(type = collectionType)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
    }


    fun getSearchResultsForQuery(query: String) {

        if (query.length < 3) {
            setState { copy(searchResultsResource = null) }
        } else {
            // Cancel the last query, we don't want it anymore
            currentQuery?.dispose()

            currentQuery = moviesRepository
                .getSearchResultsForQuery(query)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { searchResults ->
                        setState { copy(searchResultsResource = searchResults) }
                    },
                    { error ->
                        handleError(error, "get-search-results")
                    }
                )
        }
        currentQuery?.disposeWith(compositeDisposable)
    }

    fun clearSearchResults() = setState { copy(searchResultsResource = null) }

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