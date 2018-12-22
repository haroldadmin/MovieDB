package com.kshitijchauhan.haroldadmin.moviedb.di.module

import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.AuthenticationService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.discover.DiscoveryService
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.search.SearchService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes=[RetrofitModule::class])
class ApiServiceModule {

    @Provides
    fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService =
            retrofit.create(AuthenticationService::class.java)

    @Provides
    fun provideDiscoveryService(retrofit: Retrofit): DiscoveryService =
            retrofit.create(DiscoveryService::class.java)

    @Provides
    fun provideSearchService(retrofit: Retrofit): SearchService =
            retrofit.create(SearchService::class.java)

}
