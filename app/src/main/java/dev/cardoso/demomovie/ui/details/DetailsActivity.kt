package dev.cardoso.demomovie.ui.details

import dev.cardoso.demomovie.model.Result
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import dev.cardoso.demomovie.Config
import dev.cardoso.demomovie.R
import dev.cardoso.demomovie.databinding.ActivityDetailsBinding
import com.google.android.material.snackbar.Snackbar
import dev.cardoso.demomovie.model.MovieDesc



/**
 * Shows detailed information about movie/show
 */

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val   viewModel   by viewModels<DetailsViewModel>()
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent?.getIntExtra(EXTRAS_MOVIE_ID, 0)?.let { id ->
            viewModel.getMovieDetail(id)
            subscribeUi()
        } ?: showError("Unknown Movie")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun subscribeUi() {
        viewModel.movie.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let {
                        updateUi(it)
                    }
                    binding.loading.visibility = View.GONE
                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showError(it)
                    }
                    binding.loading.visibility = View.GONE
                }

                Result.Status.LOADING -> {
                    binding.loading.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun showError(msg: String) {
        Snackbar.make(binding.vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    private fun updateUi(movie: MovieDesc) {
        title = movie.title
        binding.tvTitle.text = movie.title
        binding.tvDescription.text = movie.overview
        Glide.with(this).load(Config.IMAGE_URL + movie.poster_path)
            .apply(RequestOptions().override(400, 400)
                .centerInside().placeholder(R.drawable.placehoder)).into(binding.ivCover)

        val genreNames = mutableListOf<String>()
        movie.genres.map {
            genreNames.add(it.name)
        }
        binding.tvGenre.text = genreNames.joinToString(separator = ", ")
    }

    companion object {
        const val EXTRAS_MOVIE_ID = "movie_id"
    }
}