package com.kshitijchauhan.haroldadmin.moviedb.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.kshitijchauhan.haroldadmin.moviedb.R

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthenticationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        savedInstanceState ?: supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, LoginFragment.newInstance())
            .commit()

        authViewModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)
    }
}
