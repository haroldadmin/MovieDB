package com.haroldadmin.tmdb_repository

import com.haroldadmin.cnradapter.NetworkResponse
import com.kshitijchauhan.haroldadmin.moviedb.core.Resource

internal fun <Success : Any, Error : Any> NetworkResponse<Success, Error>.toResource(): Resource<Success> {
    return when (this) {
        is NetworkResponse.Success -> Resource.Success(this.body)
        is NetworkResponse.ServerError -> Resource.Error(this.body.toString())
        is NetworkResponse.NetworkError -> Resource.Error(this.error.localizedMessage)
    }
}

internal fun <Success : Any, Error : Any, T> NetworkResponse<Success, Error>.mapToResource(mapper: Success.() -> T): Resource<T> {
    return when (this) {
        is NetworkResponse.Success -> Resource.Success(mapper(this.body))
        is NetworkResponse.ServerError -> Resource.Error(this.body.toString())
        is NetworkResponse.NetworkError -> Resource.Error(this.error.localizedMessage)
    }
}

internal fun <T: Any> T.toResource(): Resource<T> = Resource.Success(this)
