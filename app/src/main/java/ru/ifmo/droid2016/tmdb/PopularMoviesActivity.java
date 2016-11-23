package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.PagesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    public static String TAG = "PopularMovies";

    ProgressBar progressBar;
    RecyclerView recyclerView;
    TextView error;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        error = (TextView) findViewById(R.id.error_text);
        error.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(R.color.colorPrimary));

        Bundle b = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, b, this);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        return new PagesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        Log.d(TAG, "onLoadFinished");

        recyclerView.setVisibility(View.VISIBLE);
        error.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        if (data.resultType == ResultType.OK) {
            if (adapter == null) {
                adapter = new Adapter(this);
                recyclerView.setAdapter(adapter);
            }
            adapter.updateData(data.data);
        } else {
            //Error while loading has happened
            recyclerView.setVisibility(View.INVISIBLE);
            error.setVisibility(View.VISIBLE);

            if (data.resultType == ResultType.NO_INTERNET)
                error.setText("No Internet Connection");
            else
                error.setText("Unknown error happened");
        }

    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
    }
}
