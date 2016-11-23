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

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {
    private RecyclerView recyclerView;
    private TextView errorView;
    private ProgressBar progressBar;
    private List<Movie> movies = new ArrayList<>();
    private MovieAdapter adapter;
    private final String errorInternet = "No connection available";
    private final String unknownError = "An error occured";
    private final String emptyResponse = "No movies found";


    private void setDefaultVisibility() {
        progressBar.setVisibility(View.VISIBLE);
        Log.d("PopularMoviesActivity", "Progress bar is visible");
        recyclerView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    private void setVisibilityReady() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    private void setVisibilityOnError() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    private void displayError(String text) {
        setVisibilityOnError();
        errorView.setText(text);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        if (result.resultType == ResultType.ERROR) {
            displayError(unknownError);
        } else if (result.resultType == ResultType.NO_INTERNET) {
            displayError(errorInternet);
        } else {
            setVisibilityReady();
            movies = result.data;
            if (movies == null || movies.size() == 0)
                displayError(emptyResponse);
            else {
                adapter = new MovieAdapter(getBaseContext());
                recyclerView.setAdapter(adapter);
                adapter.setMovies(movies);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.movies_view);
        errorView = (TextView) findViewById(R.id.error_view);
        setDefaultVisibility();
        getSupportLoaderManager().initLoader(0, null, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
