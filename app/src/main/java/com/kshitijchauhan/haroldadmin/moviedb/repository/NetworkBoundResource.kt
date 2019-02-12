package com.kshitijchauhan.haroldadmin.moviedb.repository

import com.jakewharton.rxrelay2.BehaviorRelay
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class NetworkBoundResource<T> {

    private val result: BehaviorRelay<Resource<T>> = BehaviorRelay.create()

    abstract fun fetchFromNetwork(): Flowable<Resource<T>>

    abstract fun fetchFromDatabase(): Flowable<Resource<T>>

    abstract fun shouldRefresh(): Single<Boolean>

    abstract fun saveToDatabase(t: T)

    fun init(compositeDisposable: CompositeDisposable): BehaviorRelay<Resource<T>> {
        shouldRefresh()
            .subscribeOn(Schedulers.io())
            .flatMapPublisher { shouldRefresh ->
                if (shouldRefresh) {
                    fetchFromNetwork()
                        .switchMap { resource ->
                            if (resource is Resource.Success) {
                                saveToDatabase(resource.data)
                            }
                            fetchFromDatabase()
                        }
                } else {
                    fetchFromDatabase()
                }
            }
            .startWith(Resource.Loading())
            .subscribe { resource ->
                result.accept(resource)
            }
            .disposeWith(compositeDisposable)

        return result
    }

    fun getResult(): Observable<Resource<T>> = result
}