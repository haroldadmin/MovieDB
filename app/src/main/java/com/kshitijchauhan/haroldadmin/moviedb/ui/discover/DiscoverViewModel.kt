package com.kshitijchauhan.haroldadmin.moviedb.ui.discover

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.model.Movie
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.utils.RxDiffUtil
import com.kshitijchauhan.haroldadmin.moviedb.utils.disposeWith
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DiscoverViewModel(application: Application): AndroidViewModel(application) {

    @Inject
    lateinit var apiManager: ApiManager

    private val _moviesUpdate = MutableLiveData<Pair<List<Movie>, DiffUtil.DiffResult>>()
    private val compositeDisposable = CompositeDisposable()

    val moviesUpdate: LiveData<Pair<List<Movie>, DiffUtil.DiffResult>>
        get() = _moviesUpdate

    init {
        (application as MovieDBApplication)
            .appComponent
            .inject(this)

        getPopularMovies()
    }

    private fun getPopularMovies() {
        apiManager
            .getPopularMovies()
            .map {
                response -> response.results
            }
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
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