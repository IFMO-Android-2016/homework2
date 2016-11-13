package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    ScrollView sv;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        getSupportLoaderManager().initLoader(0, null, this);
        sv = (ScrollView) findViewById(R.id.sv);
        layout = (LinearLayout) findViewById(R.id.layout);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        for (int i = 0; i < data.data.size() * 10; i++) {
            TextView tv = new TextView(this);
            tv.setText("keke "  + i);
            layout.addView(tv);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {

    }
}
