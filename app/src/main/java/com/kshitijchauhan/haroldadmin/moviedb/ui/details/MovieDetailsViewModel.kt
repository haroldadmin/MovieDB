package com.kshitijchauhan.haroldadmin.moviedb.ui.details

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.AddMediaToWatchlistRequest
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.MarkMediaAsFavoriteRequest
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie.Movie
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val _movieDetails = MutableLiveData<Movie>()
    private val _markAsFavoriteSuccess = SingleLiveEvent<Boolean>()
    private val _addToWatchlistSuccess = SingleLiveEvent<Boolean>()

    val movieDetails: LiveData<Movie>
        get() = _movieDetails

    val markAsFavoriteSuccess: LiveData<Boolean>
        get() = _markAsFavoriteSuccess

    val addToWatchlistSuccess: LiveData<Boolean>
        get() = _addToWatchlistSuccess


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
                movie.backdropPath = "${Config.BASE_IMAGE_URL}${Config.DEFAULT_BACKDROP_SIZE}${movie.backdropPath}"
                movie
            }
            .doOnSuccess { _movieDetails.postValue(it) }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    fun markMovieAsFavorite(accountId: Int, request: MarkMediaAsFavoriteRequest) {
        apiManager.markMediaAsFavorite(accountId, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                _markAsFavoriteSuccess.value = true
            }
            .doOnError {
                _markAsFavoriteSuccess.value = false
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    fun addMovieToWatchList(accountId: Int, request: AddMediaToWatchlistRequest) {
        apiManager.addMediaToWatchlist(accountId, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                _addToWatchlistSuccess.value = true
            }
            .doOnError {
                _addToWatchlistSuccess.value = false
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
