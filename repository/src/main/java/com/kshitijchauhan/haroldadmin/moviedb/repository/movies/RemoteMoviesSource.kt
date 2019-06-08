package com.kshitijchauhan.haroldadmin.moviedb.repository.movies

import com.haroldadmin.cnradapter.NetworkResponse
import com.kshitijchauhan.haroldadmin.moviedb.core.extensions.firstOrDefault
import com.kshitijchauhan.haroldadmin.moviedb.core.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.movie.MovieService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.search.SearchService
import com.kshitijchauhan.haroldadmin.moviedb.repository.toAccountState
import com.kshitijchauhan.haroldadmin.moviedb.repository.toActor
import com.kshitijchauhan.haroldadmin.moviedb.repository.toMovie
import com.kshitijchauhan.haroldadmin.moviedb.repository.toMovieTrailer
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx2.asSingle

internal class RemoteMoviesSource (
    private val movieService: MovieService,
    private val accountService: AccountService,
    private val searchService: SearchService
) {

    fun getMovieDetails(id: Int): Single<Resource<Movie>> {
        return movieService.getMovieDetails(id)
            .asSingle(Dispatchers.Default)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMap { movieResponse ->
                Single.just(
                    when (movieResponse) {
                        is NetworkResponse.Success -> {
                            Resource.Success(movieResponse.body.toMovie())
                        }
                        is NetworkResponse.ServerError -> {
                            Resource.Error<Movie>(movieResponse.body?.statusMessage ?: "Server Error")
                        }
                        is NetworkResponse.NetworkError -> {
                            Resource.Error(movieResponse.error.localizedMessage ?: "Network Error")
                        }
                    }
                )
            }
    }

    fun getMovieAccountStates(movieId: Int): Single<Resource<AccountState>> {
        return movieService.getAccountStatesForMovie(movieId)
            .asSingle(Dispatchers.Default)
            .subscribeOn(Schedulers.io())
            .flatMap { accountStateResponse ->
                Single.just(
                    when (accountStateResponse) {
                        is NetworkResponse.Success -> {
                            Resource.Success(accountStateResponse.body.toAccountState(movieId))
                        }
                        is NetworkResponse.ServerError -> {
                            Resource.Error<AccountState>(accountStateResponse.body?.statusMessage ?: "Server Error")
                        }
                        is NetworkResponse.NetworkError -> {
                            Resource.Error(accountStateResponse.error.localizedMessage ?: "Network Error")
                        }
                    }
                )
            }
    }

    fun getMovieCast(movieId: Int): Single<Resource<Cast>> {
        return movieService.getCreditsForMovie(movieId)
            .asSingle(Dispatchers.Default)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMap { response ->
                Single.just(
                    when (response) {
                        is NetworkResponse.Success -> {
                            Resource.Success(
                                Cast(
                                    castMembersIds = response.body.cast.map { it.id },
                                    movieId = movieId
                                ).apply {
                                    castMembers = response.body.cast.map { it.toActor() }
                                }
                            )
                        }
                        is NetworkResponse.ServerError -> {
                            Resource.Error<Cast>(response.body?.statusMessage ?: "Server Error")
                        }
                        is NetworkResponse.NetworkError -> {
                            Resource.Error(response.error.localizedMessage ?: "Server Error")
                        }
                    }
                )
            }
    }

    fun getMovieTrailer(movieId: Int): Flowable<Resource<MovieTrailer>> {
        return movieService.getVideosForMovie(movieId)
            .asSingle(Dispatchers.Default)
            .subscribeOn(Schedulers.io())
            .flatMapPublisher { movieVideosResponse ->
                Flowable.just(when (movieVideosResponse) {
                    is NetworkResponse.Success -> {
                        val trailer = movieVideosResponse
                            .body
                            .results
                            .filter { it.site == "YouTube" && it.type == "Trailer" }
                            .map { it.toMovieTrailer(movieId) }
                            .firstOrDefault(MovieTrailer(movieId, ""))
                        Resource.Success(trailer)
                    }
                    is NetworkResponse.ServerError -> {
                        Resource.Error<MovieTrailer>(movieVideosResponse.body?.statusMessage ?: "Server Error")
                    }
                    is NetworkResponse.NetworkError -> {
                        Resource.Error(movieVideosResponse.error.localizedMessage ?: "Network Error")
                    }
                })
            }
    }

    fun toggleMovieFavouriteStatus(
        isFavourite: Boolean,
        movieId: Int,
        accountId: Int
    ): Single<ToggleFavouriteResponse> {
        return ToggleMediaFavouriteStatusRequest(
            MediaTypes.MOVIE.mediaName,
            movieId,
            isFavourite
        ).let { request ->
            accountService.toggleMediaFavouriteStatus(accountId, request)
                .subscribeOn(Schedulers.io())
        }
    }

    fun toggleMovieWatchlistStatus(
        isWatchlisted: Boolean,
        movieId: Int,
        accountId: Int
    ): Single<ToggleWatchlistResponse> {
        return ToggleMediaWatchlistStatusRequest(
            MediaTypes.MOVIE.mediaName,
            movieId,
            isWatchlisted
        ).let { request ->
            accountService.toggleMediaWatchlistStatus(accountId, request)
                .subscribeOn(Schedulers.io())
        }
    }

    fun getSearchResultsForQuery(query: String): Single<Resource<List<Movie>>> {
        return searchService
            .searchForMovie(query)
            .asSingle(Dispatchers.Default)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMap { searchResponse ->
                Single.just(
                    when (searchResponse) {
                        is NetworkResponse.Success -> {
                            Resource.Success(searchResponse.body.results.map { it.toMovie() })
                        }
                        is NetworkResponse.ServerError -> {
                            Resource.Error<List<Movie>>(searchResponse.body?.statusMessage ?: "Server Error")
                        }
                        is NetworkResponse.NetworkError -> {
                            Resource.Error(searchResponse.error.localizedMessage ?: "Network Error")
                        }
                    }
                )
            }
    }

    fun getSimilarMoviesForMovie(movieId: Int): Single<Resource<List<Movie>>> {
        return movieService
            .getSimilarMoviesForMovie(movieId)
            .asSingle(Dispatchers.Default)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMap { similarMoviesResponse ->
                Single.just(
                    when (similarMoviesResponse) {
                        is NetworkResponse.Success -> {
                            Resource.Success(similarMoviesResponse.body.results.map { it.toMovie() })
                        }
                        is NetworkResponse.ServerError -> {
                            Resource.Error<List<Movie>>(similarMoviesResponse.body?.statusMessage ?: "Server Error")
                        }
                        is NetworkResponse.NetworkError -> {
                            Resource.Error(similarMoviesResponse.error.localizedMessage ?: "Network Error")
                        }
                    }
                )
            }
    }
}