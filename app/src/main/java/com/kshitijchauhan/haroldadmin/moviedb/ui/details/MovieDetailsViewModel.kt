package com.kshitijchauhan.haroldadmin.moviedb.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Movie
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.movie.MoviesRepository
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.BaseViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.parameter.parametersOf
import org.koin.standalone.inject
import java.io.IOException
import java.util.concurrent.TimeoutException

class MovieDetailsViewModel(
    private val isAuthenticated: Boolean,
    private val movieId: Int
) : BaseViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _movie = MutableLiveData<Movie>()
    private val _message = SingleLiveEvent<String>()
    private val moviesRepository: MoviesRepository by inject {
        parametersOf(compositeDisposable)
    }

    val movie: LiveData<Movie>
        get() = _movie

    val message: LiveData<String>
        get() = _message

    fun getMovieDetails(isAuthenticated: Boolean) {
        moviesRepository.getMovieDetails(movieId, isAuthenticated)
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

    fun toggleMovieFavouriteStatus(accountId: Int) {
        if (isAuthenticated) {
            _movie.value?.isFavourited?.let {
                moviesRepository
                    .toggleMovieFavouriteStatus(!it, movieId, accountId)
                    .subscribe(
                        // OnNext
                        { log("Movie status updated successfully") },
                        // onError
                        { error -> handleError(error) }
                    )
                    .disposeWith(compositeDisposable)
            }
        } else throw IllegalStateException("Can't toggle favourite status if user is not logged in")
    }

    fun toggleMovieWatchlistStatus(accountId: Int) {
        if (isAuthenticated) {
            _movie.value?.isWatchlisted?.let {
                moviesRepository
                    .toggleMovieWatchlistStatus(!it, movieId, accountId)
                    .subscribe(
                        // OnNext
                        { log("Movie status updated successfully") },
                        // onError
                        { error -> handleError(error) }
                    )
                    .disposeWith(compositeDisposable)
            }
        } else throw IllegalStateException("Can't toggle watchlist status if user is not logged in")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun handleError(error: Throwable) {
        when (error) {
            is IOException -> _message.postValue("Please check your internet connection")
            is TimeoutException -> _message.postValue("Request timed out")
            else -> _message.postValue("An error occurred")
        }
    }
}
