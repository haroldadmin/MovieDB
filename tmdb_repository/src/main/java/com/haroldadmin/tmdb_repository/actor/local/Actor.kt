package com.haroldadmin.tmdb_repository.actor.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "actors")
data class Actor(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "profile_picture_url")
    val profilePictureUrl: String?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "birthday")
    val birthday: Date?,
    @ColumnInfo(name = "biography")
    val biography: String?
)