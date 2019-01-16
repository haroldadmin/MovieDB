package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.MoviesListAdapter
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getNumberOfColumns
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.gone
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_library.*
import kotlin.math.roundToInt

class LibraryFragment : BaseFragment() {

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
                    mainViewModel.setProgressBarVisible(true)
                    getFavouriteMoves(mainViewModel.accountId)
                }

                if (watchListMoviesUpdate.value == null) {
                    mainViewModel.setProgressBarVisible(true)
                    getWatchlistedeMovies(mainViewModel.accountId)
                }

                favouriteMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                    mainViewModel.setProgressBarVisible(false)
                    if (favouriteMoviesAdapter.itemCount == 0) {
                        // The RecyclerView was previously empty so we need to change the constraints of watchlist header
                        changeConstraintsOfWatchlistHeader()
                    }
                    favouriteMoviesAdapter.submitList(newList)
                })

                watchListMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                    mainViewModel.setProgressBarVisible(false)
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

    /**
     * When the favourites recycler view is empty, its visibility is GONE. The watchlist header in this case is
     * constrained to the bottom of emptyViewFavourites. However, when items appear in the recycler view, then we need
     * to change this constraint because emptyViewFavourites will be View.GONE and watchlist header will need to be
     * constrained to the bottom of rvFavourites
     */
    private fun changeConstraintsOfWatchlistHeader() {
        val initialConstraintSet = ConstraintSet()
        val finalConstraintSet = ConstraintSet()
        with (libraryContainer) {
            initialConstraintSet.clone(this)
            finalConstraintSet.clone(this)
        }
        finalConstraintSet.connect(R.id.tvWatchlist, ConstraintSet.TOP, R.id.rvFavourites, ConstraintSet.BOTTOM)
        TransitionManager.beginDelayedTransition(libraryContainer)
        finalConstraintSet.applyTo(libraryContainer)
    }

}
