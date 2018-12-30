package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.*
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.LoggedOutFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.AccountFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.details.MovieDetailsFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.discover.DiscoverFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.search.SearchFragment
import com.kshitijchauhan.haroldadmin.moviedb.utils.*
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.replaceFragment
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.snackbar
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

                mainCollapsingToolbarLayout?.apply {
                    title = "MovieDB"
                }

                mainNavView.selectedItemId = R.id.menuHome

                replaceFragment(
                    HomeFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.UnauthenticatedScreenState -> {

                mainCollapsingToolbarLayout?.apply {
                    title = "Login"
                }

                mainNavView.selectedItemId = R.id.menuAccount

                replaceFragment(
                    LoggedOutFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.AuthenticatedScreenState -> {

                mainCollapsingToolbarLayout?.apply {
                    title = "Your Account"
                }

                mainNavView.selectedItemId = R.id.menuAccount

                replaceFragment(
                    AccountFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.DiscoverScreenState -> {

                mainCollapsingToolbarLayout?.apply {
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

                mainCollapsingToolbarLayout?.apply {
                    title = "Search"
                }

                mainNavView.selectedItemId = R.id.menuHome

                mainAppBarLayout.setExpanded(false, true)

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
                    sharedElement = state.sharedView)
            }
        }.safe

//        mainAppBarLayout.setExpanded(true, true)
    }
}
