package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by ivan on 22.11.16.
 */

public class TmbdRecyclerAdapter extends RecyclerView.Adapter<TmbdRecyclerAdapter.MovieViewHolder>{

    private final Context context;
    private final LayoutInflater layoutInflater;

    @Nonnull
    private List<Movie> movies = new ArrayList<>();

    public TmbdRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        holder.titleView.setText(movie.localizedTitle);
        holder.imageView.setImageURI(movie.posterPath);
        holder.descriptionView.setText(movie.overviewText);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(@Nonnull List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }


    static class MovieViewHolder extends RecyclerView.ViewHolder {
        final SimpleDraweeView imageView;
        final TextView titleView;
        final TextView descriptionView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            titleView = (TextView) itemView.findViewById(R.id.movie_title);
            descriptionView = (TextView) itemView.findViewById(R.id.movie_description);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.movie_item, parent,false);
            return new MovieViewHolder(view);
        }
    }
}
