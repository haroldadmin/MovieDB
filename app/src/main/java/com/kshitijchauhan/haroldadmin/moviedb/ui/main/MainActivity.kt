package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.LoginFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.discover.DiscoverFragment
import com.kshitijchauhan.haroldadmin.moviedb.utils.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var sessionId by SharedPreferencesDelegate(this, Constants.KEY_SESSION_ID, "")
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mainViewModel.state.observe(this, Observer { pair ->
            log(pair.second.toString())
            handleStateChange(pair)
        })

        savedInstanceState ?: mainViewModel.updateState(null to UIState.HomeScreenState)

        setSupportActionBar(mainToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

    }

    private fun handleStateChange(state: Pair<UIState?, UIState>) {
        when (state.second) {
            is UIState.HomeScreenState ->
                replaceFragment(HomeFragment.newInstance(), R.id.fragment_container)

            is UIState.AuthScreenState ->
                replaceFragment(LoginFragment.newInstance(), R.id.fragment_container)
            is UIState.DiscoverScreenState ->
                replaceFragment(DiscoverFragment.newInstance(), R.id.fragment_container)
        }.safe
    }
}
