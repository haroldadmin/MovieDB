package com.kshitijchauhan.haroldadmin.moviedb.ui

import android.view.View
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.AccountState
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MovieTrailer
import com.kshitijchauhan.haroldadmin.mvrxlite.base.MVRxLiteState

sealed class UIState: MVRxLiteState {

    data class HomeScreenState(
        val popularMoviesResource: Resource<List<Movie>>,
        val topRatedMoviesResource: Resource<List<Movie>>,
        val searchResultsResource: Resource<List<Movie>>?
    ): UIState()

    data class LibraryScreenState(
        val favouriteMoviesResource: Resource<List<Movie>>,
        val watchlistedMoviesResource: Resource<List<Movie>>,
        val isAuthenticated: Boolean
    ): UIState()

    data class InTheatresScreenState(
        val inTheatresMoviesResource: Resource<List<Movie>>
    ): UIState()

    data class DetailsScreenState(val movieId: Int = -1,
                                  val transitionName: String? = null,
                                  val sharedView: View? = null,
                                  val movieResource: Resource<Movie>,
                                  val accountStatesResource: Resource<AccountState>,
                                  val trailerResource: Resource<MovieTrailer>,
                                  val castResource: List<Resource<Actor>>,
                                  val similarMoviesResource: Resource<List<Movie>>): UIState()

    sealed class AccountScreenState: UIState() {
        object AuthenticatedScreenState: AccountScreenState()
        object UnauthenticatedScreenState: AccountScreenState()
    }

    data class ActorDetailsScreenState(val actorId: Int,
                                       val transitionName: String? = null,
                                       val sharedView: View? = null,
                                       val actorResource: Resource<Actor>): UIState()
}