package com.kshitijchauhan.haroldadmin.moviedb.repository.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actors")
data class Actor(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "profile_picture_url")
    val profilePictureUrl: String,
    @ColumnInfo(name = "name")
    val name: String
)