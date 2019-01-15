package com.kshitijchauhan.haroldadmin.moviedb.ui

import android.view.View

sealed class UIState {

    object HomeScreenState: UIState()
    object LibraryScreenState: UIState()
    object DiscoverScreenState: UIState()
    object SearchScreenState: UIState()
    data class DetailsScreenState(val movieId: Int,
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
    }
    sealed class LibraryType: MovieItemType() {
        object Favourite: LibraryType()
        object Watchlisted: LibraryType()
    }
}