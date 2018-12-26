package com.kshitijchauhan.haroldadmin.moviedb.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie.Movie
import com.kshitijchauhan.haroldadmin.moviedb.utils.disposeWith
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val _movieDetails = MutableLiveData<Movie>()

    val movieDetails: LiveData<Movie>
        get() = _movieDetails

    @Inject
    lateinit var apiManager: ApiManager

    init {
        (application as MovieDBApplication)
            .appComponent
            .inject(this)
    }

    fun getMovieDetails(movieId: Int) {
        apiManager.getMovieDetails(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { movie ->
                movie.posterPath = "${Config.BASE_IMAGE_URL}${Config.DEFAULT_POSTER_SIZE}${movie.posterPath}"
                movie.voteAverage = movie.voteAverage.div(10.0).times(5)
                movie.releaseDate = movie.releaseDate.split("-")[0]
                movie.backdropPath = "${Config.BASE_IMAGE_URL}${Config.DEFAULT_LOGO_SIZE}${movie.backdropPath}"
                movie
            }
            .doOnSuccess { _movieDetails.postValue(it) }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
