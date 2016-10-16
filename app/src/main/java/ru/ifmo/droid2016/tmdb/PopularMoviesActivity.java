package ru.ifmo.droid2016.tmdb;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import ru.ifmo.droid2016.tmdb.adapter.PopularMoviesRecyclerAdapter;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.PopularMoviesLoader;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    ProgressBar progress;
    RecyclerView recycler;

    PopularMoviesRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        progress = (ProgressBar) findViewById(R.id.progress);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        progress.setVisibility(View.VISIBLE);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        final Bundle loaderArgs = getIntent().getExtras();
        getLoaderManager().initLoader(0, loaderArgs, this);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new PopularMoviesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        adapter = new PopularMoviesRecyclerAdapter(this, result.data);
        recycler.setAdapter(adapter);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {

    }
}
