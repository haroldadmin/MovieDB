package com.kshitijchauhan.haroldadmin.moviedb.ui

import android.view.View
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.AccountState
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MovieTrailer
import com.kshitijchauhan.haroldadmin.mvrxlite.base.MVRxLiteState

sealed class UIState {

    data class HomeScreenState(
        val popularMoviesResource: Resource<List<Movie>>,
        val topRatedMoviesResource: Resource<List<Movie>>,
        val searchResultsResource: Resource<List<Movie>>?
    ): UIState(), MVRxLiteState

    object LibraryScreenState: UIState()

    object InTheatresScreenState: UIState()

    data class DetailsScreenState(val movieId: Int = -1,
                                  val transitionName: String? = null,
                                  val sharedView: View? = null,
                                  val movieResource: Resource<Movie>,
                                  val accountStatesResource: Resource<AccountState>,
                                  val trailerResource: Resource<MovieTrailer>,
                                  val castResource: List<Resource<Actor>>): UIState(), MVRxLiteState

    sealed class AccountScreenState: UIState() {
        object AuthenticatedScreenState: AccountScreenState()
        object UnauthenticatedScreenState: AccountScreenState()
    }

    data class ActorDetailsScreenState(val actorId: Int,
                                       val transitionName: String? = null,
                                       val sharedView: View? = null): UIState()
}

sealed class MovieItemType(val name: String) {
    sealed class MovieType(name: String): MovieItemType(name) {
        object Popular: MovieType("popular")
        object TopRated: MovieType("top-rated")
        object SearchResult: MovieType("search-result")
    }
    sealed class LibraryType(name: String): MovieItemType(name) {
        object Favourite: LibraryType("favourite")
        object Watchlisted: LibraryType("watchlisted")
    }
}