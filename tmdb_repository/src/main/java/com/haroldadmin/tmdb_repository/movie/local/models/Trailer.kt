package com.haroldadmin.tmdb_repository.movie.local.models

import androidx.room.*

/**
 * A data class to represent a movie trailer.
 * @property movieId is used to link the trailer to a movie
 */
@Entity(
    tableName = "trailers",
    foreignKeys = [ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"])]
)
data class Trailer(
    @PrimaryKey
    val id: String,
    val movieId: Int,
    @ColumnInfo(name = "key")
    val key: String,
    @ColumnInfo(name = "site")
    val site: String
)

