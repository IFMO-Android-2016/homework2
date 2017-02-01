package ru.ifmo.droid2016.tmdb;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Collection;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Anton on 29.01.2017.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.Holder>{

    private ArrayList<Movie> movies = new ArrayList<>();

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.movie_view, parent, false);

        return new Holder(contactView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Movie movie = movies.get(position);

        Log.d("poster path ", "https://image.tmdb.org/t/p/w500" + movie.posterPath);
        holder.posterImage.setImageURI(Uri.parse("https://image.tmdb.org/t/p/w500" + movie.posterPath));
        holder.originalTitle.setText(movie.originalTitle);
        holder.localizedTitle.setText(movie.localizedTitle);
        holder.overview.setText(movie.overviewText);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    // TODO: 29.01.2017 check for already displayed movies
    public void addMovies(Collection<Movie> newMovies) {
        movies.addAll(newMovies);
    }

    public class Holder extends RecyclerView.ViewHolder {
        public SimpleDraweeView posterImage;
        public TextView originalTitle;
        public TextView localizedTitle;
        public TextView overview;

        public Holder(View itemView) {
            super(itemView);

            posterImage = (SimpleDraweeView) itemView.findViewById(R.id.poster_image);
            originalTitle = (TextView) itemView.findViewById(R.id.original_title);
            localizedTitle = (TextView) itemView.findViewById(R.id.localized_title);
            overview = (TextView) itemView.findViewById(R.id.overview);
        }
    }
}
