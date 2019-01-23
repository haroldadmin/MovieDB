package com.kshitijchauhan.haroldadmin.moviedb.di

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.kshitijchauhan.haroldadmin.moviedb.BuildConfig
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiKeyInterceptor
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import com.kshitijchauhan.haroldadmin.moviedb.remote.SessionIdInterceptor
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.AccountService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.AuthenticationService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.common.GeneralMovieResponse
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.discover.DiscoveryService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie.MovieService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.search.SearchService
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.AuthenticationViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.BottomNavManager
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.MoviesListAdapter
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.ProgressBarManager
import com.kshitijchauhan.haroldadmin.moviedb.ui.details.CreditsAdapter
import com.kshitijchauhan.haroldadmin.moviedb.ui.details.MovieDetailsViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres.InTheatresViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres.MoviesAdapter
import com.kshitijchauhan.haroldadmin.moviedb.ui.library.LibraryViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.HomeViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.search.SearchResultsAdapter
import com.kshitijchauhan.haroldadmin.moviedb.ui.search.SearchViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


val applicationModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            androidContext().getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }
}

val retrofitModule = module {
    single { Cache(androidContext().cacheDir, 50 * 1024 * 1024) }

    single { (sessionId: String) -> SessionIdInterceptor(sessionId) }

    single { (apiKey: String) -> ApiKeyInterceptor(apiKey) }

    single { (loggingLevel: HttpLoggingInterceptor.Level) ->
        HttpLoggingInterceptor().apply {
            level = loggingLevel
        }
    }

    single {

        val apiKey = BuildConfig.API_KEY
        val sessionId = get<SharedPreferences>().getString(Constants.KEY_SESSION_ID, "")

        OkHttpClient.Builder()
            .addInterceptor(get<ApiKeyInterceptor> {
                parametersOf(apiKey)
            })
            .addInterceptor(get<SessionIdInterceptor> {
                parametersOf(sessionId)
            })
            .addInterceptor(get<HttpLoggingInterceptor> {
                parametersOf(HttpLoggingInterceptor.Level.BODY)
            })
            .cache(get())
            .build()
    }

    single { MoshiConverterFactory.create() }

    single { RxJava2CallAdapterFactory.create() }

    single {
        Retrofit.Builder()
            .client(get())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .addConverterFactory(get<MoshiConverterFactory>())
            .baseUrl(Config.BASE_URL)
            .build()
    }
}

val apiModule = module {
    factory { get<Retrofit>().create(AuthenticationService::class.java) }
    factory { get<Retrofit>().create(DiscoveryService::class.java) }
    factory { get<Retrofit>().create(SearchService::class.java) }
    factory { get<Retrofit>().create(MovieService::class.java) }
    factory { get<Retrofit>().create(AccountService::class.java) }
    single { ApiManager(get(), get(), get(), get(), get()) }
}

val uiModule = module {

    single { BottomNavManager() }
    single { ProgressBarManager() }

    viewModel { HomeViewModel(get()) }
    viewModel { LibraryViewModel(get()) }
    viewModel { InTheatresViewModel(get()) }
    viewModel { AuthenticationViewModel(get(), get(), get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { (isAuthenticated: Boolean, movieId: Int) -> MovieDetailsViewModel(get(), isAuthenticated, movieId) }
    viewModel { MainViewModel(get(), get(), get()) }

    factory("fragment-glide-request-manager") { (fragment: Fragment) -> Glide.with(fragment) }
    factory { (glide: RequestManager) -> CreditsAdapter(glide) }
    factory { (glide: RequestManager, moviesList: MutableList<GeneralMovieResponse>, clickListener: (movieId: Int) -> Unit) ->
        MoviesAdapter(glide, moviesList, clickListener)
    }
    factory { (glide: RequestManager, clickListener: (movieId: Int, transitionName: String, sharedView: View) -> Unit) ->
        MoviesListAdapter(glide, clickListener)
    }
    factory { (searchResults: MutableList<GeneralMovieResponse>, onItemClick: (movieId: Int) -> Unit) ->
        SearchResultsAdapter(searchResults, onItemClick)
    }
}


