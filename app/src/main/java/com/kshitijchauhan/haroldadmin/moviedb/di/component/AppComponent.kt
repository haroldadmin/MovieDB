package com.kshitijchauhan.haroldadmin.moviedb.di.component

import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.AuthenticationViewModel
import com.kshitijchauhan.haroldadmin.moviedb.di.AppScope
import com.kshitijchauhan.haroldadmin.moviedb.di.module.ApiServiceModule
import com.kshitijchauhan.haroldadmin.moviedb.di.module.ContextModule
import com.kshitijchauhan.haroldadmin.moviedb.di.module.RetrofitModule
import com.kshitijchauhan.haroldadmin.moviedb.di.module.UIModule
import com.kshitijchauhan.haroldadmin.moviedb.ui.details.MovieDetailsViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.discover.DiscoverViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.library.LibraryViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.HomeViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainActivity
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.search.SearchViewModel
import dagger.Component

@AppScope
@Component(modules = [ContextModule::class, RetrofitModule::class, ApiServiceModule::class, UIModule::class])
interface AppComponent {

    fun inject(app: MovieDBApplication)

    fun inject(viewModel: AuthenticationViewModel)

    fun inject(viewModel: DiscoverViewModel)

    fun inject(searchViewModel: SearchViewModel)

    fun inject(movieDetailsViewModel: MovieDetailsViewModel)

    fun inject(homeViewModel: HomeViewModel)

    fun inject(libraryViewModel: LibraryViewModel)

    fun inject(mainActivity: MainActivity)

    fun inject(mainViewModel: MainViewModel)

}