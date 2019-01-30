package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionManager
import com.bumptech.glide.RequestManager
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionType
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.MoviesListAdapter
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.LoadingTask
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getNumberOfColumns
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.gone
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.visible
import com.mikepenz.itemanimators.AlphaInAnimator
import com.mikepenz.itemanimators.SlideDownAlphaAnimator
import kotlinx.android.synthetic.main.fragment_library.*
import kotlinx.android.synthetic.main.fragment_logged_out.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

class LibraryFragment : BaseFragment() {

    private val TASK_LOAD_FAVOURITE_MOVIES = "load-favourite-movies"
    private val TASK_LOAD_WATCHLISTED_MOVIES = "load-watchlisted-movies"

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val libraryViewModel: LibraryViewModel by viewModel()

    private var favouritesList = listOf<Movie>()
    private var watchlist = listOf<Movie>()


    private val callbacks = object: AllMoviesController.Callbacks {
        override fun onMovieItemClicked(id: Int, transitionName: String, sharedView: View?) {
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
        }
    }

    private val moviesController = AllMoviesController(callbacks)

    override val associatedUIState: UIState = UIState.LibraryScreenState

    override fun notifyBottomNavManager() {
        mainViewModel.updateBottomNavManagerState(this.associatedUIState)
    }

    override fun updateToolbarTitle() {
        mainViewModel.updateToolbarTitle("Library")
    }

    companion object {
        fun newInstance() = LibraryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateToolbarTitle()
        moviesController.setData(favouritesList, watchlist, mainViewModel.isAuthenticated)
        epoxyRv.apply {
            val columns = resources.getDimension(R.dimen.movie_grid_poster_width).getNumberOfColumns(context!!)
            val space = resources.getDimension(R.dimen.movie_grid_item_space)
            layoutManager = GridLayoutManager(context, columns)
            itemAnimator = AlphaInAnimator()
            setController(moviesController)
            addItemDecoration(EqualSpaceGridItemDecoration(space.roundToInt()))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        libraryViewModel.message.observe(viewLifecycleOwner, Observer { message ->
            mainViewModel.showSnackbar(message)
        })

        if (mainViewModel.isAuthenticated) {
            libraryViewModel.apply {

                if (favouriteMovies.value == null) {
                    mainViewModel.addLoadingTask(LoadingTask(TASK_LOAD_FAVOURITE_MOVIES, viewLifecycleOwner))
                    getFavouriteMovies(mainViewModel.accountId)
                }

                if (watchListMoviesUpdate.value == null) {
                    mainViewModel.addLoadingTask(LoadingTask(TASK_LOAD_WATCHLISTED_MOVIES, viewLifecycleOwner))
                    getWatchlistedMovies(mainViewModel.accountId)
                }

                favouriteMovies.observe(viewLifecycleOwner, Observer { newList ->
                    mainViewModel.completeLoadingTask(TASK_LOAD_FAVOURITE_MOVIES, viewLifecycleOwner)
                    favouritesList = newList
                    moviesController.setData(favouritesList, watchlist, true)
                })

                watchListMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                    mainViewModel.completeLoadingTask(TASK_LOAD_WATCHLISTED_MOVIES, viewLifecycleOwner)
                    watchlist = newList
                    moviesController.setData(favouritesList, watchlist, true)
                })

                forceRefreshCollection(mainViewModel.accountId, CollectionType.Favourite)
                forceRefreshCollection(mainViewModel.accountId, CollectionType.Watchlist)
            }
        }

        mainViewModel.updateToolbarTitle("Library")
    }
}
