package com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.config

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImagesConfiguration(
    @field:Json(name="base_url") val baseUrl: String,
    @field:Json(name="secure_base_url") val secureBaseUrl: String,
    @field:Json(name="backdrop_sizes") val backdropSizes: List<String>,
    @field:Json(name="logo_sizes") val logoSizes: List<String>,
    @field:Json(name="poster_sizes") val posterSizes: List<String>,
    @field:Json(name="profile_sizes") val profileSizes: List<String>): Parcelable

@Parcelize
data class ConfigurationResponse(
    val images: ImagesConfiguration
): Parcelable