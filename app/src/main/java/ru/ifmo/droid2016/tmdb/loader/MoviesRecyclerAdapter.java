package ru.ifmo.droid2016.tmdb.loader;

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

public class MoviesRecyclerAdapter
        extends RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;

    @NonNull
    private List<Movie> movies = Collections.EMPTY_LIST;

    public MoviesRecyclerAdapter(Context context) {
        this.context = context;
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
        holder.titleView.setText(movie.originalTitle);
        holder.localizedTitleView.setText(movie.localizedTitle);
        holder.overviewTextView.setText(movie.overviewText);
        holder.imageView.setImageURI(movie.posterPath);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView titleView;
        final TextView localizedTitleView;
        final TextView overviewTextView;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.movie_title);
            localizedTitleView = (TextView) itemView.findViewById(R.id.movie_loctitle);
            overviewTextView = (TextView) itemView.findViewById(R.id.movie_overview);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            imageView.setAspectRatio(2f / 3);
        }

        static MoviesViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MoviesViewHolder(view);
        }
    }
}
