package com.kshitijchauhan.haroldadmin.moviedb.ui.discover

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.common.GeneralMovieResponse
import com.kshitijchauhan.haroldadmin.moviedb.utils.RxDiffUtil
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DiscoverViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var apiManager: ApiManager

    private val _moviesUpdate = MutableLiveData<Pair<List<GeneralMovieResponse>, DiffUtil.DiffResult>>()
    private val compositeDisposable = CompositeDisposable()

    val moviesUpdate: LiveData<Pair<List<GeneralMovieResponse>, DiffUtil.DiffResult>>
        get() = _moviesUpdate

    init {
        (application as MovieDBApplication)
            .appComponent
            .inject(this)
    }

    fun getPopularMovies() {
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
                movie.posterPath = "${Config.BASE_IMAGE_URL}${Config.DEFAULT_POSTER_SIZE}${movie.posterPath}"
                movie.voteAverage = movie.voteAverage.div(10.0).times(5)
                movie.releaseDate = movie.releaseDate.split("-")[0]
                movie
            }
            .toList()
            .toObservable()
            .compose(RxDiffUtil.calculateDiff { oldList, newList ->
                MoviesDiffUtil(oldList, newList)
            })
            .doOnNext {
                _moviesUpdate.postValue(it)
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}