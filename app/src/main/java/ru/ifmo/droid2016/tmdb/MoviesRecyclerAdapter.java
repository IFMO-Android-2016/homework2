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
        holder.imageView.setImageURI(movie.posterPath);
        holder.titleView.setText(movie.localizedTitle);
        holder.originalTitleView.setText(movie.originalTitle);
        holder.infoView.setText(movie.overviewText);
        holder.ratingView.setText(String.valueOf(movie.rating));
        // TODO: Check for null
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView titleView, originalTitleView, infoView, ratingView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_poster);
            titleView = (TextView) itemView.findViewById(R.id.movie_title);
            originalTitleView = (TextView) itemView.findViewById(R.id.movie_title_original);
            infoView = (TextView) itemView.findViewById(R.id.movie_info);
            ratingView = (TextView) itemView.findViewById(R.id.movie_rating);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }
}
