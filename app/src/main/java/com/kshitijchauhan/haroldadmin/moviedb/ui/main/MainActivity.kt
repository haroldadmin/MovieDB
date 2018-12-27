package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Fade
import androidx.transition.TransitionSet
import com.google.android.material.snackbar.Snackbar
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.LoginFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.details.MovieDetailsFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.discover.DiscoverFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.search.SearchFragment
import com.kshitijchauhan.haroldadmin.moviedb.utils.*
import kotlinx.android.synthetic.main.activity_main_alternate.*

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
            when(item.itemId) {
                R.id.menuHome -> {
                    replaceFragment(HomeFragment.newInstance(), R.id.fragment_container)
                    true
                }
                R.id.menuLibrary -> {
                    replaceFragment(DiscoverFragment.newInstance(), R.id.fragment_container)
                    true
                }
                else -> {
                    Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }
    }

    private fun handleStateChange(state: UIState) {
        when (state) {
            is UIState.HomeScreenState -> {
                replaceFragment(HomeFragment.newInstance(), R.id.fragment_container)
            }

            is UIState.AuthScreenState -> {

                mainAppBarLayout.setExpanded(true, true)

                supportActionBar?.apply {
                    setDisplayShowTitleEnabled(true)
                    title = "Your Account"
                }

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

                val sharedTransitionSet = TransitionSet()
                sharedTransitionSet.apply {
                    ordering = TransitionSet.ORDERING_TOGETHER
                    addTransition(TextSizeTransition())
                    addTransition(ChangeBounds())
                    addTransition(ChangeTransform())
                    interpolator = adInterpolator
                    duration = 300
                }
                replaceFragment(
                    LoginFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade)
//                    sharedElement = btAccount,
//                    sharedElementTransition = sharedTransitionSet)
            }

            is UIState.DiscoverScreenState -> {

                mainCollapsingToolbarLayout?.apply {
                    title = "Discover"
                }


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

                val sharedTransitionSet = TransitionSet()
                sharedTransitionSet.apply {
                    ordering = TransitionSet.ORDERING_TOGETHER
                    addTransition(TextSizeTransition())
                    addTransition(ChangeBounds())
                    addTransition(ChangeTransform())
                    interpolator = adInterpolator
                    duration = 300
                }

                replaceFragment(DiscoverFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade)
//                    sharedElement = btDiscover,
//                    sharedElementTransition = sharedTransitionSet)
            }
            UIState.SearchScreenState -> {

                mainAppBarLayout.setExpanded(true, true)

                mainCollapsingToolbarLayout?.apply {
                    title = "Search"
                }
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

                val sharedTransitionSet = TransitionSet()
                sharedTransitionSet.apply {
                    ordering = TransitionSet.ORDERING_TOGETHER
                    addTransition(TextSizeTransition())
                    addTransition(ChangeBounds())
                    addTransition(ChangeTransform())
                    interpolator = adInterpolator
                    duration = 300
                }

                replaceFragment(SearchFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade)
//                    sharedElement = btSearch,
//                    sharedElementTransition = sharedTransitionSet)
            }
            is UIState.DetailsScreenState -> {
                val detailsFragment: MovieDetailsFragment = MovieDetailsFragment.newInstance(state.movieId)
                replaceFragment(detailsFragment, R.id.fragment_container)
            }
        }.safe
    }
}
