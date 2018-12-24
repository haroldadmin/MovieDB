package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Fade
import androidx.transition.TransitionSet
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.LoginFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.discover.DiscoverFragment
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

        savedInstanceState ?: replaceFragment(HomeFragment.newInstance(), R.id.fragment_container, addToBackStack = false)

        setSupportActionBar(mainToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

    }

    private fun handleStateChange(state: UIState) {
        when (state) {
            is UIState.HomeScreenState ->
                replaceFragment(HomeFragment.newInstance(), R.id.fragment_container)

            is UIState.AuthScreenState -> {
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
                replaceFragment(LoginFragment.newInstance(),
                    R.id.fragment_container,
                    enterTransition = enterFade,
                    exitTransition = exitFade,
                    sharedElement = btAccount,
                    sharedElementTransition = sharedTransitionSet)
            }

            is UIState.DiscoverScreenState -> {

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
        }.safe
    }
}
