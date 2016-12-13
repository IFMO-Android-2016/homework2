package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Movie> movies;

    MovieRecyclerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.origin.setText(movie.originalTitle);
        if (movie.originalTitle.equals(movie.localizedTitle))
            holder.local.setVisibility(View.GONE);
        else {
            holder.local.setVisibility(View.VISIBLE);
            holder.local.setText(movie.localizedTitle);
        }
        holder.imageView.setImageURI(movie.posterPath);
        holder.overwiew.setText(movie.overviewText);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView origin, local, overwiew;
        private SimpleDraweeView imageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            local = (TextView) itemView.findViewById(R.id.movie_title);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            overwiew = (TextView) itemView.findViewById(R.id.movie_overview);
            origin = (TextView) itemView.findViewById(R.id.movie_original_title);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            return new MovieViewHolder(layoutInflater.inflate(R.layout.item_movie, parent, false));
        }
    }
}
