package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.os.Bundle
import android.view.Gravity
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.*
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.LoginFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.discover.DiscoverFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.search.SearchFragment
import com.kshitijchauhan.haroldadmin.moviedb.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity() {

    private var sessionId by SharedPreferencesDelegate(this, Constants.KEY_SESSION_ID, "")
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

    }

    private fun handleStateChange(state: UIState) {
        when (state) {
            is UIState.HomeScreenState -> {
                supportActionBar?.setDisplayShowTitleEnabled(false)
                replaceFragment(HomeFragment.newInstance(), R.id.fragment_container)
            }

            is UIState.AuthScreenState -> {

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
                    exitTransition = exitFade,
                    sharedElement = btAccount,
                    sharedElementTransition = sharedTransitionSet)
            }

            is UIState.DiscoverScreenState -> {

                supportActionBar?.apply {
                    setDisplayShowTitleEnabled(true)
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
                    exitTransition = exitFade,
                    sharedElement = btDiscover,
                    sharedElementTransition = sharedTransitionSet)
            }
            UIState.SearchScreenState -> {

                supportActionBar?.apply {
                    setDisplayShowTitleEnabled(true)
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
                    exitTransition = exitFade,
                    sharedElement = btSearch,
                    sharedElementTransition = sharedTransitionSet)
            }
        }.safe
    }
}
