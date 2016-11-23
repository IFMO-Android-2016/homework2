package ru.ifmo.droid2016.tmdb.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.tmdb.R;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Михайлов Никита on 19.11.2016.
 */

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder> {
    private List<Movie> movies = Collections.emptyList();
    private LayoutInflater inflater;

    public MoviesRecyclerAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        inflater = LayoutInflater.from(context);
    }


    public void onUpdate(@Nullable List<Movie> movies) {
        if (movies == null) return;
        int size = getItemCount();
        this.movies.addAll(movies);
        notifyItemInserted(size);
    }

    public List<Movie> getMovies() {
        return movies;
    }

    @Override
    public MoviesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.film_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.title.setText(movie.localizedTitle.equals("") ? movie.originalTitle : movie.localizedTitle);
        if (!movie.overviewText.equals("")) {
            holder.overview.setText(movie.overviewText);
        }
        holder.poster.setImageURI(movie.posterPath);
        holder.id = movie.id;
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView overview, title;
        int id;
        final SimpleDraweeView poster;

        ViewHolder(final View itemView) {
            super(itemView);
            poster = (SimpleDraweeView) itemView.findViewById(R.id.poster);
            poster.setAspectRatio(2f / 3);
            overview = (TextView) itemView.findViewById(R.id.overview);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}