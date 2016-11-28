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

/**
 * Created by roman on 13.11.16.
 */

class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder> {
    private final Context context;
    private final LayoutInflater layoutInflater;
    private int lastItem;
    public int position;

    private List<Movie> movies = new ArrayList<>();

    public MoviesRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movies1) {
        if (movies.size() == 0) {
            setMovies(movies1);
        } else {
            movies.addAll(movies1);
        }
        notifyDataSetChanged();
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        this.position = position;
        final Movie movie = movies.get(position);
        holder.titleView.setText(movie.localizedTitle);
        holder.imageView.setImageURI(movie.posterPath);
        holder.overviewView.setText(movie.overviewText);
        lastItem = Math.max(lastItem, holder.getAdapterPosition());

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public int getLastItem() {
        return lastItem;
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        final TextView titleView;
        final SimpleDraweeView imageView;
        final TextView overviewView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            titleView = (TextView) itemView.findViewById(R.id.title);
            overviewView = (TextView) itemView.findViewById(R.id.movie_overview);
        }
        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }
    private static final String TAG = "Adapter";
}
