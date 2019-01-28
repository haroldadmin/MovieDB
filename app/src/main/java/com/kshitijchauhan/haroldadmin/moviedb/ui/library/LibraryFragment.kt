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
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.MoviesListAdapter
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.LoadingTask
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getNumberOfColumns
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.gone
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_library.*
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

    private val glideRequestManager: RequestManager by inject {
        parametersOf(this)
    }
    private val favouriteMoviesAdapter: MoviesListAdapter by inject {
        parametersOf(glideRequestManager, { id: Int, transitionName: String, sharedView: View ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
        })
    }
    private val watchListedMoviesAdapter: MoviesListAdapter by inject {
        parametersOf(glideRequestManager, { id: Int, transitionName: String, sharedView: View ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
        })
    }

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
        setupRecyclerViews()
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
                    /**
                     * If the adapter was unpopulated before, the empty view will be removed, the recycler view's new
                     * items will appear. If the new items are not empty, then the watchlist recycler view and its header
                     * will have to be shifted down. We want to animate those changes.
                     */
                    val isAdapterEmpty = favouriteMoviesAdapter.itemCount == 0
                    if (isAdapterEmpty)
                        TransitionManager.beginDelayedTransition(libraryContainer)
                    favouriteMoviesAdapter.submitList(newList)
                })

                watchListMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                    mainViewModel.completeLoadingTask(TASK_LOAD_WATCHLISTED_MOVIES, viewLifecycleOwner)
                    watchListedMoviesAdapter.submitList(newList)
                })
            }
        }

        mainViewModel.updateToolbarTitle("Library")
    }

    override fun onStart() {
        super.onStart()
        if (mainViewModel.isAuthenticated) {
            TransitionManager.beginDelayedTransition(libraryContainer)
            infoGroup.gone()
            moviesGroup.visible()
        } else {
            TransitionManager.beginDelayedTransition(libraryContainer)
            moviesGroup.gone()
            infoGroup.visible()
        }
    }

    private fun setupRecyclerViews() {
        val columns = resources.getDimension(R.dimen.movie_grid_poster_width).getNumberOfColumns(context!!)
        val space = resources.getDimension(R.dimen.movie_grid_item_space)

        rvFavourites.apply {
            setEmptyView(emptyViewFavourites)
            layoutManager = GridLayoutManager(context, columns)
            addItemDecoration(EqualSpaceGridItemDecoration(space.roundToInt()))
            adapter = favouriteMoviesAdapter
        }

        rvWatchlist.apply {
            setEmptyView(emptyViewWatchlist)
            layoutManager = GridLayoutManager(context, columns)
            addItemDecoration(EqualSpaceGridItemDecoration(space.roundToInt()))
            adapter = watchListedMoviesAdapter
        }
    }
}
