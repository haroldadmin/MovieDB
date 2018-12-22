package com.kshitijchauhan.haroldadmin.moviedb.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var authViewModel: AuthenticationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)

        savedInstanceState ?: supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, LoginFragment.newInstance())
            .commit()

        authViewModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)
    }
}
