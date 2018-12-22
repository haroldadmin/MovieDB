package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kshitijchauhan.haroldadmin.moviedb.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(mainToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        savedInstanceState ?: supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, MainFragment.newInstance())
            .commit()
    }
}
