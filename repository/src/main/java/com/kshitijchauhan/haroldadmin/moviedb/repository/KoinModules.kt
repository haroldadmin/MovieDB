package com.kshitijchauhan.haroldadmin.moviedb.repository

import android.content.SharedPreferences
import androidx.room.Room
import com.haroldadmin.cnradapter.CoroutinesNetworkResponseAdapterFactory
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.ActorsRepository
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.LocalActorsSource
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.RemoteActorsSource
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionsRepository
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.LocalCollectionsSource
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.RemoteCollectionsSource
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.db.MovieDBDatabase
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.AccountService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.auth.AuthenticationService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.discover.DiscoveryService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.movie.MovieService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.people.PersonService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.search.SearchService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.utils.ApiKeyInterceptor
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.utils.Config
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.utils.SessionIdInterceptor
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.LocalMoviesSource
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MoviesRepository
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.RemoteMoviesSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

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
        val sessionId = get<SharedPreferences>().getString(com.kshitijchauhan.haroldadmin.moviedb.core.Constants.KEY_SESSION_ID, "")

        OkHttpClient.Builder()
            .addInterceptor(get<ApiKeyInterceptor> {
                parametersOf(apiKey)
            })
            .addInterceptor(get<SessionIdInterceptor> {
                parametersOf(sessionId)
            })
            .also {
                if (BuildConfig.DEBUG)
                    it.addInterceptor(get<HttpLoggingInterceptor> {
                        parametersOf(HttpLoggingInterceptor.Level.BASIC)
                    })
            }
            .cache(get())
            .build()
    }

    single {
        val adapter = SafeRfc3339DateJsonAdapter(Rfc3339DateJsonAdapter()).nullSafe()
        Moshi.Builder()
            .add(Date::class.java, adapter)
            .build()
            .let { moshi -> MoshiConverterFactory.create(moshi) }
    }


    single { CoroutinesNetworkResponseAdapterFactory() }

    single {
        Retrofit.Builder()
            .client(get())
            .addCallAdapterFactory(get<CoroutinesNetworkResponseAdapterFactory>())
            .addConverterFactory(get<MoshiConverterFactory>())
            .baseUrl(Config.BASE_URL)
            .build()
    }
}

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            MovieDBDatabase::class.java,
            androidContext().getString(R.string.app_name)
        ).build()
    }
}

val apiModule = module {
    single { get<Retrofit>().create(AuthenticationService::class.java) }
    single { get<Retrofit>().create(DiscoveryService::class.java) }
    single { get<Retrofit>().create(SearchService::class.java) }
    single { get<Retrofit>().create(MovieService::class.java) }
    single { get<Retrofit>().create(AccountService::class.java) }
    single { get<Retrofit>().create(PersonService::class.java) }
    single { ApiManager(get(), get()) }
}

val repositoryModule = module {
    single { get<MovieDBDatabase>().moviesDao() }
    single { get<MovieDBDatabase>().actorsDao() }
    single { get<MovieDBDatabase>().collectionsDao() }

    single { LocalMoviesSource(get(), get()) }
    single { RemoteMoviesSource(get(), get(), get()) }
    single { LocalActorsSource(get()) }
    single { RemoteActorsSource(get()) }
    single { LocalCollectionsSource(get(), get()) }
    single { RemoteCollectionsSource(get(), get()) }

    single { MoviesRepository(get(), get()) }
    single { ActorsRepository(get(), get()) }
    single { CollectionsRepository(get(), get(), get()) }
}