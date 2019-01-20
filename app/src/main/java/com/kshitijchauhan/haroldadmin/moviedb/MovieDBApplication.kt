package com.kshitijchauhan.haroldadmin.moviedb

import android.app.Application
import android.content.Context
import com.kshitijchauhan.haroldadmin.moviedb.di.apiModule
import com.kshitijchauhan.haroldadmin.moviedb.di.applicationModule
import com.kshitijchauhan.haroldadmin.moviedb.di.component.AppComponent
import com.kshitijchauhan.haroldadmin.moviedb.di.component.DaggerAppComponent
import com.kshitijchauhan.haroldadmin.moviedb.di.module.ContextModule
import com.kshitijchauhan.haroldadmin.moviedb.di.module.RetrofitModule
import com.kshitijchauhan.haroldadmin.moviedb.di.retrofitModule
import com.kshitijchauhan.haroldadmin.moviedb.di.uiModule
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.SharedPreferencesDelegate
import org.koin.android.ext.android.startKoin

class MovieDBApplication: Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        val sessionId by SharedPreferencesDelegate(this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE), Constants.KEY_SESSION_ID, "")
        val apiKey by lazy { BuildConfig.API_KEY }
        appComponent = DaggerAppComponent.builder()
        .contextModule(ContextModule(this.applicationContext))
            .retrofitModule(RetrofitModule(sessionId, apiKey))
            .build()

        startKoin(this.applicationContext, listOf(applicationModule, retrofitModule, apiModule, uiModule))
    }
}