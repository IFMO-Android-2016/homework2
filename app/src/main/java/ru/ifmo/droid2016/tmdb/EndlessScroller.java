package ru.ifmo.droid2016.tmdb;

import android.support.v4.app.LoaderManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.model.Movie;

public class EndlessScroller extends RecyclerView.OnScrollListener {
    private LoaderManager loaderManager;
    private MovieRecyclerAdapter adapter;
    private LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> callbacks;
    private int lastPage = 0;

    public EndlessScroller(LoaderManager lm, MovieRecyclerAdapter adapter,
                                 LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> callbacks) {
        loaderManager = lm;
        this.adapter = adapter;
        this.callbacks = callbacks;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemNumber = adapter.getLastItem();
        int visibleTreshold = 5;
        if (lastVisibleItemNumber + visibleTreshold > adapter.getItemCount()) {
            loaderManager.initLoader(++lastPage, null, callbacks);
        }
    }
}
