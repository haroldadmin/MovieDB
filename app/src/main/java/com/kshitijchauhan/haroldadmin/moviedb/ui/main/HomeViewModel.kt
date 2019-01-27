package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionType
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionsRepository
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MoviesRepository
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.MovieGridItem
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException

class HomeViewModel(private val collectionsRepository: CollectionsRepository,
                    private val moviesRepository: MoviesRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _popularMovies = MutableLiveData<List<Movie>>()
    private val _topRatedMovies = MutableLiveData<List<Movie>>()
    private val _message = MutableLiveData<String>()

    val popularMovies: LiveData<List<Movie>>
        get() = _popularMovies

    val topRatedMovies: LiveData<List<Movie>>
        get() = _topRatedMovies

    val message: LiveData<String>
        get() = _message

    fun getPopularMovies() {
        collectionsRepository.getCollection(type = CollectionType.Popular)
            .subscribeOn(Schedulers.io())
            .flatMapPublisher { collection ->
                Flowable.fromIterable(collection.contents)
            }
            .flatMapSingle { id ->
                moviesRepository.getMovieDetails(id)
            }
            .toList()
            .subscribe(
                { popularMovies ->
                    _popularMovies.postValue(popularMovies)
                },
                { error ->
                    handleError(error)
                }
            )
            .disposeWith(compositeDisposable)
    }

    fun getTopRatedMovies() {
        collectionsRepository.getCollection(type = CollectionType.TopRated)
            .subscribeOn(Schedulers.io())
            .flatMapPublisher { collection ->
                Flowable.fromIterable(collection.contents)
            }
            .flatMapSingle { id ->
                moviesRepository.getMovieDetails(id)
            }
            .toList()
            .subscribe(
                { topRatedMovies ->
                    _topRatedMovies.postValue(topRatedMovies)
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}