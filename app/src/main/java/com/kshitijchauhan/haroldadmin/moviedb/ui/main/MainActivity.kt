package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Fade
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.BackPressListener
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private var backPressListener: BackPressListener? = null
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController

        with(mainViewModel) {
            snackbar.observe(this@MainActivity, Observer { message ->
                homeRootView.snackbar(message)
            })
            toolbarTitle.observe(this@MainActivity, Observer { newTitle ->
                supportActionBar?.apply {
                    title = newTitle
                }
            })
            backPressListener.observe(this@MainActivity, Observer { listener ->
                this@MainActivity.backPressListener = listener
            })
            navigationGraph.observe(this@MainActivity, Observer { graphResId ->
                navController.setGraph(graphResId)
            })
        }

        if (mainViewModel.isAuthenticated) {
            navController.setGraph(R.navigation.nav_graph_logged_in)
        } else {
            navController.setGraph(R.navigation.nav_graph_logged_out)
        }

        mainNavView.setupWithNavController(navController)

        setSupportActionBar(mainToolbar)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
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

//        when (state) {
//            is UIState.HomeScreenState -> {
//                replaceFragment(
//                    HomeFragment.newInstance(),
//                    R.id.fragment_container,
//                    enterTransition = enterFade,
//                    exitTransition = exitFade
//                )
//            }
//
//            is UIState.AccountScreenState.UnauthenticatedScreenState -> {
//                replaceFragment(
//                    LoggedOutFragment.newInstance(),
//                    R.id.fragment_container,
//                    enterTransition = enterFade,
//                    exitTransition = exitFade
//                )
//            }
//
//            is UIState.AccountScreenState.AuthenticatedScreenState -> {
//                replaceFragment(
//                    AccountFragment.newInstance(),
//                    R.id.fragment_container,
//                    enterTransition = enterFade,
//                    exitTransition = exitFade
//                )
//            }
//
//            is UIState.InTheatresScreenState -> {
//                replaceFragment(
//                    InTheatresFragment.newInstance(),
//                    R.id.fragment_container,
//                    enterTransition = enterFade,
//                    exitTransition = exitFade
//                )
//            }
//
//            is UIState.DetailsScreenState -> {
//
//                val transitionSet = TransitionSet()
//                transitionSet.apply {
//                    addTransition(TransitionInflater.from(this@MainActivity).inflateTransition(android.R.transition.move))
//                    duration = 300
//                }
//
//                replaceFragment(
//                    MovieDetailsFragment.newInstance(state.movieId, state.transitionName ?: ""),
//                    R.id.fragment_container,
//                    enterTransition = enterFade,
//                    exitTransition = exitFade,
//                    sharedElementTransition = transitionSet,
//                    sharedElement = state.sharedView
//                )
//            }
//            is UIState.LibraryScreenState -> {
//                replaceFragment(
//                    LibraryFragment.newInstance(),
//                    R.id.fragment_container,
//                    enterTransition = enterFade,
//                    exitTransition = exitFade
//                )
//            }
//            is UIState.ActorDetailsScreenState -> {
//                val transitionSet = TransitionSet()
//                transitionSet.apply {
//                    addTransition(TransitionInflater.from(this@MainActivity).inflateTransition(android.R.transition.move))
//                    duration = 300
//                }
//
//                replaceFragment(
//                    ActorDetailsFragment.newInstance(state.actorId, state.transitionName ?: ""),
//                    R.id.fragment_container,
//                    enterTransition = enterFade,
//                    exitTransition = exitFade,
//                    sharedElementTransition = transitionSet,
//                    sharedElement = state.sharedView
//                    )
//            }
//        }.safe
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
