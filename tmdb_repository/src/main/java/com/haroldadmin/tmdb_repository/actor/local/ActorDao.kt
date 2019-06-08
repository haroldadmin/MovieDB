package com.haroldadmin.tmdb_repository.actor.local

import androidx.room.*

@Dao
interface ActorDao {
    @Query("SELECT * FROM actors WHERE id = :actorId")
    suspend fun getActor(actorId: Int): Actor

    @Query("SELECT * FROM actors WHERE id IN (SELECT actorId FROM casts WHERE movieId = :movieId)")
    suspend fun getActorsForMovie(movieId: Int): List<Actor>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveActor(actor: Actor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveActors(vararg actor: Actor)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateActor(actor: Actor)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateActors(vararg actor: Actor)

    @Delete
    suspend fun deleteActor(actor: Actor)

    @Delete
    suspend fun deleteActors(vararg actors: Actor)
}