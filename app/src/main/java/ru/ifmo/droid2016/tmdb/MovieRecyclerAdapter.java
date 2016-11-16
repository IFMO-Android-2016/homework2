package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;

public class MovieRecyclerAdapter
        extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    private final LayoutInflater layoutInflater;
    private int lastVisibleItemNumber, currentPosition;

    private List<Movie> movies = Collections.emptyList();

    public MovieRecyclerAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> nextMovies) {
        if (movies.size() == 0) {
            setMovies(nextMovies);
        } else {
            movies.addAll(nextMovies);
        }
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        holder.titleView.setText(movie.localizedTitle);
        holder.overviewView.setText(movie.overviewText);
        holder.voteView.setText(movie.vote);
        Uri uri = TmdbApi.getPosterUri(movie);
        holder.draweeView.setImageURI(uri);
        holder.getAdapterPosition();
        lastVisibleItemNumber = Math.max(lastVisibleItemNumber, holder.getAdapterPosition());
        currentPosition = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public int getLastVisibleItemNumber() {
        return lastVisibleItemNumber;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final TextView titleView;
        final TextView overviewView;
        final TextView voteView;
        final SimpleDraweeView draweeView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.movie_title);
            overviewView = (TextView) itemView.findViewById(R.id.movie_overview);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.poster);
            voteView = (TextView) itemView.findViewById(R.id.movie_vote);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }
}
