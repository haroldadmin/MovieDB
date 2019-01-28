package com.kshitijchauhan.haroldadmin.moviedb.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.ActorsRepository
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.AccountState
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MoviesRepository
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
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

    fun getMovieDetails() {
        moviesRepository.getMovieDetailsFlowable(movieId)
            .subscribeOn(Schedulers.io())
            .subscribe(
                // onNext
                { movie: Movie ->
                    _movie.postValue(movie)
                },
                // onError
                {
                    handleError(it)
                }
            )
            .disposeWith(compositeDisposable)
    }

    fun getMovieAccountStates() {
        if (isAuthenticated) {
            moviesRepository.getAccountStatesForMovieFlowable(movieId)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    // onNext
                    { accountState ->
                        _accountStates.postValue(accountState)
                    },
                    // onError
                    { error ->
                        handleError(error)
                    }
                )
                .disposeWith(compositeDisposable)
        } else {
            AccountState(null, null, movieId).let {
                _accountStates.postValue(it)
            }
        }
    }

    fun toggleMovieFavouriteStatus(accountId: Int) {
        if (isAuthenticated) {
            moviesRepository
                .toggleMovieFavouriteStatus(movieId, accountId)
                .subscribe(
                    // OnNext
                    { log("Movie status updated successfully") },
                    // onError
                    { error -> handleError(error) }
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
                .subscribe(
                    // OnNext
                    { log("Movie status updated successfully") },
                    // onError
                    { error -> handleError(error) }
                )
                .disposeWith(compositeDisposable)
        } else {
            throw IllegalStateException("Can't toggle watchlist status if user is not logged in")
        }
    }

    fun getMovieCast() {
        moviesRepository.getMovieCast(movieId)
            .flatMap { cast ->
                actorsRepository.getAllActors(cast.castMembersIds)
            }
            .subscribeOn(Schedulers.io())
            .subscribe(
                // onSuccess
                { actorsList ->
                    actorsList.take(8).let { _cast.postValue(it) }
                },
                // onError
                { error ->
                    handleError(error)
                }
            )
            .disposeWith(compositeDisposable)
    }

    fun getMovieTrailer() {
        moviesRepository.getMovieTrailer(movieId)
            .subscribeOn(Schedulers.io())
            .subscribe(
                // onSuccess
                { trailer ->
                    _trailerKey.postValue(trailer.youtubeVideoKey)
                },
                // onError
                { error ->
                    handleError(error)
                }
            )
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun handleError(error: Throwable) {
        log(error.localizedMessage)
        when (error) {
            is IOException -> _message.postValue("Please check your internet connection")
            is TimeoutException -> _message.postValue("Request timed out")
            else -> _message.postValue("An error occurred")
        }
    }
}
