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
 * Created by YA on 20.11.2016.
 */

public class MoviesRecyclerAdapter
        extends RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesViewHolder> {

    private final LayoutInflater layoutInflater;

    @NonNull

    private List<Movie> movies = Collections.emptyList();

    public MoviesRecyclerAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }
    public void setMovies(@NonNull List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MoviesViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        holder.title.setText(movie.originalTitle);
        holder.overview.setText(movie.overviewText);
        holder.loctitle.setText(movie.localizedTitle);
        holder.image.setImageURI(movie.posterPath);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView overview;
        final TextView loctitle;
        final SimpleDraweeView image;

        private MoviesViewHolder (View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.movie_title);
            overview = (TextView) view.findViewById(R.id.movie_overview);
            loctitle = (TextView) view.findViewById(R.id.movie_loctitle);
            image = (SimpleDraweeView) view.findViewById(R.id.movie_image);
        }

        static MoviesViewHolder newInstance (LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.the_move_activity, parent, false);
            return new MoviesViewHolder(view);
        }
    }
}

