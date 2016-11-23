package ru.ifmo.droid2016.tmdb.utils;

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

import ru.ifmo.droid2016.tmdb.R;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Andrey on 22.11.2016.
 */

public class MoviesRecycleAdapter extends RecyclerView.Adapter<MoviesRecycleAdapter
        .MovieViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;

    private List<Movie> movies = Collections.EMPTY_LIST;

    public MoviesRecycleAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(@NonNull List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        holder.titleView.setText(movie.originalTitle);
        if(!movie.originalTitle.equals(movie.localizedTitle)){
            holder.localizedTitleView.setText(movie.localizedTitle);
        }
        holder.overviewView.setText(movie.overviewText);
        holder.imageView.setImageURI(movie.posterPath);

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(inflater, parent);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        final SimpleDraweeView imageView;
        final TextView titleView;
        final TextView overviewView;
        final TextView localizedTitleView;

        private MovieViewHolder(View view) {
            super(view);
            imageView = (SimpleDraweeView) view.findViewById(R.id.movie_image);
            titleView = (TextView) view.findViewById(R.id.movie_title);
            localizedTitleView = (TextView) view.findViewById(R.id.movie_localized_title);
            overviewView = (TextView) view.findViewById(R.id.movie_overview);
            imageView.setAspectRatio(2f / 3);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }

}
