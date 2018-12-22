package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kshitijchauhan.haroldadmin.moviedb.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState ?: supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, MainFragment.newInstance())
            .commit()
    }
}
