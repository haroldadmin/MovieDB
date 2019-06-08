package com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote

import com.haroldadmin.cnradapter.NetworkResponse
import com.kshitijchauhan.haroldadmin.moviedb.core.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.AccountDetailsResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.AccountService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.auth.AuthenticationService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.auth.CreateSessionRequest
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx2.asSingle

class ApiManager internal constructor(
    private val authenticationService: AuthenticationService,
    private val accountService: AccountService
) {

    fun getRequestToken(): Single<Resource<String>> {
        return authenticationService
            .getRequestToken()
            .asSingle(Dispatchers.Default)
            .flatMap { response ->
                Single.just(
                    when (response) {
                        is NetworkResponse.Success -> {
                            Resource.Success(response.body.requestToken)
                        }
                        is NetworkResponse.ServerError -> {
                            Resource.Error<String>(response.body?.statusMessage ?: "Server Error")
                        }
                        is NetworkResponse.NetworkError -> {
                            Resource.Error(response.error.localizedMessage ?: "Network Error")
                        }
                    }
                )
            }
    }

    fun createSession(request: CreateSessionRequest): Single<Resource<String>> {
        return authenticationService.createNewSession(request)
            .asSingle(Dispatchers.Default)
            .flatMap { response ->
                Single.just(
                    when (response) {
                        is NetworkResponse.Success -> {
                            Resource.Success(response.body.sessionId)
                        }
                        is NetworkResponse.ServerError -> {
                            Resource.Error<String>(response.body?.statusMessage ?: "Server Error")
                        }
                        is NetworkResponse.NetworkError -> {
                            Resource.Error(response.error.localizedMessage ?: "Network Error")
                        }
                    }
                )
            }
    }

    fun getAccountDetails(): Single<Resource<AccountDetailsResponse>> {
        return accountService
            .getAccountDetails()
            .asSingle(Dispatchers.Default)
            .flatMap { response ->
                Single.just(
                    when (response) {
                        is NetworkResponse.Success -> {
                            Resource.Success(response.body)
                        }
                        is NetworkResponse.ServerError -> {
                            Resource.Error<AccountDetailsResponse>(response.body?.statusMessage ?: "Server Error")
                        }
                        is NetworkResponse.NetworkError -> {
                            Resource.Error(response.error.localizedMessage ?: "Network Error")
                        }
                    }
                )
            }
    }
}