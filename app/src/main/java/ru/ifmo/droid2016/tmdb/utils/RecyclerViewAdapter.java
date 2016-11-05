package ru.ifmo.droid2016.tmdb.utils;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.R;
import ru.ifmo.droid2016.tmdb.apiLoaders.apiLoaderGetPopularMovies;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final List<Movie> movies;
    private OnLoadMore loader;
    private boolean loaded;
    private int visibleThreshold = 3;
    public RecyclerViewAdapter(List<Movie> movies, RecyclerView recyclerView) {
        this.movies = movies;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                .getLayoutManager();
        loaded = false;

        recyclerView
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        int totalItemCount = linearLayoutManager.getItemCount();
                        int lastVisibleItem = linearLayoutManager
                                .findLastVisibleItemPosition();
                        if (loaded
                                && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            if (loader != null) {
                                loader.onLoadMore();
                            }
                            loaded = false;
                        }
                    }
                });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_card, parent, false);
            return new PersonViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progres_bar, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {

        return movies.get(position) == null ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PersonViewHolder) {
            PersonViewHolder vw = (PersonViewHolder) holder;
            vw.image.setImageURI(movies.get(position).posterPath);
            vw.title.setText(movies.get(position).localizedTitle);
            vw.discribe.setText(movies.get(position).overviewText);
        } else {
            ProgressViewHolder v = (ProgressViewHolder) holder;
            v.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void onLoadNewItems(OnLoadMore loader) {
        this.loader = loader;
    }

    public void setLoaded() {
        this.loaded = true;
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {
        final SimpleDraweeView image;
        final TextView title;
        final TextView discribe;

        public PersonViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.title);
            discribe = (TextView) itemView.findViewById(R.id.discribe);
        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {
        final ProgressBar progressBar;
        public ProgressViewHolder(View itemView) {
            super(itemView);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }
}
