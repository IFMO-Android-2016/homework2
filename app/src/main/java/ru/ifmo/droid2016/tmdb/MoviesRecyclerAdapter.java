package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by vadim on 21.11.16.
 */

public class MoviesRecyclerAdapter
    extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieHolder> {

    private LayoutInflater inflater;
    private List<Movie> movies = Collections.emptyList();
    private static String IMAGEURL = "https://image.tmdb.org/t/p/";

    public MoviesRecyclerAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setMovies(@NonNull List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Log.d(TAG, "onBind");

        Movie movie = movies.get(position);

        holder.title.setText(movie.localizedTitle);
        holder.overview.setText(movie.overviewText);
        holder.image.setImageURI(Uri.parse(IMAGEURL + "w500" + movie.posterPath));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieHolder extends RecyclerView.ViewHolder {
        com.facebook.drawee.view.SimpleDraweeView image;
        TextView title;
        TextView overview;

        public MovieHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            overview = (TextView) v.findViewById(R.id.overview);
            image = (com.facebook.drawee.view.SimpleDraweeView) v.findViewById(R.id.image);
        }
    }
    private static String TAG = "main";
}
