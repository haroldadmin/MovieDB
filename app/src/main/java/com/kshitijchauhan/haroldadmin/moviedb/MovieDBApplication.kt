package com.kshitijchauhan.haroldadmin.moviedb

import android.app.Application
import com.kshitijchauhan.haroldadmin.moviedb.di.apiModule
import com.kshitijchauhan.haroldadmin.moviedb.di.applicationModule
import com.kshitijchauhan.haroldadmin.moviedb.di.retrofitModule
import com.kshitijchauhan.haroldadmin.moviedb.di.uiModule
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.MoviesRepository
import org.koin.android.ext.android.get
import org.koin.android.ext.android.startKoin

class MovieDBApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this.applicationContext, listOf(applicationModule, retrofitModule, apiModule, uiModule))
        val localrepo: MoviesRepository = get()
    }
}