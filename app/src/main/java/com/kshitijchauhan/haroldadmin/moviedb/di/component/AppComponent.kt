package com.kshitijchauhan.haroldadmin.moviedb.di.component

import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.auth.AuthenticationActivity
import com.kshitijchauhan.haroldadmin.moviedb.auth.AuthenticationViewModel
import com.kshitijchauhan.haroldadmin.moviedb.di.AppScope
import com.kshitijchauhan.haroldadmin.moviedb.di.module.ContextModule
import com.kshitijchauhan.haroldadmin.moviedb.di.module.RetrofitModule
import dagger.Component

@AppScope
@Component(modules = [ContextModule::class, RetrofitModule::class])
interface AppComponent {

    fun inject(app: MovieDBApplication)

    fun inject(viewModel: AuthenticationViewModel)

}