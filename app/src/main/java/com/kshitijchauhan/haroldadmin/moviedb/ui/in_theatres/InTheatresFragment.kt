package com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding2.widget.RxSearchView
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.EpoxyCallbacks
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.LoadingTask
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getNumberOfColumns
import com.mikepenz.itemanimators.AlphaInAnimator
import kotlinx.android.synthetic.main.fragment_in_theatres.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class InTheatresFragment : BaseFragment() {

    private val TASK_LOAD_IN_THEATRES_MOVIES = "load-in-theatres-movies"

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val inTheatresViewModel: InTheatresViewModel by viewModel()

    override val associatedUIState: UIState = UIState.InTheatresScreenState

    private val callbacks = object: EpoxyCallbacks {
        override fun onMovieItemClicked(id: Int, transitionName: String, sharedView: View?) {
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
        }
    }

    private val inTheatresEpoxyController = InTheatresEpoxyController(callbacks)

    override fun notifyBottomNavManager() {
        mainViewModel.updateBottomNavManagerState(this.associatedUIState)
    }

    override fun updateToolbarTitle() {
        mainViewModel.updateToolbarTitle("In Theatres")
    }

    companion object {
        fun newInstance() = InTheatresFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_in_theatres, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inTheatresEpoxyController.setData(null)
        inTheatresViewModel.apply {
            mainViewModel.addLoadingTask(LoadingTask(TASK_LOAD_IN_THEATRES_MOVIES, viewLifecycleOwner))
            getPopularMovies()

            moviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                inTheatresEpoxyController.setData(newList)
                mainViewModel.completeLoadingTask(TASK_LOAD_IN_THEATRES_MOVIES, viewLifecycleOwner)
            })

            message.observe(viewLifecycleOwner, Observer { message ->
                mainViewModel.showSnackbar(message)
            })

            forceRefreshInTheatresCollection()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateToolbarTitle()
        rvMovies.apply {
            val columns = resources.getDimension(R.dimen.movie_grid_poster_width).getNumberOfColumns(context!!)
            val space = resources.getDimension(R.dimen.movie_grid_item_space)
            layoutManager = GridLayoutManager(context, columns)
            itemAnimator = AlphaInAnimator()
            addItemDecoration(EqualSpaceGridItemDecoration(space.roundToInt()))
            setController(inTheatresEpoxyController)
        }
    }
}
