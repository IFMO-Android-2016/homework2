package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

class MovieRecyclerAdapter
        extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    // private final List<Movie> movies = new ArrayList<>();
    private List<Movie> movies;

    MovieRecyclerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    void setMovies(List<Movie> movies) {
        this.movies = movies;
        //this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        /*
        if (movie.originalTitle.contains(movie.localizedTitle)) {
            holder.localizedTitleView.setText(movie.localizedTitle + " (" +
                    movie.originalTitle.substring(0,
                            movie.originalTitle.indexOf(" (" + movie.localizedTitle)) + ")");
        } else {
            if (movie.originalTitle != movie.localizedTitle) {
                holder.localizedTitleView.setText(movie.localizedTitle + " (" + movie.originalTitle + ")");
            } else {
                holder.localizedTitleView.setText(movie.localizedTitle);
            }
        }
        */
        holder.originalTitleView.setText(movie.originalTitle);
        if (movie.originalTitle.equals(movie.localizedTitle)) {
            holder.localizedTitleView.setVisibility(View.GONE);
        } else {
            holder.localizedTitleView.setVisibility(View.VISIBLE);
            holder.localizedTitleView.setText(movie.localizedTitle);
        }
        //Log.d(TAG, movie.posterPath);
        holder.imageView.setImageURI(movie.posterPath);
        holder.overviewTextView.setText(movie.overviewText);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final TextView originalTitleView;
        final TextView localizedTitleView;
        final SimpleDraweeView imageView;
        final TextView overviewTextView;

        MovieViewHolder(View itemView) {
            super(itemView);
            localizedTitleView = (TextView) itemView.findViewById(R.id.movie_title);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            overviewTextView = (TextView) itemView.findViewById(R.id.movie_overview);
            originalTitleView = (TextView) itemView.findViewById(R.id.movie_original_title);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }

    private static final String TAG = "Movie Adapter";
}
