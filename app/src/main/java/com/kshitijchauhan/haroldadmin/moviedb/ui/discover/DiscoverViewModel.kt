package com.kshitijchauhan.haroldadmin.moviedb.ui.discover

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.model.Movie
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.utils.disposeWith
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DiscoverViewModel(application: Application): AndroidViewModel(application) {

    @Inject
    lateinit var apiManager: ApiManager

    private val _moviesList = MutableLiveData<List<Movie>>()
    private val compositeDisposable = CompositeDisposable()

    val moviesList: LiveData<List<Movie>>
        get() = _moviesList

    init {
        (application as MovieDBApplication)
            .appComponent
            .inject(this)
    }

    fun getPopularMovies() =
            apiManager
                .getPopularMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnSuccess { response ->
                    _moviesList.postValue(response.results)
                }
                .subscribe()
                .disposeWith(compositeDisposable)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}