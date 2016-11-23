package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

import ru.ifmo.droid2016.tmdb.MoviesRecyclerAdapter;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorView;
    private MoviesRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        errorView = (TextView) findViewById(R.id.error_text);

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.INVISIBLE);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(getResources().getColor(R.color.aX6)));
        Bundle loadExArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, loadExArgs, this);

    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        if (data.resultType == ResultType.OK) {
            if (data.data != null && !data.data.isEmpty()) {
                displayData(data.data);
            } else {
                displayEmptyResult();
            }
        } else {
            displayErrorMessage(data.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        displayEmptyResult();
    }

    private void displayEmptyResult() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText("Movies haven't been founded");
    }

    private void displayErrorMessage(ResultType resultType) {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        if (resultType == ResultType.NO_INTERNET) {
            errorView.setText("No Internet connection");
        } else {
            errorView.setText("Error");
        }
    }

    private void displayData(List<Movie> movies) {
        if (adapter == null) {
            adapter = new MoviesRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        adapter.setMovies(movies);
    }
}
