<h1>MovieDB</h1>
<img src="https://user-images.githubusercontent.com/24315306/52174146-ca69b580-27b5-11e9-96e3-376b49933c0f.png" width="25%"></img>

<a href='https://play.google.com/store/apps/details?id=com.kshitijchauhan.haroldadmin.moviedb&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width="25%"/></a>
----------------------------------------------------------------

MovieDB is a gorgeous client application for [TMDb](https://www.themoviedb.org) on Android, built using Kotlin.

## Architecture and Tech-stack

* Built on MVVM architecture pattern
* Uses [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/), specifically ViewModel, LiveData and Room.
* Heavily uses the [Epoxy Library](https://github.com/airbnb/epoxy/) from Airbnb
* Has a clean, gorgeous user interface with pretty animations, built using Android Transitions framework, and [Material Components for Android](https://github.com/material-components/material-components-android)
* Heavily uses [RxJava](https://github.com/ReactiveX/RxJava) for network calls, transformations, and database observation.
* Completely offline ready. MovieDB uses [Room](https://developer.android.com/topic/libraries/architecture/room) for managing a local SQLite database, which means that if you have seen some content already while you were online, you won't need an internet connection to see it again. Everything except movie trailers are cached.
* Uses [Retrofit](https://square.github.io/retrofit/) for making API calls.
* Uses [Glide](https://github.com/bumptech/glide) for image loading.
* Built on a Single-Activity Architecture. Every screen in the app is a fragment.

## Features
* Discover Top Rated and Popular movies on TMDb.
* Search for movies
* View movie details like release date, rating, overview, **movie trailer** and cast right inside the app.
* Supports login for TMDb accounts to manage Watchlist and Favourite movies
* View movies in theatres in your region.
* Works offline by caching data into a database.

## Screenshots
<img src="https://user-images.githubusercontent.com/24315306/52173451-106d4c00-27ab-11e9-895e-6b8a429c12c9.png" width="45%"></img> <img src="https://user-images.githubusercontent.com/24315306/52173453-14996980-27ab-11e9-966b-c71e293bc250.png" width="45%"></img> <img src="https://user-images.githubusercontent.com/24315306/52173454-182cf080-27ab-11e9-916c-c05e4a438980.png" width="45%"></img> <img src="https://user-images.githubusercontent.com/24315306/52173452-12cfa600-27ab-11e9-92de-6358d7532402.png" width="45%"></img> 


## Planned Features
* Notify the user when an unreleased movie in their watchlist is released.
* Get movie recommendations based on any given movie
* Safer networking calls by wrapping Retrofit responses into a Resource class to have Success and Failure response types.
* Migrate to [MVRx](https://github.com/airbnb/mvrx) at some point.