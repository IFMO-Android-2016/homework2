package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Дарья on 23.11.2016.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;

    @Nonnull
    private List<Movie> movies = Collections.emptyList();

    public MoviesAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setMovies(@Nonnull List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView titleView, description;

        private ViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_pic);
            titleView = (TextView) itemView.findViewById(R.id.movie_title);
            description = (TextView) itemView.findViewById(R.id.movie_description);
        }

        static ViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.description.setText(movie.overviewText);
        holder.imageView.setImageURI(movie.posterPath);
        holder.titleView.setText(movie.localizedTitle);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


}
