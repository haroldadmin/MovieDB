package com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details

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
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.format
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getNumberOfColumns
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe
import com.kshitijchauhan.haroldadmin.mvrxlite.base.MVRxLiteView
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movie_details.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MovieDetailsFragment : BaseFragment(), MVRxLiteView<UIState.DetailsScreenState> {

    private val mainViewModel: MainViewModel by sharedViewModel()

    private val callbacks = object : DetailsEpoxyController.MovieDetailsCallbacks {
        override fun toggleMovieFavouriteStatus() {
            if (mainViewModel.isAuthenticated) {
                movieDetailsViewModel.toggleMovieFavouriteStatus(mainViewModel.accountId)
            } else {
                mainViewModel.showSnackbar(getString(R.string.message_need_to_login))
            }
        }

        override fun toggleMovieWatchlistStatus() {
            if (mainViewModel.isAuthenticated) {
                movieDetailsViewModel.toggleMovieWatchlistStatus(mainViewModel.accountId)
            } else {
                mainViewModel.showSnackbar(getString(R.string.message_need_to_login))
            }
        }

        override fun onActorItemClicked(id: Int, transitionName: String, sharedView: View?) {
            mainViewModel.updateStateTo(UIState.ActorDetailsScreenState(
                id,
                transitionName,
                sharedView,
                Resource.Loading()
            ))
        }
    }

    private val glideRequestManager: RequestManager by inject("fragment-glide-request-manager") {
        parametersOf(this)
    }

    private val detailsEpoxyController: DetailsEpoxyController by inject {
        parametersOf(callbacks, glideRequestManager)
    }

    // We will treat this as initial state for now
    // TODO Remove this and move to a navigator based system
    override val associatedUIState: UIState by lazy {
        UIState.DetailsScreenState(
            movieId = this.arguments?.getInt(Constants.KEY_MOVIE_ID) ?: -1,
            accountStatesResource = Resource.Loading(),
            movieResource = Resource.Loading(),
            trailerResource = Resource.Loading(),
            castResource = listOf(Resource.Loading())
        )
    }

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModel {
        val isAuthenticated = mainViewModel.isAuthenticated
        val movieId = arguments?.getInt(Constants.KEY_MOVIE_ID, -1)
        parametersOf(isAuthenticated, movieId, associatedUIState)
    }

    override fun notifyBottomNavManager() {
        // TODO make this a part of render state
        mainViewModel.updateBottomNavManagerState(this.associatedUIState)
    }

    override fun updateToolbarTitle() {
        // TODO make this a part of render state
        // The title will be updated when the movie details are retrieved
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()
        inflater.inflate(R.layout.fragment_movie_details, container, false)
            .apply {
                ViewCompat.setTransitionName(this.ivPoster, arguments?.getString(Constants.KEY_TRANSITION_NAME))
            }
            .also {
                return it
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateToolbarTitle()
        rvMovieDetails.apply {
            val columns = resources.getDimension(R.dimen.cast_member_picture_size).getNumberOfColumns(view.context)
            layoutManager = GridLayoutManager(context, columns)
            setController(detailsEpoxyController)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(movieDetailsViewModel) {
            getAllMovieInfo()
            message.observe(viewLifecycleOwner, Observer { message ->
                mainViewModel.showSnackbar(message)
            })
            state.observe(viewLifecycleOwner, Observer { state ->
                renderState(state)
            })
        }
    }

    override fun renderState(state: UIState.DetailsScreenState) {
        detailsEpoxyController.setData(state)
        when (val movieResource = state.movieResource) {
            is Resource.Success -> {
                val movie = movieResource.data
                mainViewModel.updateToolbarTitle(movie.title)
                glideRequestManager
                    .load(movie.posterPath)
                    .apply {
                        RequestOptions()
                            .placeholder(R.drawable.ic_round_local_movies_24px)
                            .error(R.drawable.ic_round_local_movies_24px)
                            .fallback(R.drawable.ic_round_local_movies_24px)
                    }
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
                chipMovieYear.text = movie.releaseDate.format("yyyy")
                chipMovieGenre.text = movie.genres?.first() ?: "..."
                chipMovieRating.text = movie.voteAverage.format("%.2f")
            }
            else -> Unit
        }.safe
    }
}
