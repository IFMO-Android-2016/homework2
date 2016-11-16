package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RequestType;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private LoaderManager loaderManager;
    private MovieRecyclerAdapter adapter;
    private LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> callbacks;
    private final int visibleThreshold = 7;
    int lastPage = 0;
    private int lastFinishedLoader;

    public EndlessScrollListener(LoaderManager lm, MovieRecyclerAdapter adapter, int lastPage,
                                 LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> callbacks,
                                 int lastFinishedLoader) {
        loaderManager = lm;
        this.adapter = adapter;
        this.callbacks = callbacks;
        this.lastPage = lastPage;
        this.lastFinishedLoader = lastFinishedLoader;
    }

    public void setLastFinishedLoader(int lastFinishedLoader) {
        this.lastFinishedLoader = Math.max(this.lastFinishedLoader, lastFinishedLoader);
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemNumber = adapter.getLastVisibleItemNumber();
        if (lastVisibleItemNumber + visibleThreshold > adapter.getItemCount()) {
            Bundle args = new Bundle();
            args.putIntArray(PopularMoviesActivity.PAGES, new int[]{lastPage + 1});
            args.putInt(PopularMoviesActivity.REQUEST_TYPE, RequestType.LOAD_MORE);
            if (lastFinishedLoader == lastPage) {
                loaderManager.initLoader(++lastPage, args, callbacks);
            }
        }
    }
}
