package com.kshitijchauhan.haroldadmin.moviedb.repository.actors

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
internal interface ActorsDao {

    @Query("SELECT * FROM actors")
    fun getAllActors(): Flowable<List<Actor>>

    @Query("SELECT * FROM actors WHERE id = :id")
    fun getActor(id: Int): Single<Actor>

    @Query("SELECT * FROM actors WHERE id = :id")
    fun getActorFlowable(id: Int): Flowable<Actor>

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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAllActors(actors: List<Actor>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAllActorsFromCast(actors: List<Actor>)

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