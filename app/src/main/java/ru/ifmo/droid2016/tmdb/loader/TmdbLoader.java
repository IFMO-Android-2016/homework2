package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by RinesThaix on 22.11.16.
 */

public class TmdbLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private int currentPage;
    private String language;

    private LoadResult<List<Movie>> lastResult;

    public TmdbLoader(Context context, String lang, int page) {
        super(context);
        this.currentPage = page;
        this.language = lang;
    }

    public TmdbLoader page(int page) {
        this.currentPage = page;
        return this;
    }

    public TmdbLoader language(String lang) {
        this.language = lang;
        return this;
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        return lastResult = TmdbApi.getPopularMovies(language, currentPage);
    }

    @Override
    protected void onStartLoading() {
        if(lastResult == null || lastResult.resultType != ResultType.OK)
            forceLoad();
        else
            deliverResult(lastResult);
    }

}
