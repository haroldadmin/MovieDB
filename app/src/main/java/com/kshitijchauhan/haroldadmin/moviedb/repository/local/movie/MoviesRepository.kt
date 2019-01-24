package com.kshitijchauhan.haroldadmin.moviedb.repository.local.movie

import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Movie
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MoviesRepository(
    private val localMoviesSource: LocalMoviesSource,
    private val remoteMoviesSource: RemoteMoviesSource,
    private val compositeDisposable: CompositeDisposable
) {

    fun getMovieDetails(id: Int, isAuthenticated: Boolean): Flowable<Movie> {
        remoteMoviesSource.getMovieDetails(id, isAuthenticated)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .subscribe(
                // OnNext
                { movie: Movie ->
                    log("Successfully retrieved movie: $movie")
                    localMoviesSource.saveMovieToDatabase(movie)
                },
                // OnError
                {
                    log(it.localizedMessage)
                }
            )
            .disposeWith(compositeDisposable)
        return localMoviesSource.getMovie(id)
    }

    fun toggleMovieFavouriteStatus(isFavourite: Boolean, movieId: Int, accountId: Int): Single<Movie> {
        return remoteMoviesSource.toggleMovieFavouriteStatus(isFavourite, movieId, accountId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            /**
             * The call to get movie must be blocking, otherwise we will get stuck in an infinite loop
             * Room will try to keep on notifying us of changes to this database object, and therefore flatmap will
             * keep emitting items
             **/
            .flatMap { Single.just(localMoviesSource.getMovieBlocking(movieId)) }
            .map { movie ->
                movie.let {
                    localMoviesSource.updateMovieInDatabase(it.copy(isFavourited = isFavourite))
                    it
                }
            }
    }

    fun toggleMovieWatchlistStatus(isWatchlisted: Boolean, movieId: Int, accountId: Int): Single<Movie> {
        return remoteMoviesSource.toggleMovieWatchlistStatus(isWatchlisted, movieId, accountId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            /**
             * The call to get movie must be blocking, otherwise we will get stuck in an infinite loop
             * Room will try to keep on notifying us of changes to this database object, and therefore flatmap will
             * keep emitting items
             **/
            .flatMap { Single.just(localMoviesSource.getMovieBlocking(movieId)) }
            .map { movie ->
                movie.let {
                    localMoviesSource.updateMovieInDatabase(it.copy(isWatchlisted = isWatchlisted))
                    it
                }
            }
    }

}