package ru.ifmo.droid2016.tmdb.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.tmdb.R;
import ru.ifmo.droid2016.tmdb.model.Movie;

public class PopularMoviesRecyclerAdapter
        extends RecyclerView.Adapter<PopularMoviesRecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private List<Movie> movies = Collections.emptyList();

    public PopularMoviesRecyclerAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PopularMoviesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.newInstance(inflater, parent);
    }

    @Override
    public void onBindViewHolder(PopularMoviesRecyclerAdapter.ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.title.setText(movie.title.equals("") ? movie.originalTitle : movie.title);

        if (!movie.overview.equals("")) {
            holder.overview.setText(movie.overview);
        }

        holder.voteAverage.setText(movie.voteAverage);
        Double voteAverage = Double.parseDouble(movie.voteAverage);

        if (voteAverage >= 9) {
            holder.voteAverageContainer.setBackgroundResource(R.color.voteAverageBlue);
        } else if (voteAverage >= 7) {
            holder.voteAverageContainer.setBackgroundResource(R.color.voteAverageGreen);
        } else if (voteAverage >= 5) {
            holder.voteAverageContainer.setBackgroundResource(R.color.voteAverageOrange);
        } else if (voteAverage >= 3.5) {
            holder.voteAverageContainer.setBackgroundResource(R.color.voteAverageRed);
        }

        holder.poster.setImageURI(movie.posterPath);
//        holder.movieBackdrop.setImageURI(movie.backdropPath);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView overview;

        final TextView voteAverage;
        final LinearLayout voteAverageContainer;

        final SimpleDraweeView poster;
//        final SimpleDraweeView backdrop;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            overview = (TextView) itemView.findViewById(R.id.overview);

            voteAverage = (TextView) itemView.findViewById(R.id.vote_average);
            voteAverageContainer = (LinearLayout) itemView.findViewById(R.id.vote_average_container);

            poster = (SimpleDraweeView) itemView.findViewById(R.id.poster);
//            backdrop = (SimpleDraweeView) itemView.findViewById(R.id.movie_backdrop);

            poster.setAspectRatio(2f / 3);
//            backdrop.setAspectRatio(16f/9);
        }

        static ViewHolder newInstance(LayoutInflater inflater, ViewGroup parent) {
            return new ViewHolder(inflater.inflate(R.layout.item_movie, parent, false));
        }
    }
}
