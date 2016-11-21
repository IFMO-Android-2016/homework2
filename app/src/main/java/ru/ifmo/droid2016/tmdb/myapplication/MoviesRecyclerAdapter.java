package ru.ifmo.droid2016.tmdb.myapplication;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import ru.ifmo.droid2016.tmdb.myapplication.loader.MoviesDownloader;
import ru.ifmo.droid2016.tmdb.myapplication.model.Movie;

import java.io.File;
import java.util.Collections;
import java.util.List;

class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder> {

    private final LayoutInflater layoutInflater;


    @NonNull
    private List<Movie> movies = Collections.emptyList();

    MoviesRecyclerAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    void setMovies(@NonNull List<Movie> movies) {
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
        holder.poster.invalidate();
        holder.poster.setImageURI(Uri.fromFile(new File(MoviesDownloader.directory, movie.posterPath)));
        //holder.poster.setImageBitmap(BitmapFactory.decodeFile(
          //      (new File(MoviesDownloader.directory, movie.posterPath)).toString()));
        holder.localizedTitle.setText(movie.localizedTitle);
        holder.overviewText.setText(movie.overviewText);
      //  holder.originalTitle.setText(movie.originalTitle);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    static class MovieViewHolder extends RecyclerView.ViewHolder {
        final SimpleDraweeView poster;
      //  final TextView originalTitle;
        final TextView overviewText;
        final TextView localizedTitle;

        MovieViewHolder(View itemView) {
            super(itemView);
            poster = (SimpleDraweeView) itemView.findViewById(R.id.poster);
         //   originalTitle = (TextView) itemView.findViewById(R.id.originalTitle);
            overviewText = (TextView) itemView.findViewById(R.id.overviewText);
            localizedTitle = (TextView) itemView.findViewById(R.id.localizedTitle);

        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }
}
