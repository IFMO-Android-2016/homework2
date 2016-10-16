package ru.ifmo.droid2016.tmdb.adapter;


import android.content.Context;
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

public class PopularMoviesRecyclerAdapter
        extends RecyclerView.Adapter<PopularMoviesRecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private List<Movie> movies = Collections.emptyList();

    public PopularMoviesRecyclerAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PopularMoviesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.newInstance(inflater, parent);
    }

    @Override
    public void onBindViewHolder(PopularMoviesRecyclerAdapter.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.movieTitle.setText(movie.localizedTitle);
        holder.moviePoster.setImageURI(movie.posterPath);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView moviePoster;
        final TextView movieTitle;

        ViewHolder(View itemView) {
            super(itemView);
            moviePoster = (SimpleDraweeView) itemView.findViewById(R.id.movie_poster);
            movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
        }

        static ViewHolder newInstance(LayoutInflater inflater, ViewGroup parent) {
            return new ViewHolder(inflater.inflate(R.layout.item_movie, parent, false));
        }
    }
}
