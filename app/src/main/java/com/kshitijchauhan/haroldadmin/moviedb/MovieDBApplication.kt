package com.kshitijchauhan.haroldadmin.moviedb

import android.app.Application
import com.kshitijchauhan.haroldadmin.moviedb.di.applicationModule
import com.kshitijchauhan.haroldadmin.moviedb.di.uiModule
import com.kshitijchauhan.haroldadmin.moviedb.repository.apiModule
import com.kshitijchauhan.haroldadmin.moviedb.repository.databaseModule
import com.kshitijchauhan.haroldadmin.moviedb.repository.repositoryModule
import com.kshitijchauhan.haroldadmin.moviedb.repository.retrofitModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieDBApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieDBApplication.applicationContext)
            modules(
                listOf(
                    applicationModule,
                    retrofitModule,
                    apiModule,
                    uiModule,
                    databaseModule,
                    repositoryModule
                )
            )
        }
    }
}