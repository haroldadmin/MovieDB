package com.kshitijchauhan.haroldadmin.moviedb.repository.remote.service.discover



object SortParameters {

    const val PopularityAsc = "popularity.asc"
    const val PopularityDsc = "popularity.dsc"
    const val Oldest = "release_date.asc"
    const val Newest = "release_date.dsc"
    const val LowestEarning = "revenue.asc"
    const val HighestEarning = "revenue.dsc"
    const val NameAsc = "original_title.asc"
    const val NameDsc = "original_title.dsc"
    const val HighestRated = "vote_average.dsc"
    const val LowestRated = "vote_average.asc"
}