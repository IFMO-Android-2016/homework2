package ru.ifmo.droid2016.tmdb.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.tmdb.R;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Антон on 23.11.2016.
 */

public class MoviesRecyclerAdapter
        extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;

    @NonNull
    private List<Movie> movies = Collections.emptyList();

    public MoviesRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setMovies(@NonNull List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        if (movie.localizedTitle.equals(movie.originalTitle))
            holder.localizedTitleView.setVisibility(View.GONE);
        else
            holder.localizedTitleView.setVisibility(View.VISIBLE);
        holder.localizedTitleView.setText(movie.localizedTitle);
        holder.originalTitleView.setText(movie.originalTitle);
        holder.overviewTextView.setText(movie.overviewText);
        holder.imageView.setImageURI(movie.posterPath);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView localizedTitleView;
        final TextView originalTitleView;
        final TextView overviewTextView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_poster);
            localizedTitleView = (TextView) itemView.findViewById(R.id.movie_title);
            originalTitleView = (TextView) itemView.findViewById(R.id.movie_original_title);
            overviewTextView = (TextView) itemView.findViewById(R.id.movie_overview);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }
}
