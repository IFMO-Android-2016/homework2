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

/**
 * Created by 1 on 23/11/16.
 */

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder>{
    private final Context context;
    private final LayoutInflater layoutInflater;

    @NonNull
    private List<Movie> movies = Collections.emptyList();

    public MoviesRecyclerAdapter(Context context){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void addMovies(@NonNull List<Movie> movies){
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void setMovies(@NonNull List<Movie> movies){
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
        holder.originalTitleView.setText(movie.originalTitle);
        holder.localizedTitleView.setText(movie.localizedTitle);
        holder.overviewView.setText(movie.overviewText);
        holder.posterView.setImageURI(movie.posterPath);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    static class MovieViewHolder extends RecyclerView.ViewHolder{

        final SimpleDraweeView posterView;
        final TextView originalTitleView;
        final TextView localizedTitleView;
        final TextView overviewView;

        private MovieViewHolder(View itemView){
            super(itemView);
            posterView = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            originalTitleView = (TextView) itemView.findViewById(R.id.movie_original_title);
            localizedTitleView = (TextView) itemView.findViewById(R.id.movie_localized_title);
            overviewView = (TextView) itemView.findViewById(R.id.movie_overview);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent){
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }

    }
}
