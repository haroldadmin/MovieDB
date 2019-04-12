package com.kshitijchauhan.haroldadmin.moviedb.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.actor_details.ActorDetailsEpoxyController
import com.kshitijchauhan.haroldadmin.moviedb.ui.actor_details.ActorDetailsViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.auth.AuthenticationViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.EpoxyCallbacks
import com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres.InTheatresEpoxyController
import com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres.InTheatresViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.library.LibraryEpoxyController
import com.kshitijchauhan.haroldadmin.moviedb.ui.library.LibraryViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.HomeEpoxyController
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.HomeViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details.DetailsEpoxyController
import com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details.MovieDetailsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val applicationModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            androidContext().getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }
}

val uiModule = module {

    viewModel { (initialState: UIState.HomeScreenState) ->
        HomeViewModel(get(), get(), initialState)
    }

    viewModel { (accountId: Int, initialState: UIState.LibraryScreenState) ->
        LibraryViewModel(get(), accountId, initialState)
    }

    viewModel { (initialState: UIState.InTheatresScreenState) ->
        InTheatresViewModel(get(), initialState)
    }

    viewModel { AuthenticationViewModel(get(), get(), get()) }
    viewModel { (isAuthenticated: Boolean, movieId: Int, initialState: UIState.DetailsScreenState) ->
        MovieDetailsViewModel(isAuthenticated, movieId, get(), initialState)
    }
    viewModel { MainViewModel(get()) }

    viewModel { (actorId: Int, initialState: UIState.ActorDetailsScreenState) ->
        ActorDetailsViewModel(actorId, get(), initialState)
    }

    factory(named("fragment-glide-request-manager")) { (fragment: Fragment) ->
        Glide.with(fragment)
    }

    factory(named("view-glide-request-manager")) { (view: View) ->
        Glide.with(view)
    }

    factory { (callbacks: DetailsEpoxyController.MovieDetailsCallbacks, glide: RequestManager) ->
        DetailsEpoxyController(callbacks, glide, get<Handler>(named("epoxy-handler")))
    }

    factory { (callbacks: EpoxyCallbacks, glide: RequestManager) ->
        HomeEpoxyController(callbacks, glide, get(named("epoxy-handler")))
    }

    factory { (callbacks: EpoxyCallbacks, glide: RequestManager) ->
        LibraryEpoxyController(callbacks, glide, get(named("epoxy-handler")))
    }

    factory { (callbacks: EpoxyCallbacks, glide: RequestManager) ->
        InTheatresEpoxyController(callbacks, glide, get(named("epoxy-handler")))
    }

    factory { ActorDetailsEpoxyController(get(named("epoxy-handler"))) }

    single(named("epoxy-handler-thread")) {
        HandlerThread("epoxy").apply {
            start()
        }
    }

    single(named("epoxy-handler")) {
        val handlerThread = get<HandlerThread>(named("epoxy-handler-thread"))
        Handler(handlerThread.looper)
    }
}


