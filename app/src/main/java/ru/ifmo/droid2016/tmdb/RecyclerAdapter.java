package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

class RecyclerAdapter
        extends RecyclerView.Adapter<RecyclerAdapter.MovieViewHolder> {

    private final LayoutInflater layoutInflater;

    @NonNull
    private ArrayList<Movie> movies = new ArrayList<>();

    RecyclerAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    void addMovies(@NonNull List<Movie> movies) {
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
        holder.titleView.setText(movie.getLocalizedTitle());
        holder.imageView.setImageURI(movie.getPosterPath());
        holder.descriptionView.setText(movie.getOverviewText());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    void setMovies(@NonNull List<Movie> movies) {
        this.movies = new ArrayList<>();
        addMovies(movies);
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
            final View view = layoutInflater.inflate(R.layout.movie_item, parent, false);
            return new MovieViewHolder(view);
        }
    }


}
