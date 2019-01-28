package com.kshitijchauhan.haroldadmin.moviedb.repository.movies

import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MoviesRepository(
    private val localMoviesSource: LocalMoviesSource,
    private val remoteMoviesSource: RemoteMoviesSource
) {

    fun getMovieDetailsFlowable(id: Int): Flowable<Movie> {
        return localMoviesSource.isMovieInDatabase(id)
            .flatMapPublisher<Movie> { count: Int ->
                if (count > 0) {
                    log("Movie already exists in database")
                    localMoviesSource.getMovieFlowable(id)
                        .doOnComplete { log("debug: Completed emitting movie details from db") }
                } else {
                    log("Fetching movie from the network")
                    remoteMoviesSource.getMovieDetails(id)
                        .doOnSuccess { movie ->
                            log("Saving movie to the database")
                            localMoviesSource.saveMovieToDatabase(movie)
                        }
                        .flatMapPublisher {
                            localMoviesSource.getMovieFlowable(id)
                        }
                }
            }
    }

    fun getMovieDetails(id: Int): Single<Movie> {
        return localMoviesSource.isMovieInDatabase(id)
            .flatMap { count ->
                if (count > 0) {
                    log("Movie already exists in database")
                    localMoviesSource.getMovie(id)
                } else {
                    log("Fetching movie from the network")
                    remoteMoviesSource.getMovieDetails(id)
                        .doOnSuccess { movie ->
                            log("Saving movie to database")
                            localMoviesSource.saveMovieToDatabase(movie)
                        }
                        .flatMap {
                            localMoviesSource.getMovie(id)
                        }
                }
            }
    }

    fun getAccountStatesForMovieFlowable(movieId: Int): Flowable<AccountState> {
        return localMoviesSource.isAccountStateInDatabase(movieId)
            .flatMapPublisher<AccountState> { count ->
                if (count > 0) {
                    log("Account states are already in database")
                    localMoviesSource.getAccountStateForMovieFlowable(movieId)
                } else {
                    log("Fetching account states from the network")
                    remoteMoviesSource.getMovieAccountStates(movieId)
                        .doOnSuccess { accountState ->
                            log("Saving account state to database")
                            localMoviesSource.saveAccountStateToDatabase(accountState)
                        }
                        .flatMapPublisher {
                            localMoviesSource.getAccountStateForMovieFlowable(movieId)
                        }
                }
            }
    }

    fun getAccountStatesForMovie(movieId: Int): Single<AccountState> {
        return localMoviesSource.isAccountStateInDatabase(movieId)
            .flatMap { count ->
                if (count > 0){
                    log("Account states are already in database")
                    localMoviesSource.getAccountStatesForMovie(movieId)
                } else {
                    log("Fetching account states from the network")
                    remoteMoviesSource.getMovieAccountStates(movieId)
                        .doOnSuccess { accountState ->
                            log("Saving account state to database")
                            localMoviesSource.saveAccountStateToDatabase(accountState)
                        }
                        .flatMap {
                            localMoviesSource.getAccountStatesForMovie(movieId)
                        }
                }
            }
    }

    fun getMovieCastFlowable(movieId: Int): Flowable<Cast> {
        return localMoviesSource.isCastInDatabase(movieId)
            .flatMapPublisher { count ->
                if (count > 0) {
                    log("Cast already exists in database")
                    localMoviesSource.getCastForMovieFlowable(movieId)
                } else {
                    log("Fetching cast from network")
                        remoteMoviesSource.getMovieCast(movieId)
                            .doOnSuccess { cast ->
                                log("Saving cast to database")
                                localMoviesSource.saveCastToDatabase(cast)
                            }
                            .flatMapPublisher {
                                localMoviesSource.getCastForMovieFlowable(movieId)
                            }
                }
            }
    }

    fun getMovieCast(movieId: Int): Single<Cast> {
        return localMoviesSource.isCastInDatabase(movieId)
            .flatMap { count ->
                if (count > 0) {
                    log("Cast already exists in database")
                    localMoviesSource.getCastForMovie(movieId)
                } else {
                    log("Fetching cast from network")
                    remoteMoviesSource.getMovieCast(movieId)
                        .doOnSuccess { cast ->
                            log("Saving cast to database")
                            localMoviesSource.saveCastToDatabase(cast)
                        }
                        .flatMap {
                            localMoviesSource.getCastForMovie(movieId)
                        }
                }
            }
    }

    fun getMovieTrailer(movieId: Int): Single<MovieTrailer> {
        return localMoviesSource.isMovieTrailerInDatabase(movieId)
            .flatMap { count ->
                if (count > 0) {
                    log("Trailer already exists in database")
                    localMoviesSource.getMovieTrailer(movieId)
                } else {
                    log("Fetching trailer from network")
                    remoteMoviesSource.getMovieTrailer(movieId)
                        .doOnSuccess {  trailer ->
                            log("Saving trailer to database")
                            localMoviesSource.saveMovieTrailerToDatabase(trailer)
                        }
                        .flatMap {
                            localMoviesSource.getMovieTrailer(movieId)
                        }
                }
            }
    }

    fun toggleMovieFavouriteStatus(movieId: Int, accountId: Int): Single<AccountState> {
        return localMoviesSource.getAccountStatesForMovie(movieId)
            .flatMap { accountState ->
                val newStatus = !accountState.isFavourited
                remoteMoviesSource.toggleMovieFavouriteStatus(newStatus, movieId, accountId)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .flatMap {
                localMoviesSource.getAccountStatesForMovie(movieId)
            }
            .doOnSuccess { accountStates ->
                val newStatus = !accountStates.isFavourited
                localMoviesSource.updateAccountStatesInDatabase(accountStates.copy(isFavourited = newStatus))
            }
    }

    fun toggleMovieWatchlistStatus(movieId: Int, accountId: Int): Single<AccountState> {
        return localMoviesSource.getAccountStatesForMovie(movieId)
            .flatMap { accountState ->
                val newStatus = !accountState.isWatchlisted
                remoteMoviesSource.toggleMovieWatchlistStatus(newStatus, movieId, accountId)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .flatMap {
                localMoviesSource.getAccountStatesForMovie(movieId)
            }
            .doOnSuccess { accountStates ->
                val newStatus = !accountStates.isWatchlisted
                localMoviesSource.updateAccountStatesInDatabase(accountStates.copy(isWatchlisted = newStatus))
            }
    }

}