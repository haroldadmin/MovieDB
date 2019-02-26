package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.BackPressListener
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
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
            snackbar.observe(this@MainActivity, Observer { snackbarAction ->
                val snackbar = Snackbar.make(homeRootView, snackbarAction.message, snackbarAction.length)
                if (snackbarAction.actionText != null && snackbarAction.action != null) {
                    snackbar.setAction(snackbarAction.actionText, snackbarAction.action)
                }
                snackbar.show()
            })
            toolbarTitle.observe(this@MainActivity, Observer { newTitle ->
                supportActionBar?.apply {
                    title = newTitle
                }
            })
            backPressListener.observe(this@MainActivity, Observer { listener ->
                this@MainActivity.backPressListener = listener
            })
        }

        mainNavView.setupWithNavController(navController)

        setSupportActionBar(mainToolbar)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
        }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
            else -> {
                item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
            }
        }
    }
}
