package ru.ifmo.droid2016.tmdb.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.tmdb.R;
import ru.ifmo.droid2016.tmdb.loader.MoviesPullParser;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by dmitry.trunin on 03.10.2016.
 */

public class MoviesRecyclerAdapter
        extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder> {
    
    public MoviesRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    private final Context context;
    private final LayoutInflater layoutInflater;
    private SparseArray<List<Movie>> movies = new SparseArray<>();

    public void addMovies(List<Movie> movies, int page) {
        Log.d("ADDED MOVIES", String.valueOf(page));
        this.movies.append(page, movies);
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = movies.get(position / MoviesPullParser.itemsOnPage)
                                  .get(position % MoviesPullParser.itemsOnPage);
        holder.originalTitleView.setText(movie.originalTitle);
        holder.localizedTitleView.setText(movie.localizedTitle);
        holder.overviewTextView.setText(movie.overviewText);
        holder.imageView.setImageURI(movie.posterPath);
    }

    @Override
    public int getItemCount() {
        return this.movies.size() * MoviesPullParser.itemsOnPage;
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView originalTitleView, localizedTitleView, overviewTextView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_poster);
            originalTitleView = (TextView) itemView.findViewById(R.id.movie_original_title);
            localizedTitleView = (TextView) itemView.findViewById(R.id.movie_localized_title);
            overviewTextView = (TextView) itemView.findViewById(R.id.movie_overview);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }
}