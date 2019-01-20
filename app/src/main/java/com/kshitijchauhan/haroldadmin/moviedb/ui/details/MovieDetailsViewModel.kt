package com.kshitijchauhan.haroldadmin.moviedb.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.AddMediaToWatchlistRequest
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.MarkMediaAsFavoriteRequest
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.MovieState
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val _movieDetails = MutableLiveData<Movie>()
    private val _accountStatesOfMovie = MutableLiveData<MovieState>()
    private val _trailerUrl = MutableLiveData<String>()

    val movieDetails: LiveData<Movie>
        get() = _movieDetails

    val accountStatesOfMovie: LiveData<MovieState>
        get() = _accountStatesOfMovie

    val trailerUrl: LiveData<String>
        get() = _trailerUrl

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
            .doOnSuccess {
                _movieDetails.postValue(it)
            }
            .doAfterSuccess {
                getVideosForMovie(movieId)
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    fun getAccountStatesForMovie(movieId: Int) {
        apiManager.getAccountStatesForMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response ->
                MovieState(response.isWatchlisted, response.isFavourited)
            }
            .doOnSuccess{ state ->
                _accountStatesOfMovie.value = state
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    fun toggleMovieFavouriteStatus(accountId: Int, request: MarkMediaAsFavoriteRequest) {
        apiManager.toggleMediaFavouriteStatus(accountId, request)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMap {
                apiManager.getAccountStatesForMovie(request.mediaId)
            }
            .map { response ->
                MovieState(response.isWatchlisted, response.isFavourited)
            }
            .doOnSuccess { state ->
                _accountStatesOfMovie.postValue(state)
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    fun toggleMovieWatchlistStatus(accountId: Int, request: AddMediaToWatchlistRequest) {
        apiManager.toggleMediaWatchlistStatus(accountId, request)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMap {
                apiManager.getAccountStatesForMovie(request.mediaId)
            }
            .map { response ->
                MovieState(response.isWatchlisted, response.isFavourited)
            }
            .doOnSuccess { state ->
                _accountStatesOfMovie.postValue(state)
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    fun getVideosForMovie(movieId: Int) {
        apiManager.getVideosForMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMapObservable { response ->
                Observable.fromIterable(response.results)
            }
            .filter { movieVideo ->
                movieVideo.site == "YouTube" && movieVideo.type == "Trailer"
            }
            .map { movieVideo ->
                movieVideo.key
            }
            .firstElement()
            .doOnSuccess { url ->
                _trailerUrl.postValue(url)
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
