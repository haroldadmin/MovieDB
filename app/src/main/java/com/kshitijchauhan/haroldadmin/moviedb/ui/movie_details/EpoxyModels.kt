package com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.AccountState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.CustomMaterialButton
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.KotlinEpoxyHolder
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener

@EpoxyModelClass(layout = R.layout.view_main_text)
abstract class MainTextModel : EpoxyModelWithHolder<MainTextModel.MainTextViewHolder>() {

    @EpoxyAttribute
    lateinit var text: String

    override fun bind(holder: MainTextViewHolder) {
        super.bind(holder)
        holder.mainText.text = text
    }

    inner class MainTextViewHolder : KotlinEpoxyHolder() {
        val mainText by bind<TextView>(R.id.tvMainText)
    }
}

@EpoxyModelClass(layout = R.layout.view_youtube_player)
abstract class TrailerModel : EpoxyModelWithHolder<TrailerModel.TrailerHolder>() {

    @EpoxyAttribute
    lateinit var trailerKey: String

    override fun bind(holder: TrailerHolder) {
        super.bind(holder)
        if (holder.isInitialized) {
            holder.youTubePlayer?.cueVideo(trailerKey, 0f)
        } else {
            holder.cueVideo(trailerKey)
        }
    }

    override fun unbind(holder: TrailerHolder) {
        super.unbind(holder)
        // We should not release the player here, otherwise subsequent video requests will be not loaded
        holder.youTubePlayer?.pause()
    }

    /**
     * To create this view holder for Epoxy, we are using [EpoxyHolder] instead of [KotlinEpoxyHolder] because we need
     * to initialize the youtube player view. For this, we need a reference to the inflated view object this holder is
     * working with. [KotlinEpoxyHolder] does not provide this view object.
     */
    inner class TrailerHolder : EpoxyHolder() {
        // Actual view inside the view hierarchy
        private var youTubePlayerView: YouTubePlayerView? = null

        // Youtube player backing the player view
        var youTubePlayer: YouTubePlayer? = null

        // Used to provide access to outside classes to the initialized status of the youtube player
        var isInitialized = false

        // If the youtube player is uninitialized when binding to it, we can store the video key in this variable.
        // The youtube player view will cue this video upon initialization.
        var videoKey: String = ""

        fun cueVideo(key: String) {
            videoKey = key
        }

        override fun bindView(itemView: View) {
            youTubePlayerView = itemView.findViewById<YouTubePlayerView>(R.id.movieTrailer)
                .apply {
                    initialize(
                        { initializedPlayer ->
                            initializedPlayer.addListener(object : AbstractYouTubePlayerListener() {
                                override fun onReady() {
                                    super.onReady()
                                    youTubePlayer = initializedPlayer
                                    isInitialized = true
                                    if (videoKey != "") {
                                        initializedPlayer.cueVideo(videoKey, 0f)
                                    }
                                }
                            })
                        },
                        true
                    )
                }
        }

    }
}

@EpoxyModelClass(layout = R.layout.view_movie_info_bar)
abstract class InfoBarModel : EpoxyModelWithHolder<InfoBarModel.InfoBarHolder>() {

    @EpoxyAttribute
    lateinit var accountStates: AccountState

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var watchListClickListener: View.OnClickListener

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var favouritesClickListener: View.OnClickListener

    override fun bind(holder: InfoBarHolder) {
        super.bind(holder)

        accountStates.isWatchlisted?.let { watchlisted ->
            holder.watchlistButton.apply {
                setRemoveFromListState(watchlisted)
                text = if (watchlisted) "Un-Watchlist" else "Add to Watchlist"
                setOnClickListener(watchListClickListener)
            }
        } ?: holder.watchlistButton.apply {
            setUnauthenticatedState(true)
            setOnClickListener(watchListClickListener)
        }

        accountStates.isFavourited?.let { favourite ->
            holder.favouriteButton.apply {
                setRemoveFromListState(favourite)
                text = if (favourite) "Un-Favourite" else "Add to Favourites"
                setOnClickListener(favouritesClickListener)
            }
        } ?: holder.favouriteButton.apply {
            setUnauthenticatedState(true)
            setOnClickListener(favouritesClickListener)
        }
    }

    override fun unbind(holder: InfoBarHolder) {
        super.unbind(holder)
        holder.favouriteButton.apply {
            setRemoveFromListState(false)
            setUnauthenticatedState(true)
        }
        holder.watchlistButton.apply {
            setRemoveFromListState(false)
            setUnauthenticatedState(true)
        }
    }

    inner class InfoBarHolder : KotlinEpoxyHolder() {
        val favouriteButton by bind<CustomMaterialButton>(R.id.btToggleFavourite)
        val watchlistButton by bind<CustomMaterialButton>(R.id.btToggleWatchlist)
    }
}