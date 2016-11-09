package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.InputStream;
import java.util.List;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.PopularMoviesLoader;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private final String LOG_TAG = "my_tag";

    private final String LANG = "LANG";
    private Bundle mBundle;
    private Loader<LoadResult<List<Movie>>> mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        mBundle = new Bundle();
        mBundle.putString(LANG, "en-US");
        mBundle.putInt("PAGE_ID", 1);
        mLoader = getSupportLoaderManager().initLoader(0, mBundle, this);

        mLoader.onContentChanged();
    }

    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        Loader<LoadResult<List<Movie>>> mLoader = null;

        Log.d(LOG_TAG, "starting ###");

        mLoader = new PopularMoviesLoader(this, args);

        return mLoader;
    }


    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
    }

    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> rez) {
        Log.d(LOG_TAG, "rez : " + rez.resultType);

    }

}
