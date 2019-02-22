package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.actor_details.ActorDetailsFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.AccountFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.LoggedOutFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.BackPressListener
import com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details.MovieDetailsFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres.InTheatresFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.library.LibraryFragment
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.*
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private var backPressListener: BackPressListener? = null

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

        mainViewModel.clearBackStack.observe(this, Observer {
            supportFragmentManager.clearBackStack()
        })

        mainViewModel.toolbarTitle.observe(this, Observer { newTitle ->
            supportActionBar?.apply {
                title = newTitle
            }
        })

        mainViewModel.backPressListener.observe(this, Observer { listener ->
            this.backPressListener = listener
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

        // TODO Fix this. Change to a navigator based solution
        mainViewModel.getBottomNavView().setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHome -> {
                    mainViewModel.updateStateTo(UIState.HomeScreenState(
                        popularMoviesResource = Resource.Loading(),
                        topRatedMoviesResource = Resource.Loading(),
                        searchResultsResource = null
                    ))
                    true
                }
                R.id.menuLibrary -> {
                    mainViewModel.updateStateTo(UIState.LibraryScreenState(
                        Resource.Loading(),
                        Resource.Loading(),
                        mainViewModel.isAuthenticated
                    ))
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
                    mainViewModel.updateStateTo(UIState.InTheatresScreenState(Resource.Loading()))
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
                replaceFragment(
                    HomeFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.AccountScreenState.UnauthenticatedScreenState -> {
                replaceFragment(
                    LoggedOutFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.AccountScreenState.AuthenticatedScreenState -> {
                replaceFragment(
                    AccountFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }

            is UIState.InTheatresScreenState -> {
                replaceFragment(
                    InTheatresFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
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
            is UIState.LibraryScreenState -> {
                replaceFragment(
                    LibraryFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade
                )
            }
            is UIState.ActorDetailsScreenState -> {
                val transitionSet = TransitionSet()
                transitionSet.apply {
                    addTransition(TransitionInflater.from(this@MainActivity).inflateTransition(android.R.transition.move))
                    duration = 300
                }

                replaceFragment(
                    ActorDetailsFragment.newInstance(state.actorId, state.transitionName ?: ""),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade,
                    sharedElementTransition = transitionSet,
                    sharedElement = state.sharedView
                    )
            }
        }.safe
    }

    override fun onBackPressed() {
        if (backPressListener == null || backPressListener?.onBackPressed() == true) {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menuPrivacyPolicy -> {
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(Constants.PRIVACY_POLICY_URL)
                }.also {
                    startActivity(it)
                }
                true
            }
            R.id.menuTandC -> {
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(Constants.TERMS_AND_CONDITIONS_URL)
                }.also {
                    startActivity(it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
