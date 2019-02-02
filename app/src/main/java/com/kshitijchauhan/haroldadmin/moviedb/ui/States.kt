package com.kshitijchauhan.haroldadmin.moviedb.ui

import android.view.View
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.AccountState

sealed class UIState {

    object HomeScreenState: UIState()
    object LibraryScreenState: UIState()
    object InTheatresScreenState: UIState()
    data class DetailsScreenState(val movieId: Int = -1,
                                  val transitionName: String? = null,
                                  val sharedView: View? = null,
                                  val trailerKey: String? = null,
                                  val actors: List<Actor>? = null,
                                  val accountStates: AccountState? = null,
                                  val overview: String? = null): UIState()
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