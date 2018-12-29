package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.gone
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_main_alternate.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.math.roundToInt

class HomeFragment : BaseFragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var topRatedMoviesAdapter: MoviesAdapter

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModels()

        homeViewModel.apply {

            if (popularMoviesUpdate.value == null) {
                getPopularMovies()
            }

            if (topRatedMoviesUpdate.value == null) {
                getTopRatedMovies()
            }

            isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
                if (isLoading) progressBar.visible() else progressBar.gone()
            })

            popularMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                popularMoviesAdapter.updateList(newList)
            })

            topRatedMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                topRatedMoviesAdapter.updateList(newList)
            })
        }

        activity?.apply {
            mainCollapsingToolbarLayout?.title = getString(R.string.app_name)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val columns = getNumberOfColumns(resources.getDimension(R.dimen.movie_grid_poster_width))
        val space = resources.getDimension(R.dimen.movie_grid_item_space)

        popularMoviesAdapter = MoviesAdapter(emptyList(), Glide.with(this)) { id, transitionName, sharedView ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
        }

        topRatedMoviesAdapter = MoviesAdapter(emptyList(), Glide.with(this)) { id, transitionName, sharedView ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
        }

        popularMoviesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, columns)
            addItemDecoration(EqualSpaceGridItemDecoration((space).roundToInt()))
            adapter = popularMoviesAdapter
        }

        topRatedMoviesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, columns)
            addItemDecoration(EqualSpaceGridItemDecoration((space).roundToInt()))
            adapter = topRatedMoviesAdapter
        }
    }

    private fun initViewModels() {
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    private fun getNumberOfColumns(itemWidthPx: Float): Int {
        val screenWidth = resources.displayMetrics.widthPixels
        return screenWidth.div(itemWidthPx).toInt()
    }
}
