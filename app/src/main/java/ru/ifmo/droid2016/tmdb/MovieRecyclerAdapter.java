package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Игорь on 19.11.2016.
 */

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieHolder> {
    private final Context context;
    private final LayoutInflater layoutInflater;

    private ArrayList<Movie> data;

    public static final String LOG_TAG = "RecyclerAdapter";

    public MovieRecyclerAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        data = movies;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "movieHolder was created");
        return MovieHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie movie = data.get(position);
        holder.imageView.setImageURI(movie.posterPath);
        holder.tvTitle.setText(movie.localizedTitle);
        holder.tvOverview.setText(movie.overviewText);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MovieHolder extends RecyclerView.ViewHolder {
        final SimpleDraweeView imageView;
        final TextView tvTitle;
        final TextView tvOverview;

        public MovieHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView)itemView.findViewById(R.id.image);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView)itemView.findViewById(R.id.tvOverview);
        }

        private static MovieHolder newInstance(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            final View view = layoutInflater.inflate(R.layout.movie_card, viewGroup, false);
            return new MovieHolder(view);
        }
    }
}
