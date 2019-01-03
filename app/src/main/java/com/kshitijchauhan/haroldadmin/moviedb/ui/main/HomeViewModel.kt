package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.model.MovieGridItem
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.model.MovieTypes
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val isPopularMoviesLoading = MutableLiveData<Boolean>()
    private val isTopRatedMoviesLoading = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()
    private val _popularMoviesUpdate = MutableLiveData<List<MovieGridItem>>()
    private val _topRatedMoviesUpdate = MutableLiveData<List<MovieGridItem>>()

    @Inject
    lateinit var apiManager: ApiManager

    val popularMoviesUpdate: LiveData<List<MovieGridItem>>
        get() = _popularMoviesUpdate

    val topRatedMoviesUpdate: LiveData<List<MovieGridItem>>
        get() = _topRatedMoviesUpdate

    init {
        (application as MovieDBApplication)
            .appComponent
            .inject(this)
    }

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
                    MovieGridItem(id, title, getPosterUrl(posterPath), MovieTypes.POPULAR)
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
                    MovieGridItem(id, title, getPosterUrl(posterPath), MovieTypes.TOP_RATED)
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

    private fun getPosterUrl(posterPath: String?): String {
        return posterPath?.let {
            "${Config.BASE_IMAGE_URL}${Config.SMALL_POSTER_SIZE}$it"
        } ?: ""
    }
}