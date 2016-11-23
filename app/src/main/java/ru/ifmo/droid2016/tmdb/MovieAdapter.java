package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.net.Uri;
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
 * Created by Roman on 16/11/2016.
 */

public class MovieAdapter
        extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private LayoutInflater inflater;
    private List<Movie> movies = new ArrayList<>();

    public MovieAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.makeHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Uri uri = Uri.parse("http://image.tmdb.org/t/p/w500");
        Uri.Builder builder = uri.buildUpon();
        builder.appendEncodedPath(movie.posterPath);
        holder.draweeView.setImageURI(builder.build());
        Log.d("image uri", builder.toString());
        holder.title.setText(movie.localizedTitle);
        holder.originalTitle.setText(movie.originalTitle);
        holder.overview.setText(movie.overviewText);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        protected SimpleDraweeView draweeView;
        protected TextView title, originalTitle, overview;
        protected MovieViewHolder(View itemView) {
            super(itemView);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.title);
            originalTitle = (TextView) itemView.findViewById(R.id.original_title);
            overview = (TextView) itemView.findViewById(R.id.overview);

        }
        protected static MovieViewHolder makeHolder(LayoutInflater inflater, ViewGroup parent) {
            View view = inflater.inflate(R.layout.movies_list_item, parent, false);
            return new MovieViewHolder(view);
        }
    }
}
