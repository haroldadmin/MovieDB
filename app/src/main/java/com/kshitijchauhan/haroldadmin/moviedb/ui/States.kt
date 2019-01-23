package com.kshitijchauhan.haroldadmin.moviedb.ui

import android.view.View

sealed class UIState {

    object HomeScreenState: UIState()
    object LibraryScreenState: UIState()
    object InTheatresScreenState: UIState()
    object SearchScreenState: UIState()
    data class DetailsScreenState(val movieId: Int = -1,
                                  val transitionName: String? = null,
                                  val sharedView: View? = null): UIState()
    sealed class AccountScreenState: UIState() {
        object AuthenticatedScreenState: AccountScreenState()
        object UnauthenticatedScreenState: AccountScreenState()
    }
}

sealed class MovieItemType {
    sealed class MovieType: MovieItemType() {
        object Popular: MovieType()
        object TopRated: MovieType()
        object SearchResult: MovieType()
    }
    sealed class LibraryType: MovieItemType() {
        object Favourite: LibraryType()
        object Watchlisted: LibraryType()
    }
}