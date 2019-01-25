package com.kshitijchauhan.haroldadmin.moviedb.repository.local.dao

import androidx.room.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Actor
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ActorsDao {

    @Query("SELECT * FROM actors")
    fun getAllActors(): Flowable<List<Actor>>

    @Query("SELECT * FROM actors WHERE id = :id")
    fun getActor(id: Int): Flowable<Actor>

    @Query("SELECT * FROM actors WHERE id = :id")
    fun getActorBlocking(id: Int): Actor

    @Query("SELECT * FROM actors WHERE id IN (:ids)")
    fun getActors(ids: List<Int>): Flowable<List<Actor>>

    @Query("SELECT COUNT(*) FROM actors WHERE id = :id")
    fun isActorInDatabase(id: Int): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveActor(actor: Actor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveActors(vararg actor: Actor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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