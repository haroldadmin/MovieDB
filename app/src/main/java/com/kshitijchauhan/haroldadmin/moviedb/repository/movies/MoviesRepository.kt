package com.kshitijchauhan.haroldadmin.moviedb.repository.movies

import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MoviesRepository(
    private val localMoviesSource: LocalMoviesSource,
    private val remoteMoviesSource: RemoteMoviesSource
) {

    fun getMovieDetailsFlowable(id: Int, isAuthenticated: Boolean): Flowable<Movie> {
        return localMoviesSource.isMovieInDatabase(id)
            .flatMapPublisher<Movie> { count: Int ->
                if (count > 0) {
                    log("Movie already exists in database")
                    localMoviesSource.getMovieSingle(id).toFlowable()
                        .doOnComplete { log("debug: Completed emitting movie details from db") }
                } else {
                    log("Fetching movie from the network")
                    remoteMoviesSource.getMovieDetails(id, isAuthenticated).toFlowable()
                        .doOnNext { movie ->
                            log("Saving movie to the database")
                            localMoviesSource.saveMovieToDatabase(movie)
                        }
                        .flatMapSingle {
                            localMoviesSource.getMovieSingle(id)
                        }
                }
            }
    }

    fun getMovieDetails(id: Int, isAuthenticated: Boolean = false): Single<Movie> {
        return localMoviesSource.isMovieInDatabase(id)
            .flatMap { count ->
                if (count > 0) {
                    log("Movie already exists in database")
                    localMoviesSource.getMovieSingle(id)
                } else {
                    log("Fetching movie from the network")
                    remoteMoviesSource.getMovieDetails(id, isAuthenticated)
                        .doOnSuccess { movie ->
                            log("Saving movie to database")
                            localMoviesSource.saveMovieToDatabase(movie)
                        }
                        .flatMap {
                            localMoviesSource.getMovieSingle(id)
                        }
                }
            }
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