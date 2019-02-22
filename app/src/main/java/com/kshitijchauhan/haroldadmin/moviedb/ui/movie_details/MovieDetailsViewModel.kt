package com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.*
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import com.kshitijchauhan.haroldadmin.mvrxlite.base.MVRxLiteViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException

class MovieDetailsViewModel(
    private val isAuthenticated: Boolean,
    private val movieId: Int,
    private val moviesRepository: MoviesRepository,
    initialState: UIState.DetailsScreenState
) : MVRxLiteViewModel<UIState.DetailsScreenState>(initialState) {

    private val compositeDisposable = CompositeDisposable()
    private val _message = SingleLiveEvent<String>()

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
            .doOnNext { movie ->
                setState { copy(movieResource = movie) }
            }
            .skip(1) // We skip the first value because it is Resource.Loading
            .publish()
            .apply {
                switchMap { getMovieAccountStates() }
                    .subscribe(
                        { accountState ->
                            setState { copy(accountStatesResource = accountState) }
                        },
                        { error -> handleError(error, "get-account-states") })
                    .disposeWith(compositeDisposable)
                switchMap { getMovieTrailer() }
                    .subscribe(
                        { trailer ->
                            setState { copy(trailerResource = trailer) }
                        },
                        { error -> handleError(error, "get-movie-trailer") }
                    )
                    .disposeWith(compositeDisposable)
                switchMap { getMovieCast() }
                    .switchMapSingle { castResource ->
                        if (castResource is Resource.Success) {
                            getMovieActors(castResource.data.castMembersIds)
                        } else {
                            getMovieActors(emptyList())
                        }
                    }
                    .subscribe(
                        { actorsList ->
                            setState { copy(castResource = actorsList) }
                        },
                        { error -> handleError(error, "get-movie-cast") }
                    )
                    .disposeWith(compositeDisposable)
            }
            .connect()
            .disposeWith(compositeDisposable)
    }

    private fun getMovieDetails(): Observable<Resource<Movie>> {
        return moviesRepository.getMovieDetailsFlowable(movieId)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
    }

    private fun getMovieAccountStates(): Observable<Resource<AccountState>> {
        return if (isAuthenticated) {
            moviesRepository.getAccountStatesForMovieResource(movieId)
                .init(compositeDisposable)
                .subscribeOn(Schedulers.io())
        } else {
            Observable.just(Resource.Success(AccountState(null, null, movieId)))
        }
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

    private fun getMovieCast(): Observable<Resource<Cast>> {
        return moviesRepository.getMovieCast(movieId)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
    }

    private fun getMovieActors(ids: List<Int>, count: Int = 8): Single<List<Resource<Actor>>> {
        return moviesRepository.getActorsInMovie(ids.take(count))
            .subscribeOn(Schedulers.io())
    }

    private fun getMovieTrailer(): Observable<Resource<MovieTrailer>> {
        return moviesRepository.getMovieTrailer(movieId)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
    }

    private fun forceRefreshMovieDetails(): Observable<Resource<Movie>> {
        return moviesRepository.forceRefreshMovieDetails(movieId)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
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
