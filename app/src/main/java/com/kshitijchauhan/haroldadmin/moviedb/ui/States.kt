package com.kshitijchauhan.haroldadmin.moviedb.ui

sealed class UIState {

    object HomeScreenState: UIState()
    object AuthScreenState: UIState()
    object DiscoverScreenState: UIState()
    object SearchScreenState: UIState()

}