package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.MoviesListAdapter
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getNumberOfColumns
import kotlinx.android.synthetic.main.fragment_library.*
import kotlin.math.roundToInt

class LibraryFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var libraryViewModel: LibraryViewModel
    private lateinit var favouriteMoviesAdapter: MoviesListAdapter
    private lateinit var watchListedMoviesAdapter: MoviesListAdapter

    companion object {
        fun newInstance() = LibraryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        libraryViewModel = ViewModelProviders.of(this).get(LibraryViewModel::class.java)

        libraryViewModel.apply {

            if (favouriteMoviesUpdate.value == null) {
                getFavouriteMoves(TODO())
            }

            if (watchListMoviesUpdate.value == null) {
                getWatchlistedeMovies(TODO())
            }

            favouriteMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                favouriteMoviesAdapter.submitList(newList)
            })

            watchListMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                watchListedMoviesAdapter.submitList(newList)
            })

            isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
                mainViewModel.setProgressBarVisible(isLoading)
            })
        }

        mainViewModel.updateToolbarTitle("Library")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val columns = resources.getDimension(R.dimen.movie_grid_poster_width).getNumberOfColumns(context!!)
        val space = resources.getDimension(R.dimen.movie_grid_item_space)

        favouriteMoviesAdapter = MoviesListAdapter(Glide.with(this)) { id, transitionName, sharedView ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
        }

        watchListedMoviesAdapter = MoviesListAdapter(Glide.with(this)) { id, transitionName, sharedView ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
        }

        rvFavourites.apply {
            layoutManager = GridLayoutManager(context, columns)
            addItemDecoration(EqualSpaceGridItemDecoration(space.roundToInt()))
            adapter = favouriteMoviesAdapter
        }

        rvWatchlist.apply {
            layoutManager = GridLayoutManager(context, columns)
            addItemDecoration(EqualSpaceGridItemDecoration(space.roundToInt()))
            adapter = watchListedMoviesAdapter
        }
    }

}
