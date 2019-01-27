package com.kshitijchauhan.haroldadmin.moviedb.repository.movies

import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.movie.MovieService
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getBackdropUrl
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getPosterUrl
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getProfilePictureUrl
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.toMovie
import io.reactivex.Single

class RemoteMoviesSource(
    private val movieService: MovieService,
    private val accountService: AccountService
) {

    fun getMovieDetails(id: Int): Single<Movie> {
        return movieService.getMovieDetails(id)
            .map { movieResponse ->
                movieResponse.toMovie()
            }
    }

    fun getMovieAccountStates(movieId: Int): Single<AccountState> {
        return movieService.getAccountStatesForMovie(movieId)
            .map { movieStatesResponse ->
                AccountState(
                    id = movieId,
                    isWatchlisted = movieStatesResponse.isWatchlisted ?: false,
                    isFavourited = movieStatesResponse.isFavourited ?: false,
                    movieId = movieId
                )
            }
    }

    fun getMovieCast(movieId: Int): Single<Cast> {
        return movieService.getCreditsForMovie(movieId)
            .map { creditsResponse ->
                Cast(
                    id = movieId,
                    castMembersIds = creditsResponse.cast.map { castMember -> castMember.id },
                    castMembers = creditsResponse.cast.map { castMember ->
                        Actor(
                            castMember.id,
                            castMember.profilePath.getProfilePictureUrl(),
                            castMember.name
                        )
                    },
                    movieId = movieId
                )
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
        }
    }
}