package com.kshitijchauhan.haroldadmin.moviedb.remote.service.account

import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import io.reactivex.Single
import retrofit2.http.GET

interface AccountService {

    @GET("${Config.API_VERSION}/account")
    fun getAccountDetails(): Single<AccountDetailsResponse>

}