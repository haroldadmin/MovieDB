package com.kshitijchauhan.haroldadmin.moviedb.repository.local

import io.reactivex.Observable

abstract class Repository <T> {
    abstract fun get(id: Int): Observable<T>
    abstract fun getAll(): Observable<List<T>>
    abstract fun save(t: T)
    abstract fun saveMultiple(vararg t: T)
    abstract fun saveAll(ts: List<T>)
    abstract fun update(t: T)
    abstract fun updateMultiple(vararg t: T)
    abstract fun updateAll(ts: List<T>)
    abstract fun delete(t: T)
    abstract fun deleteMultiple(vararg t: T)
    abstract fun deleteAll(ts: List<T>)
}