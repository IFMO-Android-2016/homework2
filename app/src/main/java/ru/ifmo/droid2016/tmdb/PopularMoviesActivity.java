package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.loader.TmdbLoader;
import ru.ifmo.droid2016.tmdb.model.Movie;


/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private static final String TAG = PopularMoviesActivity.class.getName();

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        Log.d(TAG, "onCreate: ");

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);

        final Bundle loaderArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, loaderArgs, this).forceLoad();
    }


    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new TmdbLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        Log.d(TAG, "onLoadFinished: ");
        if (data.resultType == ResultType.OK) {
            if (data.data != null && !data.data.isEmpty()) {
                displayNonEmptyData(data.data);
            } else {
                displayEmptyData();
            }
        } else {
            displayError(data.resultType);
        }
    }

    private void displayError(ResultType resultType) {
        Log.d(TAG, "displayError: " + resultType);
        String errorMessage = "Error occurred";
        if (resultType == ResultType.NO_INTERNET) {
            errorMessage = "Check your internet connection";
        }
        Toast.makeText(PopularMoviesActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//        final Snackbar snack = Snackbar.make(recyclerView, errorMessage, Snackbar.LENGTH_INDEFINITE);
//        snack.setAction("Dismiss", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        snack.dismiss();
//                    }
//                });
//        snack.show();
    }

    private void displayEmptyData() {
        Log.d(TAG, "displayEmptyData: ");
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        Log.d(TAG, "onLoaderReset: ");
        displayEmptyData();
    }

    private void displayNonEmptyData(List<Movie> data) {
        Log.d(TAG, "displayNonEmptyData: " + adapter);
        if (adapter == null) {
            Log.d(TAG, "displayNonEmptyData: wtf? why on earth adapter is null?");
            adapter = new MovieAdapter();
            recyclerView.setAdapter(adapter);
        }
        adapter.addMovies(data);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

    }
}
