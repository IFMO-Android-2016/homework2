package ru.ifmo.droid2016.tmdb;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.adapter.PopularMoviesRecyclerAdapter;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.PopularMoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    ProgressBar progress;
    RecyclerView recycler;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        progress = (ProgressBar) findViewById(R.id.progress);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        error = (TextView) findViewById(R.id.error);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        final Bundle loaderArgs = getIntent().getExtras();
        getLoaderManager().initLoader(0, loaderArgs, this);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        progress.setVisibility(View.VISIBLE);
        return new PopularMoviesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader,
                               LoadResult<List<Movie>> result) {
        if (result.resultType == ResultType.OK && result.data != null && !result.data.isEmpty()) {
            error.setVisibility(View.GONE);
            recycler.setAdapter(new PopularMoviesRecyclerAdapter(this, result.data));
            recycler.setVisibility(View.VISIBLE);
        } else {
            recycler.setVisibility(View.GONE);
            final int errorTextResId;

            if (result.data != null && result.data.isEmpty()) {
                errorTextResId = R.string.no_movies_found;
            } else if (result.resultType == ResultType.NO_INTERNET) {
                errorTextResId = R.string.no_inernet;
            } else {
                errorTextResId = R.string.unexpected_error;
            }

            error.setText(errorTextResId);
            error.setVisibility(View.VISIBLE);
        }

        progress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        recycler.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
    }
}
