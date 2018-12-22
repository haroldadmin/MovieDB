package com.kshitijchauhan.haroldadmin.moviedb.utils

import androidx.appcompat.app.AppCompatActivity
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun AppCompatActivity.app(): MovieDBApplication = this.application as MovieDBApplication

fun Disposable.disposeWith(compositeDisposable: CompositeDisposable) = compositeDisposable.add(this)