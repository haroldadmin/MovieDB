package com.haroldadmin.tmdb_repository.movie.local.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * A data class to represent the relation between a movie and its trailers
 * The `@Relation` annotation from Room is suitable here because there is a
 * 1XN mapping cardinality between the two tables.
 */
data class MovieTrailer(
    @Embedded
    val movie: Movie,
    @Relation(entity = Trailer::class, parentColumn = "id", entityColumn = "movieId")
    val trailers: List<Trailer>
)