package ru.ifmo.droid2016.tmdb;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MovieViewHolder> {
    private final LayoutInflater layoutInflater;

    @NonNull
    private ArrayList<Movie> movies = new ArrayList<>();

    RecyclerAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void addMovies(@NonNull List<Movie> data) {
        this.movies.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = movies.get(position);

        holder.titleTView.setText       (movie.localizedTitle);
        holder.imageSDView.setImageURI  (movie.posterPath);
        holder.owerviewTView.setText    (movie.overviewText);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    void setMovies(@NonNull List<Movie> data) {
        this.movies = new ArrayList<>();
        addMovies(data);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        final SimpleDraweeView imageSDView;
        final TextView titleTView;
        final TextView owerviewTView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            imageSDView    = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            titleTView     = (TextView) itemView.findViewById(R.id.movie_title);
            owerviewTView  = (TextView) itemView.findViewById(R.id.movie_overviewText);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }
}

