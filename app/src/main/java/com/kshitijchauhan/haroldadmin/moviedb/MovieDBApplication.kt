package com.kshitijchauhan.haroldadmin.moviedb

import android.app.Application
import com.kshitijchauhan.haroldadmin.moviedb.di.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.MoviesRepository
import org.koin.android.ext.android.get
import org.koin.android.ext.android.startKoin

class MovieDBApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this.applicationContext, listOf(
            applicationModule,
            retrofitModule,
            apiModule,
            uiModule,
            databaseModule,
            repositoryModule
        ))
    }
}