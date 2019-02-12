package com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.*
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException

class MovieDetailsViewModel(
    private val isAuthenticated: Boolean,
    private val movieId: Int,
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _movie = MutableLiveData<Resource<Movie>>()
    private val _actors = MutableLiveData<List<Resource<Actor>>>()
    private val _accountStates = MutableLiveData<Resource<AccountState>>()
    private val _trailerKey = MutableLiveData<Resource<MovieTrailer>>()
    private val _message = SingleLiveEvent<String>()

    val movie: LiveData<Resource<Movie>>
        get() = _movie

    val actors: LiveData<List<Resource<Actor>>>
        get() = _actors

    val accountState: LiveData<Resource<AccountState>>
        get() = _accountStates

    val trailerKey: LiveData<Resource<MovieTrailer>>
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
            .doOnNext { movieResource -> _movie.postValue(movieResource) }
            .publish()
            .apply {
                switchMap { getMovieAccountStates() }
                    .subscribe(
                        { accountState -> _accountStates.postValue(accountState) },
                        { error -> handleError(error, "get-account-states") })
                    .disposeWith(compositeDisposable)
                switchMap { getMovieTrailer() }
                    .subscribe(
                        { trailer -> _trailerKey.postValue(trailer) },
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
                        { actorsList -> _actors.postValue(actorsList) },
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
//            .subscribeBy(
//                onNext = { actors -> _actors.postValue(actors) },
//                onError = { error -> handleError(error, "get-movie-actors") }
//            )
//            .disposeWith(compositeDisposable)
    }

//            .subscribeOn(Schedulers.io())
//            .flatMapPublisher { cast ->
//                actorsRepository.getAllActorsResource(cast, compositeDisposable).toFlowable(BackpressureStrategy.BUFFER)
//            }
//            .subscribeBy (
//                onNext = { actorResource ->
//                    log("Cast list resource: $actorResource")
//                    _actors.postValue(actorResource)
//                },
//                onError = { error -> handleError(error, "get-actor-details") }
//            )
//            .disposeWith(compositeDisposable)
//
//        moviesRepository.getMovieActors(movieId)
//            .subscribeOn(Schedulers.io())
//            .flatMapPublisher { cast ->
//                Flowable.fromIterable(cast.castMembersIds)
//            }
//            .flatMapSingle { id ->
//                actorsRepository.getActorResource(id)
//            }
//        return moviesRepository.getMovieActors(movieId)
//            .subscribeOn(Schedulers.io())
//            .toFlowable()
//            .map { cast -> cast.castMembersIds.take(8) }
//            .flatMapSingle { ids ->
//                actorsRepository.getAllActorsResource(ids)
//            }
//            .map { list ->
//                list.filter { it.data != null }
//                    .map { resource -> resource.data!! }
//            }

    private fun getMovieTrailer(): Observable<Resource<MovieTrailer>> {
        return moviesRepository.getMovieTrailer(movieId)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
//            .subscribeBy(
//                onNext = { trailerResource -> _trailerKey.postValue(trailerResource) },
//                onError = { error -> handleError(error, "get-movie-trailer") }
//            )
//            .disposeWith(compositeDisposable)
//        return moviesRepository.getMovieTrailer(movieId)
//            .subscribeOn(Schedulers.io())
//            .toFlowable()
    }

    private fun forceRefreshMovieDetails(): Observable<Resource<Movie>> {
        return moviesRepository.forceRefreshMovieDetails(movieId)
            .init(compositeDisposable)
            .subscribeOn(Schedulers.io())
//            .subscribeBy(
//                onNext = { movieResource -> _movie.postValue(movieResource) },
//                onError = { error -> handleError(error, "force-refresh-movie") }
//            )
//            .disposeWith(compositeDisposable)
//        moviesRepository.forceRefreshMovieDetails(movieId)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                // onSuccess
//                { movie ->
//                    log("Successfully retrieved complete movie model")
//                },
//                // onError
//                { error ->
//                    handleError(error, "force-refresh-movie-details")
//                }
//            )
//            .disposeWith(compositeDisposable)
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
