package com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionType
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionsRepository
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException

class InTheatresViewModel(private val collectionsRepository: CollectionsRepository) : ViewModel() {

    private val _inTheatreMovies = MutableLiveData<List<Movie>>()
    private val _message = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    val moviesUpdate: LiveData<List<Movie>>
        get() = _inTheatreMovies

    val message: LiveData<String>
        get() = _message

    fun getPopularMovies() {
        collectionsRepository.getMoviesInCollection(type = CollectionType.InTheatres)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { list ->
                    _inTheatreMovies.postValue(list)
                },
                { error ->
                    handleError(error)
                }
            )
            .disposeWith(compositeDisposable)
    }

    fun forceRefreshInTheatresCollection() {
        collectionsRepository.forceRefreshCollection(type = CollectionType.InTheatres)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { log("Successfully refreshed in theatres collection") },
                { error -> handleError(error) }
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}