package com.example.newproject.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newproject.R
import com.example.newproject.data.models.Movie
import com.example.newproject.presentation.adapter.MoviesAdapter.MovieViewHolder

/**
 * Created by Nimish Nandwana on 07/09/2021.
 * Description - MoviesAdapter to show movie list
 */

class MoviesAdapter(private val onClick: (id: String) -> Unit) : RecyclerView.Adapter<MovieViewHolder>() {

    private var allMovies: MutableList<Movie> = mutableListOf()

    fun updateItems(movies: List<Movie>) {
        allMovies.clear()
        allMovies.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_movie_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindView(allMovies[position])
    }

    override fun getItemCount() = allMovies.size

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val moviePoster = view.findViewById<ImageView>(R.id.iv_movie_image)
        private val movieName = view.findViewById<TextView>(R.id.tv_movie_name)
        private val movieYear = view.findViewById<TextView>(R.id.tv_movie_year)
        private val llParent = view.findViewById<LinearLayout>(R.id.ll_parent)

        fun bindView(movie: Movie) {

            Glide.with(moviePoster.context)
                .load(movie.poster)
                .error(R.drawable.nope_not_here)
                .into(moviePoster)
            movieName.text = movie.title
            movieYear.text = movie.year

            llParent.setOnClickListener {
                onClick.invoke(movie.title)
            }
        }
    }
}