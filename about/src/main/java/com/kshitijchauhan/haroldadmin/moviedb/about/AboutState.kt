package com.kshitijchauhan.haroldadmin.moviedb.about

import com.kshitijchauhan.haroldadmin.moviedb.BuildConfig

private const val githubProfile = "https://www.github.com/haroldadmin"
private const val authName = "Kshitij Chauhan"
private const val repoUrl = "https://www.github.com/haroldadmin/moviedb"
private const val appUrl = "https://play.google.com/store/apps/details?id=com.kshitijchauhan.haroldadmin.moviedb"
private const val tmdb = "http://themoviedb.org"

data class AboutState(
    val version: String = BuildConfig.VERSION_NAME,
    val applicationUrl: String = appUrl,
    val authorName: String = authName,
    val authorUrl: String = githubProfile,
    val repositoryUrl: String = repoUrl,
    val tmdbUrl: String = tmdb
)