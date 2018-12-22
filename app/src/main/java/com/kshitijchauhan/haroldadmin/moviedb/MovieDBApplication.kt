package com.kshitijchauhan.haroldadmin.moviedb

import android.app.Application
import com.kshitijchauhan.haroldadmin.moviedb.di.component.AppComponent
import com.kshitijchauhan.haroldadmin.moviedb.di.component.DaggerAppComponent
import com.kshitijchauhan.haroldadmin.moviedb.di.module.ContextModule

class MovieDBApplication: Application() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .contextModule(ContextModule(this.applicationContext))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}