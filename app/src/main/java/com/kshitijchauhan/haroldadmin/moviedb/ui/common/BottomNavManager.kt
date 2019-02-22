package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe

class BottomNavManager {

    lateinit var bottomNavigationView: BottomNavigationView

    fun setBottomNavActiveState(state: UIState) {
        when (state) {
            is UIState.HomeScreenState -> {
                bottomNavigationView.menu.findItem(R.id.menuHome).setChecked(true)
            }
            is UIState.LibraryScreenState -> {
                bottomNavigationView.menu.findItem(R.id.menuLibrary).setChecked(true)
            }
            UIState.InTheatresScreenState -> {
                bottomNavigationView.menu.findItem(R.id.menuInTheatres).setChecked(true)
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
            is UIState.ActorDetailsScreenState -> {
                Any()
            }
        }.safe
    }

    fun setBottomNavView(bottomNavigationView: BottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView
    }

}