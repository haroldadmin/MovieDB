package com.kshitijchauhan.haroldadmin.moviedb.ui.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.LoadingTask
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getNumberOfColumns
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movie_details.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import kotlin.math.roundToInt


class MovieDetailsFragment : BaseFragment() {

    private val TASK_LOAD_MOVIE_DETAILS = "load-movie-details"
    private val TASK_LOAD_MOVIE_ACCOUNT_STATES = "load-account-states"
    private val TASK_TOGGLE_FAVOURITE = "mark-as-favourite"
    private val TASK_TOGGLE_WATCHLIST = "add-to-watchlist"
    private val TASK_LOAD_MOVIE_VIDEOS = "load-movie-videos"

    private val compositeDisposable = CompositeDisposable()

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModel {
        val movieId = arguments?.getInt(Constants.KEY_MOVIE_ID, -1)
        parametersOf(movieId)
    }

    private val mainViewModel: MainViewModel by sharedViewModel()

    private val glideRequestManager: RequestManager by inject("fragment-glide-request-manager") {
        parametersOf(this)
    }

    private val creditsAdapter: CreditsAdapter by inject {
        parametersOf(glideRequestManager)
    }

    private var movie: Movie? = null

    override val associatedUIState: UIState =
        UIState.DetailsScreenState(this.arguments?.getInt(Constants.KEY_MOVIE_ID) ?: -1)

    override fun notifyBottomNavManager() {
        mainViewModel.updateBottomNavManagerState(this.associatedUIState)
    }

    override fun updateToolbarTitle() {
        mainViewModel.updateToolbarTitle("Movie")
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateToolbarTitle()

        lifecycle.addObserver(ypvTrailer)

        val columns = resources.getDimension(R.dimen.cast_member_picture_size).getNumberOfColumns(view.context)
        val space = resources.getDimension(R.dimen.cast_member_picture_space)

        rvCredits.apply {
            layoutManager = GridLayoutManager(context, columns)
            adapter = creditsAdapter
            addItemDecoration(EqualSpaceGridItemDecoration(space.roundToInt()))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        movieDetailsViewModel.getMovieDetails(mainViewModel.isAuthenticated)
        mainViewModel.addLoadingTask(LoadingTask(TASK_LOAD_MOVIE_DETAILS, viewLifecycleOwner))

        movieDetailsViewModel.movie.observe(viewLifecycleOwner, Observer { movie ->
            log("Received movie update: $movie")
            updateView(movie)
            this.movie = movie
            mainViewModel.completeLoadingTask(TASK_LOAD_MOVIE_DETAILS, viewLifecycleOwner)
        })

        movieDetailsViewModel.cast.observe(viewLifecycleOwner, Observer { castList ->
            creditsAdapter.submitList(castList)
        })

        movieDetailsViewModel.message.observe(viewLifecycleOwner, Observer { message ->
            mainViewModel.showSnackbar(message)
        })
    }

    private fun updateView(movie: Movie) {
        if (this.movie == null) {
            mainViewModel.updateToolbarTitle(movie.title)
            glideRequestManager
                .load(movie.posterPath)
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

            glideRequestManager
                .asBitmap()
                .transition(BitmapTransitionOptions.withCrossFade())
                .load(movie.backdropPath)
                .into(ivBackdrop)

            tvTitle.text = movie.title
            chipMovieYear.text = SimpleDateFormat("yyyy").format(movie.releaseDate)
            chipMovieGenre.text = movie.genre
            chipMovieRating.text = String.format("%.2f", movie.voteAverage)
            tvDescription.text = movie.overview
            ypvTrailer.initialize({ initializedPlayer ->
                initializedPlayer.addListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady() {
                        super.onReady()
                        initializedPlayer.cueVideo(movie.trailerUrl, 0f)
                    }
                })
            }, true)
            handleAccountStates(movie.isWatchlisted, movie.isFavourited)
        } else {
            handleAccountStates(movie.isWatchlisted, movie.isFavourited)
        }
    }


    private fun handleAccountStates(isWatchlisted: Boolean?, isFavourited: Boolean?) {
        if (mainViewModel.isAuthenticated) {
                btToggleWatchlist.apply {
                    val watchlisted = isWatchlisted ?: false
                    setRemoveFromListState(watchlisted)
                    text = if (watchlisted) {
                        "Un-Watchlist"
                    } else {
                        "Add to Watchlist"
                    }
                    setOnClickListener {
                        movieDetailsViewModel.toggleMovieWatchlistStatus(mainViewModel.accountId)
                    }
                }
        } else {
            btToggleWatchlist.apply {
                setUnauthenticatedState(true)
                setOnClickListener {
                    mainViewModel.showSnackbar("You need to login to do that.")
                }
            }
        }

        if (mainViewModel.isAuthenticated) {
            btToggleFavourite.apply {
                val favourite = isFavourited ?: false
                setRemoveFromListState(favourite)
                text = if (favourite) {
                    "Un-Favourite"
                } else {
                    "Add to Favourites"
                }
                setOnClickListener {
                    movieDetailsViewModel.toggleMovieFavouriteStatus(mainViewModel.accountId)
                }
            }
        } else {
            btToggleFavourite.apply {
                setUnauthenticatedState(true)
                setOnClickListener {
                    mainViewModel.showSnackbar("You need to login to do that.")
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }
}
