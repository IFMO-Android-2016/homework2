package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

class MovieRecyclerAdapter
        extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<Movie> movies = Collections.emptyList();

    MovieRecyclerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    void setMovies(List<Movie> movies) {
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
        holder.localizedTitleView.setText(movie.localizedTitle);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final TextView localizedTitleView;

        MovieViewHolder(View itemView) {
            super(itemView);
            localizedTitleView = (TextView) itemView.findViewById(R.id.movie_title);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }
}
