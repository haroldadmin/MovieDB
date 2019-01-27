package com.kshitijchauhan.haroldadmin.moviedb

import android.app.Application
import com.kshitijchauhan.haroldadmin.moviedb.di.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.db.MovieDBDatabase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
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
//        Single.fromCallable {
//            val db: MovieDBDatabase = get()
//            db.clearAllTables()
//        }
//            .subscribeOn(Schedulers.io())
//            .subscribe()
    }
}