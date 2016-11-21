package ru.ifmo.droid2016.tmdb;

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

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Anna Kopeliovich on 20.11.2016.
 */

public class MoviesRecyclerAdapter
        extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder>{

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
        holder.originalTitleView.setText(movie.originalTitle);
        holder.localizedTitleView.setText(movie.localizedTitle);
        holder.imageView.setImageURI(movie.posterPath);
        holder.overviewTextView.setText(movie.overviewText);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView originalTitleView;
        final TextView localizedTitleView;
        final TextView overviewTextView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            localizedTitleView = (TextView) itemView.findViewById(R.id.movie_title);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            overviewTextView = (TextView) itemView.findViewById(R.id.text);
            originalTitleView = (TextView) itemView.findViewById(R.id.movie_original_title);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.movie_item, parent, false);
            return new MovieViewHolder(view);
        }
    }

    private static final String TAG = "MovieReyclerAdapter";
}