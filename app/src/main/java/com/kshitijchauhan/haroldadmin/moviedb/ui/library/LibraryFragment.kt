package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
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
import kotlin.math.roundToInt

class LibraryFragment : BaseFragment() {

    private val TASK_LOAD_FAVOURITE_MOVIES = "load-favourite-movies"
    private val TASK_LOAD_WATCHLISTED_MOVIES = "load-watchlisted-movies"

    private lateinit var mainViewModel: MainViewModel
    private lateinit var libraryViewModel: LibraryViewModel
    private lateinit var favouriteMoviesAdapter: MoviesListAdapter
    private lateinit var watchListedMoviesAdapter: MoviesListAdapter

    override val associatedUIState: UIState = UIState.LibraryScreenState

    override fun notifyBottomNavManager() {
        mainViewModel.bottomNavManager.setBottomNavActiveState(this.associatedUIState)
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
        setupRecyclerViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        libraryViewModel = ViewModelProviders.of(this).get(LibraryViewModel::class.java)

        if (mainViewModel.isAuthenticated) {
            libraryViewModel.apply {

                if (favouriteMoviesUpdate.value == null) {
                    mainViewModel.addLoadingTask(LoadingTask(TASK_LOAD_FAVOURITE_MOVIES, viewLifecycleOwner))
                    getFavouriteMoves(mainViewModel.accountId)
                }

                if (watchListMoviesUpdate.value == null) {
                    mainViewModel.addLoadingTask(LoadingTask(TASK_LOAD_WATCHLISTED_MOVIES, viewLifecycleOwner))
                    getWatchlistedeMovies(mainViewModel.accountId)
                }

                favouriteMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                    mainViewModel.completeLoadingTask(TASK_LOAD_FAVOURITE_MOVIES, viewLifecycleOwner)
                    /**
                     * If the adapter was unpopulated before, the empty view will be removed, the recycler view's new
                     * items will appear. If the new items are not empty, then the watchlist recycler view and its header
                     * will have to be shifted down. We want to animate those changes.
                      */
                    val isListEmpty = favouriteMoviesAdapter.itemCount == 0
                    if (isListEmpty) TransitionManager.beginDelayedTransition(libraryContainer)
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

        favouriteMoviesAdapter = MoviesListAdapter(Glide.with(this)) { id, transitionName, sharedView ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
        }

        watchListedMoviesAdapter = MoviesListAdapter(Glide.with(this)) { id, transitionName, sharedView ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
        }

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
