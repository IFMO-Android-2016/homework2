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
 * Created by Anya on 21.11.2016.
 */

public class MoviesRecyclerAdapter
        extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<Movie> movies = new ArrayList<>();

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
        holder.imageView.setImageURI(movie.posterPath);
        holder.imageView.setAspectRatio(2f / 3);
        holder.titleView.setText(movie.localizedTitle);
        holder.originalTitleView.setText(movie.originalTitle);
        holder.overviewView.setText(movie.overviewText);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView titleView;
        final TextView overviewView;
        final TextView originalTitleView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            titleView = (TextView) itemView.findViewById(R.id.movie_title);
            overviewView = (TextView) itemView.findViewById(R.id.movie_overview);
            originalTitleView = (TextView) itemView.findViewById(R.id.movie_original_title);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }
}