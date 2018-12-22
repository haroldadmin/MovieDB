package com.kshitijchauhan.haroldadmin.moviedb.di.module

import android.content.Context
import com.kshitijchauhan.haroldadmin.moviedb.di.AppScope
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiKeyInterceptor
import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module(includes = [ContextModule::class])
class RetrofitModule {

    @AppScope
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @AppScope
    @Provides
    fun provideHttpClient(apiKeyInterceptor: ApiKeyInterceptor,
                          interceptor: HttpLoggingInterceptor,
                          cache: Cache): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(interceptor)
            .cache(cache)
            .build()

    @AppScope
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }
            .also { return it }

    @AppScope
    @Provides
    fun provideApiKeyInterceptor(): ApiKeyInterceptor =
        ApiKeyInterceptor()

    @AppScope
    @Provides
    fun provideCache(context: Context): Cache = Cache(context.cacheDir, 5 * 1024 * 1024)

}