package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.ui.MovieItemType
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.MovieGridItem
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getPosterUrl
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(private val apiManager: ApiManager) : ViewModel() {

    private val isPopularMoviesLoading = MutableLiveData<Boolean>()
    private val isTopRatedMoviesLoading = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()
    private val _popularMoviesUpdate = MutableLiveData<List<MovieGridItem>>()
    private val _topRatedMoviesUpdate = MutableLiveData<List<MovieGridItem>>()

    val popularMoviesUpdate: LiveData<List<MovieGridItem>>
        get() = _popularMoviesUpdate

    val topRatedMoviesUpdate: LiveData<List<MovieGridItem>>
        get() = _topRatedMoviesUpdate

    fun getPopularMovies() {
        isPopularMoviesLoading.value = true
        apiManager
            .getPopularMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { response ->
                response.results
            }
            .flatMapObservable { list ->
                Observable.fromIterable(list)
            }
            .map { movie ->
                with(movie) {
                    MovieGridItem(
                        id,
                        title,
                        this.posterPath.getPosterUrl(),
                        MovieItemType.MovieType.Popular
                    )
                }
            }
            .toList()
            .doOnSuccess {
                isPopularMoviesLoading.postValue(false)
                _popularMoviesUpdate.postValue(it)
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    fun getTopRatedMovies() {
        isTopRatedMoviesLoading.value = true
        apiManager
            .getTopRatedMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { response ->
                response.results
            }
            .flatMapObservable { list ->
                Observable.fromIterable(list)
            }
            .map { movie ->
                with (movie) {
                    MovieGridItem(
                        id,
                        title,
                        this.posterPath.getPosterUrl(),
                        MovieItemType.MovieType.TopRated
                    )
                }
            }
            .toList()
            .doOnSuccess {
                isTopRatedMoviesLoading.postValue(false)
                _topRatedMoviesUpdate.postValue(it)
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}