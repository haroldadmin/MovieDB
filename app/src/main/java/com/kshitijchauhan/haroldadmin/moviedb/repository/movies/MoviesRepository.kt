package com.kshitijchauhan.haroldadmin.moviedb.repository.movies

import com.kshitijchauhan.haroldadmin.moviedb.repository.NetworkBoundResource
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers

class MoviesRepository(
    private val localMoviesSource: LocalMoviesSource,
    private val remoteMoviesSource: RemoteMoviesSource
) {

    fun getMovieDetailsFlowable(id: Int): NetworkBoundResource<Movie> {

        return object : NetworkBoundResource<Movie>() {
            override fun fetchFromNetwork(): Flowable<Resource<Movie>> {
                return remoteMoviesSource.getMovieDetails(id).toFlowable()
            }

            override fun fetchFromDatabase(): Flowable<Resource<Movie>> {
                return localMoviesSource.getMovieFlowable(id).map { movie -> Resource.Success(movie) }
            }

            override fun shouldRefresh(): Single<Boolean> {
                val isInDb = localMoviesSource.isMovieInDatabase(id).map { count -> count == 0 }
                val isModelComplete = localMoviesSource.getMovie(id).map { movie -> movie.isModelComplete }

                return isInDb
                    .zipWith(isModelComplete) {
                        dbStatus, modelStatus -> !(dbStatus && modelStatus)
                    }
            }

            override fun saveToDatabase(movie: Movie) {
                localMoviesSource.saveMovieToDatabase(movie)
            }
        }
    }

    fun getAccountStatesForMovieResource(movieId: Int): NetworkBoundResource<AccountState> {

        return object : NetworkBoundResource<AccountState>() {
            override fun fetchFromNetwork(): Flowable<Resource<AccountState>> {
                return remoteMoviesSource.getMovieAccountStates(movieId).toFlowable()
            }

            override fun fetchFromDatabase(): Flowable<Resource<AccountState>> {
                return localMoviesSource.getAccountStateForMovieFlowable(movieId)
                    .map { accountState -> Resource.Success(accountState) }
            }

            override fun shouldRefresh(): Single<Boolean> {
                return localMoviesSource.isAccountStateInDatabase(movieId).map { count -> count == 0 }
            }

            override fun saveToDatabase(accountState: AccountState) {
                localMoviesSource.saveAccountStateToDatabase(accountState)
            }
        }
    }

    fun getMovieCast(movieId: Int): NetworkBoundResource<Cast> {

        return object : NetworkBoundResource<Cast>() {
            override fun fetchFromNetwork(): Flowable<Resource<Cast>> {
                return remoteMoviesSource.getMovieCast(movieId).toFlowable()
            }

            override fun fetchFromDatabase(): Flowable<Resource<Cast>> {
                return localMoviesSource
                    .getCastForMovieFlowable(movieId)
                    .map { cast ->
                        Resource.Success(cast)
                    }
            }

            override fun shouldRefresh(): Single<Boolean> {
                return localMoviesSource
                    .isCastInDatabase(movieId)
                    .map { count -> count == 0 }
            }

            override fun saveToDatabase(cast: Cast) {
                localMoviesSource.saveCastToDatabase(cast)
            }
        }
    }

    fun getMovieTrailer(movieId: Int): NetworkBoundResource<MovieTrailer> {

        return object : NetworkBoundResource<MovieTrailer>() {
            override fun fetchFromNetwork(): Flowable<Resource<MovieTrailer>> {
                return remoteMoviesSource.getMovieTrailer(movieId)
            }

            override fun fetchFromDatabase(): Flowable<Resource<MovieTrailer>> {
                return localMoviesSource.getMovieTrailerFlowable(movieId)
                    .map { trailer -> Resource.Success(trailer) }
            }

            override fun shouldRefresh(): Single<Boolean> {
                return localMoviesSource.isMovieTrailerInDatabase(movieId).map { count -> count == 0 }
            }

            override fun saveToDatabase(movieTrailer: MovieTrailer) {
                localMoviesSource.saveMovieTrailerToDatabase(movieTrailer)
            }
        }
    }

    fun toggleMovieFavouriteStatus(movieId: Int, accountId: Int): Single<AccountState> {

        return localMoviesSource.getAccountStatesForMovie(movieId)
            .flatMap { accountState ->
                val newStatus = !accountState.isFavourited!!
                remoteMoviesSource.toggleMovieFavouriteStatus(newStatus, movieId, accountId)
            }
            .flatMap {
                localMoviesSource.getAccountStatesForMovie(movieId)
            }
            .observeOn(Schedulers.single())
            .doOnSuccess { accountStates ->
                val newStatus = !accountStates.isFavourited!!
                localMoviesSource.updateAccountStatesInDatabase(accountStates.copy(isFavourited = newStatus))
            }
    }

    fun toggleMovieWatchlistStatus(movieId: Int, accountId: Int): Single<AccountState> {

        return localMoviesSource.getAccountStatesForMovie(movieId)
            .flatMap { accountState ->
                val newStatus = !accountState.isWatchlisted!!
                remoteMoviesSource.toggleMovieWatchlistStatus(newStatus, movieId, accountId)
            }
            .flatMap {
                localMoviesSource.getAccountStatesForMovie(movieId)
            }
            .observeOn(Schedulers.single())
            .doOnSuccess { accountStates ->
                val newStatus = !accountStates.isWatchlisted!!
                localMoviesSource.updateAccountStatesInDatabase(accountStates.copy(isWatchlisted = newStatus))
            }
    }

    fun forceRefreshMovieDetails(id: Int): NetworkBoundResource<Movie> {
        return object : NetworkBoundResource<Movie>() {
            override fun fetchFromNetwork(): Flowable<Resource<Movie>> {
                return remoteMoviesSource.getMovieDetails(id).toFlowable()
            }

            override fun fetchFromDatabase(): Flowable<Resource<Movie>> {
                return localMoviesSource.getMovieFlowable(id).map { movie -> Resource.Success(movie) }
            }

            override fun shouldRefresh(): Single<Boolean> {
                return Single.just(true)
            }

            override fun saveToDatabase(movie: Movie) {
                localMoviesSource.saveMovieToDatabase(movie)
            }
        }
    }

    fun getSearchResultsForQuery(query: String): Single<Resource<List<Movie>>> {
        return remoteMoviesSource.getSearchResultsForQuery(query)
    }

    fun getActorsInMovie(ids: List<Int>): Single<List<Resource<Actor>>> {
        return localMoviesSource.getActorsForMovie(ids)
            .map { actors -> actors.map { Resource.Success(it) } }
    }
}