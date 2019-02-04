package com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.ActorsRepository
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.db.MovieDBDatabase
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.AccountState
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MovieTrailer
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MoviesRepository
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException

class MovieDetailsViewModel(
    private val isAuthenticated: Boolean,
    private val movieId: Int,
    private val moviesRepository: MoviesRepository,
    private val actorsRepository: ActorsRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _movie = MutableLiveData<Movie>()
    private val _cast = MutableLiveData<List<Actor>>()
    private val _accountStates = MutableLiveData<AccountState>()
    private val _trailerKey = MutableLiveData<String>()
    private val _message = SingleLiveEvent<String>()

    val movie: LiveData<Movie>
        get() = _movie

    val cast: LiveData<List<Actor>>
        get() = _cast

    val accountState: LiveData<AccountState>
        get() = _accountStates

    val trailerKey: LiveData<String>
        get() = _trailerKey

    val message: LiveData<String>
        get() = _message


    /**
     * Methods for Movie account states, trailer, and cast should be executed after the movie details have been fully
     * retrieved because all their database entities have a foreign key constraint on the movie ID. If we try to save
     * them to the database before the movie has been fully retrieved, we will run into an SQLite Foreign Key Constraint
     * error.
     */
    fun getAllMovieInfo() {
        this.getMovieDetails()
            .publish()
            .apply {
                switchMap { getMovieAccountStates() }
                    .subscribe(
                        { accountState -> _accountStates.postValue(accountState) },
                        { error -> handleError(error, "get-account-states") })
                    .disposeWith(compositeDisposable)
                switchMap { getMovieTrailer() }
                    .subscribe(
                        { trailer -> _trailerKey.postValue(trailer.youtubeVideoKey) },
                        { error -> handleError(error, "get-movie-trailer") }
                    )
                    .disposeWith(compositeDisposable)
                switchMap { getMovieCast() }
                    .subscribe(
                        { actorsList -> _cast.postValue(actorsList) },
                        { error -> handleError(error, "get-movie-cast") }
                    )
                    .disposeWith(compositeDisposable)
            }
            .connect()
            .disposeWith(compositeDisposable)
    }

    private fun getMovieDetails(): Flowable<Movie> {
        return moviesRepository.getMovieDetailsFlowable(movieId)
            .subscribeOn(Schedulers.io())
            .doOnNext { movie ->
                _movie.postValue(movie)
                if (!movie.isModelComplete) {
                    log("Movie model is incomplete, force refreshing")
                    forceRefreshMovieDetails()
                }
            }
    }

    private fun getMovieAccountStates(): Flowable<AccountState> {
        return if (isAuthenticated) {
            moviesRepository.getAccountStatesForMovieFlowable(movieId)
        } else {
            Flowable.just(AccountState(null, null, movieId))
                .doOnNext { _accountStates.postValue(it) }
        }
            .subscribeOn(Schedulers.io())
    }

    fun toggleMovieFavouriteStatus(accountId: Int) {
        if (isAuthenticated) {
            moviesRepository
                .toggleMovieFavouriteStatus(movieId, accountId)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    // OnNext
                    { log("Movie status updated successfully") },
                    // onError
                    { error -> handleError(error, "toggle-favourite") }
                )
                .disposeWith(compositeDisposable)
        } else {
            throw IllegalStateException("Can't toggle favourite status if user is not logged in")
        }
    }

    fun toggleMovieWatchlistStatus(accountId: Int) {
        if (isAuthenticated) {
            moviesRepository
                .toggleMovieWatchlistStatus(movieId, accountId)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    // OnNext
                    { log("Movie status updated successfully") },
                    // onError
                    { error -> handleError(error, "toggle-watchlist") }
                )
                .disposeWith(compositeDisposable)
        } else {
            throw IllegalStateException("Can't toggle watchlist status if user is not logged in")
        }
    }

    private fun getMovieCast(): Flowable<List<Actor>> {
        return moviesRepository.getMovieCast(movieId)
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .map { cast -> cast.castMembersIds.take(8) }
            .flatMapSingle { ids ->
                actorsRepository.getAllActors(ids)
            }
    }

    private fun getMovieTrailer(): Flowable<MovieTrailer> {
        return moviesRepository.getMovieTrailer(movieId)
            .subscribeOn(Schedulers.io())
            .toFlowable()
    }

    private fun forceRefreshMovieDetails() {
        moviesRepository.forceRefreshMovieDetails(movieId)
            .subscribeOn(Schedulers.io())
            .subscribe(
                // onSuccess
                { movie ->
                    log("Successfully retrieved complete movie model")
                },
                // onError
                { error ->
                    handleError(error, "force-refresh-movie-details")
                }
            )
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun handleError(error: Throwable, caller: String) {
        error.localizedMessage?.let {
            log("ERROR $caller -> $it")
        } ?: log("ERROR $caller ->")
            .also {
                error.printStackTrace()
            }
        when (error) {
            is IOException -> _message.postValue("Please check your internet connection")
            is TimeoutException -> _message.postValue("Request timed out")
            else -> _message.postValue("An error occurred")
        }
    }
}
