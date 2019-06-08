package com.haroldadmin.tmdb_repository.actor.local

import androidx.room.Entity
import androidx.room.ForeignKey
import com.haroldadmin.tmdb_repository.actor.local.Actor
import com.haroldadmin.tmdb_repository.movie.local.models.Movie

/**
 * A data class to represent the Movie-Actor relationship set.
 * We create a table for this relation instead because it has MxN
 * mapping cardinality. The `@Relation` annotation from room is suitable
 * for 1XN relations only.
 */
@Entity(
    tableName = "casts",
    primaryKeys = ["movieId", "actorId"],
    foreignKeys = [
        ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Actor::class, parentColumns = ["id"], childColumns = ["actorId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Cast(
    val movieId: Int,
    val actorId: Int
)