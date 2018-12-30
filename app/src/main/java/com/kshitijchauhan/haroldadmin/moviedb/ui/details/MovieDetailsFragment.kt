package com.kshitijchauhan.haroldadmin.moviedb.ui.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main_alternate.*
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movie_details.view.*


class MovieDetailsFragment : BaseFragment() {

    private val compositeDisposable = CompositeDisposable()

    companion object {
        fun newInstance(movieId: Int, transitionName: String): MovieDetailsFragment {
            val newInstance = MovieDetailsFragment()
            newInstance.arguments = Bundle()
                .apply {
                    putInt(Constants.KEY_MOVIE_ID, movieId)
                    putString(Constants.KEY_TRANSITION_NAME, transitionName)
                }
            return newInstance
        }
    }

    private lateinit var viewModel: MovieDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        ViewCompat.setTransitionName(view.ivPoster, arguments?.getString(Constants.KEY_TRANSITION_NAME))
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)
        arguments?.getInt(Constants.KEY_MOVIE_ID)?.let { id ->
            viewModel.getMovieDetails(id)
        }

        viewModel.movieDetails.observe(viewLifecycleOwner, Observer { movie ->
            updateView(movie)
        })
    }

    private fun updateView(movie: Movie) {

        (activity as AppCompatActivity)
            .mainToolbar
            .title = movie.title

        Glide.with(this)
            .load(movie.posterPath)
            .apply(
                RequestOptions()
                    .centerCrop()
            )
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

            })
            .into(ivPoster)

        Glide.with(this)
            .asBitmap()
            .transition(BitmapTransitionOptions.withCrossFade())
            .apply(
                RequestOptions()
                    .centerCrop()
            )
            .load(movie.backdropPath)
            .into(ivBackdrop)

        tvTitle.text = movie.title
        chipMovieYear.text = movie.releaseDate
        chipMovieGenre.text = movie.genres[0].name
        chipMovieRating.text = String.format("%.2f", movie.voteAverage)
        tvDescription.text = movie.overview
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

}
