package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by egorsafronov on 00.11.2016
 */

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder>{

    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<Movie> movies = Collections.emptyList();

    public MoviesRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setMovies(List<Movie> movies) {
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
        holder.posterView.setImageURI(movie.posterPath);
        holder.titleView.setText(movie.localizedTitle);
        holder.overviewView.setText(movie.overviewText);
        holder.ratingView.setText(movie.rating);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView posterView;
        final TextView titleView;
        final TextView overviewView;
        final TextView ratingView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            posterView = (SimpleDraweeView) itemView.findViewById(R.id.movie_poster);
            titleView = (TextView) itemView.findViewById(R.id.movie_title);
            overviewView = (TextView) itemView.findViewById(R.id.movie_overview);
            ratingView = (TextView) itemView.findViewById(R.id.movie_rating);
        }

        public static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }
}
