package com.kshitijchauhan.haroldadmin.moviedb.ui.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.AddMediaToWatchlistRequest
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.MarkMediaAsFavoriteRequest
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.MediaTypes
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disabled
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movie_details.view.*


class MovieDetailsFragment : BaseFragment() {

    private val compositeDisposable = CompositeDisposable()

    override val associatedUIState: UIState = UIState.DetailsScreenState(this.arguments?.getInt(Constants.KEY_MOVIE_ID) ?: -1)

    override fun notifyBottomNavManager() {
        mainViewModel.bottomNavManager.setBottomNavActiveState(this.associatedUIState)
    }


    companion object {
        fun newInstance(movieId: Int, transitionName: String): MovieDetailsFragment {
            val newInstance = MovieDetailsFragment()
            newInstance.arguments = Bundle()
                .apply {
                    putInt(Constants.KEY_MOVIE_ID, movieId)
                    putString(Constants.KEY_TRANSITION_NAME, transitionName)
                }
            return newInstance
        }
    }

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        ViewCompat.setTransitionName(view.ivPoster, arguments?.getString(Constants.KEY_TRANSITION_NAME))
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        movieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)

        arguments?.getInt(Constants.KEY_MOVIE_ID)?.let { id ->
            movieDetailsViewModel.getMovieDetails(id)
        }

        movieDetailsViewModel.movieDetails.observe(viewLifecycleOwner, Observer { movie ->
            updateView(movie)
        })

        movieDetailsViewModel.markAsFavoriteSuccess.observe(viewLifecycleOwner, Observer { isSuccessful ->
            if (isSuccessful) {
                // TODO Change button state to red instead of disabling it
                // TODO Add ability to remove from favourites
                btMarkAsFavourite.text = "Favourited"
                btMarkAsFavourite.disabled()
                mainViewModel.setProgressBarVisible(false)
            }
        })

        movieDetailsViewModel.addToWatchlistSuccess.observe(viewLifecycleOwner, Observer { isSuccessful ->
            // TODO Change button state to red instead of disabling it
            // TODO Add ability to remove from watchlist
            btAddToWatchlist.text = "Watchlisted"
            btAddToWatchlist.disabled()
            mainViewModel.setProgressBarVisible(false)
        })
    }

    private fun updateView(movie: Movie) {

        mainViewModel.updateToolbarTitle(movie.title)

        Glide.with(this)
            .load(movie.posterPath)
            .apply(
                RequestOptions()
                    .centerCrop()
            )
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

            })
            .into(ivPoster)

        Glide.with(this)
            .asBitmap()
            .transition(BitmapTransitionOptions.withCrossFade())
            .apply(
                RequestOptions()
                    .centerCrop()
            )
            .load(movie.backdropPath)
            .into(ivBackdrop)

        tvTitle.text = movie.title
        chipMovieYear.text = movie.releaseDate
        chipMovieGenre.text = movie.genres[0].name
        chipMovieRating.text = String.format("%.2f", movie.voteAverage)
        tvDescription.text = movie.overview
        btMarkAsFavourite.setOnClickListener {
            val request = MarkMediaAsFavoriteRequest(
                MediaTypes.MOVIE.mediaName,
                movie.id,
                true
            )
            movieDetailsViewModel.markMovieAsFavorite(mainViewModel.accountId, request)
            mainViewModel.setProgressBarVisible(true)
            btMarkAsFavourite.setRemoveFromListState(true)
        }
        btAddToWatchlist.setOnClickListener {
            val request=  AddMediaToWatchlistRequest(
                MediaTypes.MOVIE.mediaName,
                movie.id,
                true
            )
            movieDetailsViewModel.addMovieToWatchList(mainViewModel.accountId, request)
            mainViewModel.setProgressBarVisible(true)
            btAddToWatchlist.setRemoveFromListState(true)
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

}
