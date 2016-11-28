package ru.ifmo.droid2016.tmdb;

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

public class MovieRecyclerAdapter
        extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    private static final String ADDRESS = "https://image.tmdb.org/t/p/w500";

    private final LayoutInflater li;
    private int lastItem;
    @NonNull
    private List<Movie> movies = Collections.emptyList();

    public MovieRecyclerAdapter(Context c) {
        this.li = LayoutInflater.from(c);
    }

    public int getLastItem() {
        return lastItem;
    }

    public void setMovies(@NonNull List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> nextMovies) {
        if (movies.size() == 0) {
            setMovies(nextMovies);
        } else {
            movies.addAll(nextMovies);
        }
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(li, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie m = movies.get(position);
        holder.overview.setText(m.overviewText);
        holder.title.setText(m.localizedTitle);
        holder.sdv.setImageURI(ADDRESS + m.posterPath);
        holder.getAdapterPosition();
        lastItem = Math.max(lastItem, holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView sdv;
        final TextView title, overview;

        public MovieViewHolder(View itemView) {
            super(itemView);
            sdv = (SimpleDraweeView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.title);
            overview = (TextView) itemView.findViewById(R.id.overview);
        }

        static MovieViewHolder newInstance(LayoutInflater li, ViewGroup p) {
            final View v = li.inflate(R.layout.item, p, false);
            return new MovieViewHolder(v);
        }
    }
}
