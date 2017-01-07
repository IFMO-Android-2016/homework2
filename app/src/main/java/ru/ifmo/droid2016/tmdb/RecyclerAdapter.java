package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.net.Uri;
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

/**
 * Created by Averin Maxim on 07.01.2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.FilmsViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<Movie> movies = Collections.EMPTY_LIST;


    static class FilmsViewHolder extends RecyclerView.ViewHolder {
        final SimpleDraweeView filmImage;
        final TextView filmTitle;
        final TextView filmOverview;

        public FilmsViewHolder(View itemView) {
            super(itemView);
            filmImage = (SimpleDraweeView) itemView.findViewById(R.id.filmImage);
            filmTitle = (TextView) itemView.findViewById(R.id.filmTitle);
            filmOverview = (TextView) itemView.findViewById(R.id.filmOverview);
        }

        static FilmsViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            return new FilmsViewHolder(layoutInflater.inflate(R.layout.film_item, parent, false));
        }
    }


    // size of out data
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // constructor of our adapter
    public RecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setMovies(@NonNull List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public FilmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return FilmsViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(FilmsViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.filmTitle.setText(movie.localizedTitle);
        holder.filmOverview.setText(movie.overviewText);
        holder.filmImage.setImageURI(Uri.parse("http://image.tmdb.org/t/p/w92" + movie.posterPath));
        holder.filmImage.setAspectRatio(2f / 3);
    }

}
