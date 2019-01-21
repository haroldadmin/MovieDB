package com.kshitijchauhan.haroldadmin.moviedb.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.AddMediaToWatchlistRequest
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.MarkMediaAsFavoriteRequest
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie.CastMember
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.MovieState
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getBackdropUrl
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getPosterUrl
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsViewModel(private val apiManager: ApiManager,
                            private val isAuthenticated: Boolean,
                            private val movieId: Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _movieDetails = MutableLiveData<Movie>()
    private val _accountStatesOfMovie = MutableLiveData<MovieState>()
    private val _trailerUrl = MutableLiveData<String>()
    private val _movieCredits = MutableLiveData<List<CastMember>>()

    val movieDetails: LiveData<Movie>
        get() = _movieDetails

    val accountStatesOfMovie: LiveData<MovieState>
        get() = _accountStatesOfMovie

    val trailerUrl: LiveData<String>
        get() = _trailerUrl

    val movieCredits: LiveData<List<CastMember>>
        get() = _movieCredits

    init {
        getMovieDetails()
        getVideosForMovie()
        if (isAuthenticated) {
            getAccountStatesForMovie()
        }
        getCreditsForMovie()
    }

    fun getMovieDetails(movieId: Int = this.movieId) {
        apiManager.getMovieDetails(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { movie ->
                movie.posterPath = movie.posterPath.getPosterUrl()
                movie.voteAverage = movie.voteAverage.div(10.0).times(5)
                movie.releaseDate = movie.releaseDate.split("-")[0]
                movie.backdropPath = movie.backdropPath.getBackdropUrl()
                movie
            }
            .doOnSuccess {
                _movieDetails.postValue(it)
            }
            .doAfterSuccess {
                getVideosForMovie(movieId)
                if (isAuthenticated) {
                    getAccountStatesForMovie(movieId)
                }
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    fun getAccountStatesForMovie(movieId: Int = this.movieId) {
        apiManager.getAccountStatesForMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response ->
                MovieState(response.isWatchlisted, response.isFavourited)
            }
            .doOnSuccess { state ->
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

    fun getVideosForMovie(movieId: Int = this.movieId) {
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
            .first("")
            .doOnSuccess { url ->
                _trailerUrl.postValue(url)
            }
            .doOnError { err ->
                _trailerUrl.postValue("")
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    fun getCreditsForMovie(movieId: Int = this.movieId) {
        apiManager.getCreditsForMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMapObservable { response ->
                Observable.fromIterable(response.cast)
            }
            .take(8)
            .toList()
            .doOnSuccess {
                _movieCredits.postValue(it)
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
