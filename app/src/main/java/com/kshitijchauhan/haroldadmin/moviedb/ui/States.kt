package com.kshitijchauhan.haroldadmin.moviedb.ui

import android.view.View

sealed class UIState {

    object HomeScreenState: UIState()
    object DiscoverScreenState: UIState()
    object SearchScreenState: UIState()
    data class DetailsScreenState(val movieId: Int,
                                  val transitionName: String? = null,
                                  val sharedView: View? = null): UIState()
    object AuthenticatedScreenState: UIState()
    object UnauthenticatedScreenState: UIState()

}