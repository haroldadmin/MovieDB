package com.kshitijchauhan.haroldadmin.moviedb.about

import android.os.Handler
import android.view.View
import com.airbnb.epoxy.TypedEpoxyController

class AboutEpoxyController(
    handler: Handler,
    val callbacks: AboutEpoxyControllerCallbacks
) :
    TypedEpoxyController<AboutState>(handler, handler) {

    interface AboutEpoxyControllerCallbacks {
        val onRateClick: View.OnClickListener
        val onAuthorClick: View.OnClickListener
        val onRepoClick: View.OnClickListener
        val onTmdbClick: View.OnClickListener
    }

    override fun buildModels(data: AboutState?) {
        println(data.toString())
        itemApp {
            id("app-model")
            state(data)
            rateClickListener(callbacks.onRateClick)
        }

        itemAuthor {
            id("author-model")
            state(data)
            authorClickListener(callbacks.onAuthorClick)
            repoClickListener(callbacks.onRepoClick)
        }

        itemTmdb {
            id("tmdb-model")
            state(data)
            tmdbClickListener(callbacks.onTmdbClick)
        }
    }

}