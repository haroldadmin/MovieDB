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
import com.kshitijchauhan.haroldadmin.moviedb.utils.dpToPx
import com.kshitijchauhan.haroldadmin.moviedb.utils.getNumberOfColumns
import com.kshitijchauhan.haroldadmin.moviedb.utils.log
import kotlinx.android.synthetic.main.activity_main_alternate.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.math.roundToInt

class HomeFragment : BaseFragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var popularMoviesAdapter: PopularMoviesAdapter
    private lateinit var topRatedMoviesAdapter: PopularMoviesAdapter

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.getPopularMovies()
        homeViewModel.getTopRatedMovies()

        activity?.apply {
            (this as AppCompatActivity).mainCollapsingToolbarLayout?.title = getString(R.string.app_name)
        }

        homeViewModel.popularMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
            popularMoviesAdapter.updateList(newList)
        })

        homeViewModel.topRatedMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
            topRatedMoviesAdapter.updateList(newList)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        log("onCreateView")
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popularMoviesAdapter = PopularMoviesAdapter(emptyList(), Glide.with(this)) { id ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id))
        }
        topRatedMoviesAdapter = PopularMoviesAdapter(emptyList(), Glide.with(this)) { id ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id))
        }
        popularMoviesRecyclerView.apply {
            val columns = context.getNumberOfColumns(100)
            val space = context.dpToPx(1f)
            layoutManager = GridLayoutManager(context, columns)
            addItemDecoration(GridItemDecoration(space.roundToInt()))
            adapter = popularMoviesAdapter
        }
        topRatedMoviesRecyclerView.apply {
            val columns = context.getNumberOfColumns(100)
            val space = context.dpToPx(1f)
            layoutManager = GridLayoutManager(context, columns)
            addItemDecoration(GridItemDecoration(space.roundToInt()))
            adapter = topRatedMoviesAdapter
        }
    }
}
