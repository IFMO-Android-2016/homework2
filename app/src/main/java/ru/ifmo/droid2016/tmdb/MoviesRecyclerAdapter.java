package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

class MoviesRecyclerAdapter extends RecyclerView.Adapter {

    private final int ITEM_PROGRESS = R.layout.item_progressbar;
    private final int ITEM_MOVIE = R.layout.item_movie;

    private final LayoutInflater layoutInflater;
    private final List<Movie> movies;
    private boolean loading;
    private int errorText;

    MoviesRecyclerAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return movies.get(position) != null ? ITEM_MOVIE : ITEM_PROGRESS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_MOVIE) {
            return MovieViewHolder.newInstance(layoutInflater, parent);
        } else if (viewType == ITEM_PROGRESS) {
            return ProgressViewHolder.newInstance(layoutInflater, parent);
        }
        throw new IllegalArgumentException("Unknown View type: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProgressViewHolder) {
            final ProgressViewHolder progressHolder = (ProgressViewHolder) holder;
            progressHolder.progressBar.setIndeterminate(true);
            progressHolder.tryAgainButton.setVisibility(View.GONE);
            progressHolder.tryAgainButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    progressHolder.progressBar.setVisibility(View.VISIBLE);
                    progressHolder.tryAgainButton.setVisibility(View.GONE);
                }
            });
        } else if (holder instanceof MovieViewHolder) {
            final Movie movie = movies.get(position);
            final MovieViewHolder movieHolder = (MovieViewHolder) holder;
            movieHolder.imageView.setImageURI(movie.posterPath);
            movieHolder.titleView.setText(movie.localizedTitle);
            movieHolder.originalTitleView.setText(movie.originalTitle);
            movieHolder.infoView.setText(movie.overviewText);
            movieHolder.ratingView.setText(String.valueOf(movie.rating));
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        
    }

    public void setErrorText(int errorText) {
        this.errorText = errorText;
    }

    private static class MovieViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView titleView, originalTitleView, infoView, ratingView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_poster);
            titleView = (TextView) itemView.findViewById(R.id.movie_title);
            originalTitleView = (TextView) itemView.findViewById(R.id.movie_title_original);
            infoView = (TextView) itemView.findViewById(R.id.movie_info);
            ratingView = (TextView) itemView.findViewById(R.id.movie_rating);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {

        private final ProgressBar progressBar;
        private final Button tryAgainButton;

        private ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            tryAgainButton = (Button) itemView.findViewById(R.id.try_again);
        }

        static ProgressViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_progressbar, parent, false);
            return new ProgressViewHolder(view);
        }
    }
}
