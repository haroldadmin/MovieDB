package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.AccountFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.LoggedOutFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.details.MovieDetailsFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.discover.DiscoverFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.search.SearchFragment
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.SharedPreferencesDelegate
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.*
import kotlinx.android.synthetic.main.activity_main_alternate.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity() {

    private var sessionId by SharedPreferencesDelegate(this, Constants.KEY_SESSION_ID, "")
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_alternate)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mainViewModel.state.observe(this, Observer { pair ->
            handleStateChange(pair)
        })

        mainViewModel.snackbar.observe(this, Observer { message ->
            homeRootView.snackbar(message)
        })

        mainViewModel.bottomNavSelectedItemId.observe(this, Observer { id ->
            mainNavView.selectedItemId = id
        })

        mainViewModel.progressBar.observe(this, Observer { isVisible ->
            if (isVisible) mainProgressBar.visible() else mainProgressBar.gone()
        })

        savedInstanceState ?: replaceFragment(
            HomeFragment.newInstance(),
            R.id.fragment_container,
            addToBackStack = false
        )

        setSupportActionBar(mainToolbar)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
        }

        mainNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHome -> {
                    mainViewModel.updateStateTo(UIState.HomeScreenState)
                    replaceFragment(HomeFragment.newInstance(), R.id.fragment_container)
                    true
                }
                R.id.menuLibrary -> {
                    mainViewModel.updateStateTo(UIState.DiscoverScreenState)
                    true
                }
                R.id.menuAccount -> {
                    if (mainViewModel.isAuthenticated) {
                        mainViewModel.updateStateTo(UIState.AuthenticatedScreenState)
                    } else {
                        mainViewModel.updateStateTo(UIState.UnauthenticatedScreenState)
                    }
                    true
                }
                else -> throw IllegalStateException("Unknown screen state")
            }
        }
    }

    private fun handleStateChange(state: UIState) {

        val adInterpolator = AccelerateDecelerateInterpolator()

        val enterFade = Fade()
        enterFade.apply {
            duration = 300
            interpolator = adInterpolator
        }

        val exitFade = Fade()
        exitFade.apply {
            duration = 300
            interpolator = adInterpolator
        }

        when (state) {
            is UIState.HomeScreenState -> {

                mainToolbar?.apply {
                    title = "MovieDB"
                }

                replaceFragment(
                    HomeFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.UnauthenticatedScreenState -> {

                mainToolbar?.apply {
                    title = "Login"
                }

                replaceFragment(
                    LoggedOutFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.AuthenticatedScreenState -> {

                mainToolbar?.apply {
                    title = "Your Account"
                }

                replaceFragment(
                    AccountFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.DiscoverScreenState -> {

                mainToolbar?.apply {
                    title = "Discover"
                }

                mainNavView.selectedItemId = R.id.menuLibrary

                replaceFragment(
                    DiscoverFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.SearchScreenState -> {

                mainToolbar?.apply {
                    title = "Search"
                }

                val transitionSet = TransitionSet()
                transitionSet.apply {
                    addTransition(TransitionInflater.from(this@MainActivity).inflateTransition(android.R.transition.move))
                    duration = 500
                }

                replaceFragment(
                    SearchFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade,
                    sharedElement = searchBox,
                    sharedElementTransition = transitionSet
                )
            }

            is UIState.DetailsScreenState -> {

                mainNavView.selectedItemId = R.id.menuHome

                val transitionSet = TransitionSet()
                transitionSet.apply {
                    addTransition(TransitionInflater.from(this@MainActivity).inflateTransition(android.R.transition.move))
                    duration = 300
                }

                replaceFragment(
                    MovieDetailsFragment.newInstance(state.movieId, state.transitionName ?: ""),
                    R.id.fragment_container,
//                    enterTransition = enterFade,
//                    exitTransition = exitFade,
                    sharedElementTransition = transitionSet,
                    sharedElement = state.sharedView
                )
            }
        }.safe

//        mainAppBarLayout.setExpanded(true, true)
    }
}
