package com.haroldadmin.tmdb_repository.movie.local.models

import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * A data class to represent the relationship set Movie-Genre.
 * We create a separate table for this relation because it is a MxN relation.
 * The '@Relation' annotation from is suitable only for 1xN relations.
 */
@Entity(
    tableName = "movie_genre",
    primaryKeys = ["movieId", "genreId"],
    foreignKeys = [
        ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"]),
        ForeignKey(entity = Genre::class, parentColumns = ["id"], childColumns = ["genreId"])
    ]
)
data class MovieGenre(
    val movieId: Int,
    val genreId: Int
)