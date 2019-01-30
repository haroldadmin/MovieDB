package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionType
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.EpoxyCallbacks
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.LoadingTask
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getNumberOfColumns
import com.mikepenz.itemanimators.AlphaInAnimator
import kotlinx.android.synthetic.main.fragment_library.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class LibraryFragment : BaseFragment() {

    private val TASK_LOAD_FAVOURITE_MOVIES = "load-favourite-movies"
    private val TASK_LOAD_WATCHLISTED_MOVIES = "load-watchlisted-movies"

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val libraryViewModel: LibraryViewModel by viewModel()

    private val callbacks = object : EpoxyCallbacks {
        override fun onMovieItemClicked(id: Int, transitionName: String, sharedView: View?) {
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
        }
    }

    private val libraryEpoxyController = LibraryEpoxyController(callbacks)

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
        rvLibrary.apply {
            val columns = resources.getDimension(R.dimen.movie_grid_poster_width).getNumberOfColumns(context!!)
            val space = resources.getDimension(R.dimen.movie_grid_item_space)
            layoutManager = GridLayoutManager(context, columns)
            itemAnimator = AlphaInAnimator()
            addItemDecoration(EqualSpaceGridItemDecoration(space.roundToInt()))
            setController(libraryEpoxyController)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        libraryEpoxyController.setData(null, null, mainViewModel.isAuthenticated)

        libraryViewModel.message.observe(viewLifecycleOwner, Observer { message ->
            mainViewModel.showSnackbar(message)
        })

        if (mainViewModel.isAuthenticated) {
            libraryViewModel.apply {

                if (favouriteMovies.value == null) {
                    mainViewModel.addLoadingTask(LoadingTask(TASK_LOAD_FAVOURITE_MOVIES, viewLifecycleOwner))
                    getFavouriteMovies(mainViewModel.accountId)
                }

                if (watchListMovies.value == null) {
                    mainViewModel.addLoadingTask(LoadingTask(TASK_LOAD_WATCHLISTED_MOVIES, viewLifecycleOwner))
                    getWatchlistedMovies(mainViewModel.accountId)
                }

                favouriteMovies.observe(viewLifecycleOwner, Observer { newList ->
                    mainViewModel.completeLoadingTask(TASK_LOAD_FAVOURITE_MOVIES, viewLifecycleOwner)
                    libraryEpoxyController.setData(newList, watchListMovies.value, true)
                })

                watchListMovies.observe(viewLifecycleOwner, Observer { newList ->
                    mainViewModel.completeLoadingTask(TASK_LOAD_WATCHLISTED_MOVIES, viewLifecycleOwner)
                    libraryEpoxyController.setData(favouriteMovies.value, newList, true)
                })

                forceRefreshCollection(mainViewModel.accountId, CollectionType.Favourite)
                forceRefreshCollection(mainViewModel.accountId, CollectionType.Watchlist)
            }
        }
    }
}
