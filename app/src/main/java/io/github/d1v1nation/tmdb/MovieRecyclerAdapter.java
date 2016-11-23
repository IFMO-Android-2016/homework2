package io.github.d1v1nation.tmdb;

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

import io.github.d1v1nation.tmdb.model.Movie;

/**
 * @author d1v1nation (catanaut@gmail.com)
 *         <p>
 *         23.11.16 of movies | io.github.d1v1nation.tmdb
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;

    @NonNull
    private List<Movie> movies = Collections.emptyList();

    public MovieRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setMovies(@NonNull List<Movie> movies) {
        this.movies = movies;
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
        holder.imageView.setImageURI(movie.posterPath);
        holder.descView.setText(movie.overviewText);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView titleView;
        final TextView descView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            titleView = (TextView) itemView.findViewById(R.id.movie_title);
            descView = (TextView) itemView.findViewById(R.id.movie_desc);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }


}
