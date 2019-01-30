package com.kshitijchauhan.haroldadmin.moviedb.repository.movies

import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.movie.MovieService
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.toActor
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.toMovie
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.toMovieTrailer
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RemoteMoviesSource(
    private val movieService: MovieService,
    private val accountService: AccountService
) {

    fun getMovieDetails(id: Int): Single<Movie> {
        return movieService.getMovieDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { movieResponse ->
                movieResponse.toMovie()
            }
    }

    fun getMovieAccountStates(movieId: Int): Single<AccountState> {
        return movieService.getAccountStatesForMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { movieStatesResponse ->
                AccountState(
                    isWatchlisted = movieStatesResponse.isWatchlisted ?: false,
                    isFavourited = movieStatesResponse.isFavourited ?: false,
                    movieId = movieId
                )
            }
    }

    fun getMovieCast(movieId: Int): Single<Cast> {
        return movieService.getCreditsForMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { creditsResponse ->
                Cast(
                    castMembersIds = creditsResponse.cast.map { castMember -> castMember.id },
                    movieId = movieId)
                    .apply {
                        castMembers = creditsResponse.cast.map { castMember -> castMember.toActor() }
                    }
            }
    }

    fun getMovieTrailer(movieId: Int): Single<MovieTrailer> {
        return movieService.getVideosForMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMapPublisher { movieVideosResponse ->
                Flowable.fromIterable(movieVideosResponse.results)
            }
            .filter { movieVideo ->
                movieVideo.site == "YouTube" && movieVideo.type == "Trailer"
            }
            .map { movieVideo -> movieVideo.toMovieTrailer(movieId) }
            .firstOrError()
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
}