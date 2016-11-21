package ru.ifmo.droid2016.tmdb.utils;
//

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
import ru.ifmo.droid2016.tmdb.R;


class MoviesRecyclerAdapter
        extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;

    @NonNull
    private List<Movie> Movies = Collections.emptyList();

    public MoviesRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setMovies(@NonNull List<Movie> Movies) {
        this.Movies = Movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie myMovie = Movies.get(position);
        String myImage = myMovie.posterPath;
        holder.imageView.setImageURI(myImage);
        holder.originalTitleView.setText(myMovie.originalTitle);
        holder.overviewTextView.setText(myMovie.overviewText);
        int visibility = myMovie.originalTitle.equals(myMovie.localizedTitle) ? View.GONE : View.VISIBLE;
        holder.localizedTitleView.setVisibility(visibility);
        holder.localizedTitleView.setText(myMovie.localizedTitle);
    }

    @Override
    public int getItemCount() {
        return Movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView overviewTextView;
        final TextView localizedTitleView;
        final TextView originalTitleView;


        private MovieViewHolder(View itemView /*TextView textView*/) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            localizedTitleView = (TextView) itemView.findViewById(R.id.movie_title);
            overviewTextView = (TextView) itemView.findViewById(R.id.movie_overview);
            originalTitleView = (TextView) itemView.findViewById(R.id.movie_original_title);
        }


        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }

    private static final String TAG = "Movie Adapter";
}
