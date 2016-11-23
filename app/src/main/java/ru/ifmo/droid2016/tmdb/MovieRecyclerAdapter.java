package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by garik on 21.11.16.
 */

public class MovieRecyclerAdapter
        extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<Movie> movies;

    public MovieRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        movies = new ArrayList<>();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        holder.originalTitleView.setText(movie.originalTitle);
        if (movie.originalTitle.equals(movie.localizedTitle)) {
            holder.localizedTitleView.setVisibility(View.INVISIBLE);
        } else {
            holder.localizedTitleView.setVisibility(View.VISIBLE);
            holder.localizedTitleView.setText(movie.localizedTitle);
        }

        holder.imageView.setImageURI(movie.posterPath);
        holder.overviewTextView.setText(movie.overviewText);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView originalTitleView;
        private TextView localizedTitleView;
        private SimpleDraweeView imageView;
        private TextView overviewTextView;

        private MovieViewHolder(View view) {
            super(view);
            originalTitleView = (TextView) view.findViewById(R.id.movie_original_title);
            localizedTitleView = (TextView) view.findViewById(R.id.movie_title);
            imageView = (SimpleDraweeView) view.findViewById(R.id.movie_image);
            overviewTextView = (TextView) view.findViewById(R.id.movie_overview);

        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }
}

