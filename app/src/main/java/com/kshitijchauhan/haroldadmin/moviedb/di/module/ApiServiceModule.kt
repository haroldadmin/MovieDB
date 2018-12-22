package com.kshitijchauhan.haroldadmin.moviedb.di.module

import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.AuthenticationService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes=[RetrofitModule::class])
class ApiServiceModule {

    @Provides
    fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService =
            retrofit.create(AuthenticationService::class.java)

}
