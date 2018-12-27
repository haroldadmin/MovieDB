package com.kshitijchauhan.haroldadmin.moviedb.ui.discover


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat.animate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.R.id.rvMovies
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.gone
import com.kshitijchauhan.haroldadmin.moviedb.utils.postDelayed
import com.kshitijchauhan.haroldadmin.moviedb.utils.visible
import com.mikepenz.itemanimators.AlphaInAnimator
import kotlinx.android.synthetic.main.fragment_discover.*

class DiscoverFragment : BaseFragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var discoverViewModel: DiscoverViewModel
    private var moviesAdapter: MoviesAdapter? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    companion object {
        fun newInstance() = DiscoverFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        discoverViewModel = ViewModelProviders.of(this).get(DiscoverViewModel::class.java)

        discoverViewModel.apply {

            mainHandler.postDelayed(500) {
                getPopularMovies()
            }

            discoverViewModel.moviesUpdate.observe(viewLifecycleOwner, Observer {
                println("Updating list on discover adapter")
                moviesAdapter?.updateList(it)
            })

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(context)
        moviesAdapter = MoviesAdapter(Glide.with(this), mutableListOf()) { id ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id))
        }
        rvMovies.apply {
            layoutManager = linearLayoutManager
            adapter = moviesAdapter
            itemAnimator = AlphaInAnimator()
        }
    }

    override fun onDestroyView() {
        moviesAdapter = null
        super.onDestroyView()
    }
}
