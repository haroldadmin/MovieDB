package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe

class BottomNavManager(private val bottomNavigationView: BottomNavigationView) {

    fun setBottomNavState(state: UIState) {
        when (state) {
            UIState.HomeScreenState -> {
                bottomNavigationView.menu.findItem(R.id.menuHome).setChecked(true)
            }
            UIState.LibraryScreenState -> {
                bottomNavigationView.menu.findItem(R.id.menuLibrary).setChecked(true)
            }
            UIState.DiscoverScreenState -> {
                bottomNavigationView.menu.findItem(R.id.menuDiscover).setChecked(true)
            }
            UIState.SearchScreenState -> {
                Any()
            }
            is UIState.DetailsScreenState -> {
                Any()
            }
            UIState.AccountScreenState.AuthenticatedScreenState -> {
                bottomNavigationView.menu.findItem(R.id.menuAccount).setChecked(true)
            }
            UIState.AccountScreenState.UnauthenticatedScreenState -> {
                bottomNavigationView.menu.findItem(R.id.menuAccount).setChecked(true)
            }
        }.safe
    }

}