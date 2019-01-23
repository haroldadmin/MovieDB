package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.AccountFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.LoggedOutFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.details.MovieDetailsFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres.InTheatresFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.library.LibraryFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.search.SearchFragment
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel.setBottomNavView(mainNavView)

        mainViewModel.state.observe(this, Observer { pair ->
            handleStateChange(pair)
        })

        mainViewModel.snackbar.observe(this, Observer { message ->
            homeRootView.snackbar(message)
        })

        mainViewModel.progressBarNotification.observe(this, Observer { notification ->
            if (notification.visible) mainProgressBar.visible() else mainProgressBar.gone()
        })

        mainViewModel.clearBackStack.observe(this, Observer {
            supportFragmentManager.clearBackStack()
        })

        mainViewModel.toolbarTitle.observe(this, Observer { newTitle ->
            supportActionBar?.apply {
                title = newTitle
            }
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

        mainViewModel.getBottomNavView().setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHome -> {
                    mainViewModel.updateStateTo(UIState.HomeScreenState)
                    true
                }
                R.id.menuLibrary -> {
                    mainViewModel.updateStateTo(UIState.LibraryScreenState)
                    true
                }
                R.id.menuAccount -> {
                    if (mainViewModel.isAuthenticated) {
                        mainViewModel.updateStateTo(UIState.AccountScreenState.AuthenticatedScreenState)
                    } else {
                        mainViewModel.updateStateTo(UIState.AccountScreenState.UnauthenticatedScreenState)
                    }
                    true
                }
                R.id.menuInTheatres -> {
                    mainViewModel.updateStateTo(UIState.InTheatresScreenState)
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

                supportActionBar?.apply {
                    title = "MovieDB"
                }

                replaceFragment(
                    HomeFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.AccountScreenState.UnauthenticatedScreenState -> {

                supportActionBar?.apply {
                    title = "Login"
                }

                replaceFragment(
                    LoggedOutFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.AccountScreenState.AuthenticatedScreenState -> {

                supportActionBar?.apply {
                    title = "Your Account"
                }

                replaceFragment(
                    AccountFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.InTheatresScreenState -> {

                supportActionBar?.apply {
                    title = "In Theatres"
                }

                replaceFragment(
                    InTheatresFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.SearchScreenState -> {

                supportActionBar?.apply {
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

                val transitionSet = TransitionSet()
                transitionSet.apply {
                    addTransition(TransitionInflater.from(this@MainActivity).inflateTransition(android.R.transition.move))
                    duration = 300
                }

                replaceFragment(
                    MovieDetailsFragment.newInstance(state.movieId, state.transitionName ?: ""),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade,
                    sharedElementTransition = transitionSet,
                    sharedElement = state.sharedView
                )
            }
            UIState.LibraryScreenState -> {
                supportActionBar?.apply {
                    title = "Library"
                }

                replaceFragment(
                    LibraryFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }
        }.safe
    }
}
