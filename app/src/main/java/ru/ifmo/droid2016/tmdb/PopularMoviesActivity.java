package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.loader.PopularMoviesLoader;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<String> {

    private final String LOG_TAG = "my_tag";

    private final String LANG = "LANG";
    private Bundle mBundle;
    private Loader<String> mLoader;

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

    public Loader<String> onCreateLoader(int id, Bundle args) {
        Loader<String> mLoader = null;

        Log.d(LOG_TAG, "starting ###");

        mLoader = new PopularMoviesLoader(this, args);

        return mLoader;
    }


    public void onLoaderReset(Loader<String> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
    }

    public void onLoadFinished(Loader<String> loader, String rez) {
        Log.d(LOG_TAG, "rez : " + rez);
    }

}
