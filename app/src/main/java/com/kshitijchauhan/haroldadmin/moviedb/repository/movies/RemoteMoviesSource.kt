package com.kshitijchauhan.haroldadmin.moviedb.repository.movies

import com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.model.Movie
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.movie.*
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.firstOrDefault
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getBackdropUrl
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getPosterUrl
import io.reactivex.Flowable
import io.reactivex.Single

class RemoteMoviesSource(
    private val movieService: MovieService,
    private val accountService: AccountService
) {

    fun getMovieDetails(id: Int, isAuthenticated: Boolean): Flowable<Movie> {
        lateinit var movieResponse: MovieResponse
        lateinit var creditsResponse: MovieCreditsResponse
        lateinit var statesResponse: MovieStatesResponse
        lateinit var videosResponse: MovieVideosResponse

        return movieService.getMovieDetails(id)
            .doOnSuccess {
                movieResponse = it
            }
            .flatMap {
                movieService.getCreditsForMovie(id)
            }
            .doOnSuccess {
                creditsResponse = it
            }
            .flatMap {
                movieService.getVideosForMovie(id)
            }
            .doOnSuccess {
                videosResponse = it
            }
            .flatMap {
                if (isAuthenticated) {
                    movieService.getAccountStatesForMovie(id)
                } else {
                    Single.just(MovieStatesResponse(isFavourited = null, isWatchlisted = null))
                }
            }
            .doOnSuccess {
                statesResponse = it
            }
            .flatMapPublisher {
                Flowable.just(mapResponsesToMovieModel(movieResponse, statesResponse, videosResponse, creditsResponse))
            }
    }

    fun toggleMovieFavouriteStatus(isFavourite: Boolean, movieId: Int, accountId: Int): Single<ToggleFavouriteResponse> {
        return ToggleMediaFavouriteStatusRequest(
            MediaTypes.MOVIE.mediaName,
            movieId,
            isFavourite
        ).let { request ->
            accountService.toggleMediaFavouriteStatus(accountId, request)
        }
    }

    fun toggleMovieWatchlistStatus(isWatchlisted: Boolean, movieId: Int, accountId: Int): Single<ToggleWatchlistResponse> {
        return ToggleMediaWatchlistStatusRequest(
            MediaTypes.MOVIE.mediaName,
            movieId,
            isWatchlisted
        ).let { request ->
            accountService.toggleMediaWatchlistStatus(accountId, request)
        }
    }

    private fun mapResponsesToMovieModel(
        movieResponse: MovieResponse,
        accountStatesResponse: MovieStatesResponse,
        videosResponse: MovieVideosResponse,
        creditsResponse: MovieCreditsResponse
    ): Movie {
        return Movie(
            movieResponse.id,
            movieResponse.title,
            movieResponse.posterPath.getPosterUrl(),
            movieResponse.backdropPath.getBackdropUrl(),
            movieResponse.overview ?: "N/A",
            movieResponse.voteAverage,
            movieResponse.releaseDate,
            movieResponse.genres.first().name,
            accountStatesResponse.isWatchlisted,
            accountStatesResponse.isFavourited,
            videosResponse.results
                .filter { movieVideo ->
                    movieVideo.site == "YouTube" && movieVideo.type == "Trailer"
                }
                .map { movieVideo ->
                    movieVideo.key
                }
                .firstOrDefault(""),
            creditsResponse.cast.take(8).map { it.id }
        )
    }
}