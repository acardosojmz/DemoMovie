package dev.cardoso.demomovie.ui.listing

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dev.cardoso.demomovie.Config
import dev.cardoso.demomovie.R
import dev.cardoso.demomovie.databinding.ListItemMovieBinding
import dev.cardoso.demomovie.model.Movie
import dev.cardoso.demomovie.ui.details.DetailsActivity
import dev.cardoso.demomovie.util.Genre
import dev.cardoso.demomovie.util.inflate


class MoviesAdapter(private val context: Context, private val list: ArrayList<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val context: Context, itemView: View):
        RecyclerView.ViewHolder(itemView) {
        private val binding =  ListItemMovieBinding.bind(itemView)
        fun bind(movie: Movie) {
            itemView.setOnClickListener {
                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.EXTRAS_MOVIE_ID, movie.id)
                context.startActivity(intent)
            }
            binding.tvTitle.text = movie.title
            Glide.with(context).load(Config.IMAGE_URL + movie.poster_path)
                .apply(RequestOptions().override(400, 400)
                    .centerInside().placeholder(R.drawable.placehoder)).into(binding.ivPoster)
            binding.tvGenre.text = Genre.getGenre(movie.genre_ids)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(context, parent.inflate(R.layout.list_item_movie))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateData(newList: List<Movie>) {
        list.clear()
        val sortedList = newList.sortedBy { movie -> movie.popularity }
        list.addAll(sortedList)
        notifyDataSetChanged()
    }
}