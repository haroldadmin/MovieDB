package com.kshitijchauhan.haroldadmin.moviedb.repository.local.dao

import androidx.room.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Actor
import io.reactivex.Observable

@Dao
interface ActorsDao {

    @Query("SELECT * FROM actors")
    fun getAllActors(): Observable<List<Actor>>

    @Query("SELECT * FROM actors WHERE id = :id")
    fun getActor(id: Int): Observable<Actor>

    @Insert
    fun saveActor(actor: Actor)

    @Insert
    fun saveActors(vararg actor: Actor)

    @Insert
    fun saveAllActors(actors: List<Actor>)

    @Update
    fun updateActor(actor: Actor)

    @Update
    fun updateActors(vararg actor: Actor)

    @Update
    fun updateAllActors(actors: List<Actor>)

    @Delete
    fun deleteActor(actor: Actor)

    @Delete
    fun deleteActors(vararg actors: Actor)

    @Delete
    fun deleteAllActors(actors: List<Actor>)
}