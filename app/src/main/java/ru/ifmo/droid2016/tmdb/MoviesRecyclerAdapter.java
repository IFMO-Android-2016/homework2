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
 * Created by AdminPC on 23.11.2016.
 */
public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder>{

    private final Context context;
    private final LayoutInflater layoutInflater;
    @NonNull
    private List<Movie> movies ;

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        MovieViewHolder pvh = new MovieViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int i) {
        final Movie movie = movies.get(i);
        if (movie != null) {
            movieViewHolder.movie_title.setText(movie.localizedTitle);
            movieViewHolder.movie_text.setText(movie.overviewText);
            movieViewHolder.movie_image.setImageURI(("http://image.tmdb.org/t/p/w1000" + movie.posterPath));

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        private final SimpleDraweeView movie_image;
        private final TextView movie_title;
        private final TextView movie_text;

        private MovieViewHolder(View itemView) {
            super(itemView);
            movie_image = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            movie_title = (TextView) itemView.findViewById(R.id.movie_title);
            movie_text = (TextView) itemView.findViewById(R.id.movie_text);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }

    }
    MoviesRecyclerAdapter(Context context){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }


    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }



}
