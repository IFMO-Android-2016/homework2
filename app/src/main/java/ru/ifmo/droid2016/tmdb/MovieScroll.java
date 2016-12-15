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
/**
 * Created by Елена on 23.11.2016.
 */
class MovieScroll extends RecyclerView.Adapter<MovieScroll.MovieViewHolder> {

    private static final String TAG = "Movie Adapter";
    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<Movie> movies;

    MovieScroll(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    void setMovies(List<Movie> movies) {
        notifyDataSetChanged();
        this.movies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        holder.originalTitleView.setText(movie.originalTitle);
        if (movie.originalTitle.equals(movie.localizedTitle)) {
            holder.localizedTitleView.setVisibility(View.GONE);
        } else {
            holder.localizedTitleView.setVisibility(View.VISIBLE);
            holder.localizedTitleView.setText(movie.localizedTitle);
        }
        holder.imageView.setImageURI(movie.posterPath);
        holder.overviewTextView.setText(movie.overviewText);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final TextView originalTitleView;
        final TextView localizedTitleView;
        final SimpleDraweeView imageView;
        final TextView overviewTextView;

        MovieViewHolder(View itemView) {
            super(itemView);
            localizedTitleView = (TextView) itemView.findViewById(R.id.movie_title_trans);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            overviewTextView = (TextView) itemView.findViewById(R.id.text);
            originalTitleView = (TextView) itemView.findViewById(R.id.movie_original_title);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.main_view, parent, false);
            return new MovieViewHolder(view);
        }
    }

}