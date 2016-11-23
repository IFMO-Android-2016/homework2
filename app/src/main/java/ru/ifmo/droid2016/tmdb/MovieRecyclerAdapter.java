package ru.ifmo.droid2016.tmdb;

import android.content.Context;
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
 * Created by pohvalister on 16.11.16.
 */

class MovieRecyclerAdapter
        extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    private final LayoutInflater layoutInflater;

    private List<Movie> movies= Collections.emptyList();

    MovieRecyclerAdapter(Context context1) {;
        layoutInflater = LayoutInflater.from(context1);
    }

    void setMovies(List<Movie> movies1) {
        movies = movies1;
        notifyDataSetChanged();
    }

    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }

    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movieObj = movies.get(position);

        holder.locTitleView.setText(movieObj.localizedTitle);
        if (!movieObj.localizedTitle.equals(movieObj.originalTitle)) {
            holder.oriTitleView.setVisibility(View.VISIBLE);
            holder.oriTitleView.setText(movieObj.originalTitle);
        } else holder.oriTitleView.setVisibility(View.GONE);

        holder.posterImage.setImageURI(movieObj.posterPath);
        holder.overViewView.setText(movieObj.overviewText);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        final SimpleDraweeView posterImage;
        final TextView oriTitleView;
        final TextView locTitleView;
        final TextView overViewView;

        MovieViewHolder(View itemView) {
            super(itemView);
            posterImage = (SimpleDraweeView) itemView.findViewById(R.id.movie_poster);
            oriTitleView = (TextView) itemView.findViewById(R.id.movie_origin_title);
            locTitleView = (TextView) itemView.findViewById(R.id.movie_local_title);
            overViewView = (TextView) itemView.findViewById(R.id.movie_overview);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater1, ViewGroup parent) {
            final View view = layoutInflater1.inflate(R.layout.movie_item, parent, false);
            return new MovieViewHolder(view);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
