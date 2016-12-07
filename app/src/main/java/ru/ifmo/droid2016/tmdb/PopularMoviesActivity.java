package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import ru.ifmo.droid2016.tmdb.loader.AsyncLoader;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {
    private static final String LOG_TAG = PopularMoviesActivity.class.getSimpleName();
    private static final String VIEW_KEY = "recyclerState";
    private static final String PAGE_KEY = "Page";

    static Loader<LoadResult<List<Movie>>> loader;
    private ProgressBar bar;
    private RecyclerView recyclerView;
    private Parcelable recyclerState;
    public static int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        page = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        if (savedInstanceState != null) {
            recyclerState = savedInstanceState.getParcelable(VIEW_KEY);
        }
        Bundle args = new Bundle();
        args.putInt(PAGE_KEY, page);
        loader = getSupportLoaderManager().initLoader(1, args, this);

        bar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(RecyclerAdapter.getInstance(this, new ArrayList<Movie>()));
        bar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(VIEW_KEY, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new AsyncLoader(this, args.getInt(PAGE_KEY));
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {

    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        if (data.resultType == ResultType.OK) {
            Log.d(LOG_TAG, "finish " + Integer.toString(data.data.size()));
            recyclerState = recyclerView.getLayoutManager().onSaveInstanceState();
            RecyclerAdapter adapter = RecyclerAdapter.getInstance(this, data.data);


            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerState);

            bar.setVisibility(View.INVISIBLE);
        }
    }
}
