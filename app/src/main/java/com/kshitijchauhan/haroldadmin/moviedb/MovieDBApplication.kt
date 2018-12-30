package com.kshitijchauhan.haroldadmin.moviedb

import android.app.Application
import com.kshitijchauhan.haroldadmin.moviedb.di.component.AppComponent
import com.kshitijchauhan.haroldadmin.moviedb.di.component.DaggerAppComponent
import com.kshitijchauhan.haroldadmin.moviedb.di.module.ContextModule
import com.kshitijchauhan.haroldadmin.moviedb.di.module.RetrofitModule
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.SharedPreferencesDelegate
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log

class MovieDBApplication: Application() {

    private val sessionId by SharedPreferencesDelegate(this, Constants.KEY_SESSION_ID, "")
    private val apiKey by lazy { BuildConfig.API_KEY }

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
        .contextModule(ContextModule(this.applicationContext))
            .retrofitModule(RetrofitModule(sessionId, apiKey))
            .build()
    }

    fun rebuildAppComponent() {
        log("Rebuilding app component")
        appComponent = DaggerAppComponent
            .builder()
            .contextModule(ContextModule(this.applicationContext))
            .retrofitModule(RetrofitModule(sessionId, apiKey))
            .build()
    }
}