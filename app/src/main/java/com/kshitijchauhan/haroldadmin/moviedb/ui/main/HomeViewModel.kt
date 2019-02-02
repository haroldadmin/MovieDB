package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionType
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionsRepository
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MoviesRepository
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException

class HomeViewModel(private val collectionsRepository: CollectionsRepository,
                    private val moviesRepository: MoviesRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var currentQuery: Disposable? = null
    private val _popularMovies = MutableLiveData<List<Movie>>()
    private val _topRatedMovies = MutableLiveData<List<Movie>>()
    private val _message = MutableLiveData<String>()
    private val _searchResults = MutableLiveData<List<Movie>>()

    val popularMovies: LiveData<List<Movie>>
        get() = _popularMovies

    val topRatedMovies: LiveData<List<Movie>>
        get() = _topRatedMovies

    val message: LiveData<String>
        get() = _message

    val searchResults: LiveData<List<Movie>>
        get() = _searchResults

    fun getPopularMovies() {
        collectionsRepository.getMoviesInCollectionFlowable(type = CollectionType.Popular)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { popularMovies ->
                    _popularMovies.postValue(popularMovies)
                },
                { error ->
                    handleError(error, "get-popular-movies")
                }
            )
            .disposeWith(compositeDisposable)
    }

    fun getTopRatedMovies() {
        collectionsRepository.getMoviesInCollection(type = CollectionType.TopRated)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { topRatedMovies ->
                    _topRatedMovies.postValue(topRatedMovies)
                },
                { error ->
                    handleError(error, "get-top-rated-movies")
                }
            )
            .disposeWith(compositeDisposable)
    }

    fun forceRefreshCollection(collectionType: CollectionType) {
        collectionsRepository.forceRefreshCollection(type = collectionType)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    log("Successfully refreshed collection of type: ${collectionType.name}")
                },
                { error ->
                    handleError(error, "force-refresh-collection")
                }
            )
            .disposeWith(compositeDisposable)
    }


    fun getSearchResultsForQuery(query: String) {

        if (query.length < 3) {
            _searchResults.postValue(null)
        } else {
            // Cancel the last query, we don't want it anymore
            currentQuery?.dispose()

            currentQuery = moviesRepository
                .getSearchResultsForQuery(query)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { searchResults ->
                        _searchResults.postValue(searchResults)
                    },
                    { error ->
                        handleError(error, "get-search-results")
                    }
                )
        }
        currentQuery?.disposeWith(compositeDisposable)
    }

    fun clearSearchResults() = _searchResults.postValue(null)

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