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

import javax.annotation.Nullable;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        progressView = (ProgressBar) findViewById(R.id.progress_bar);
        errorTextView = (TextView) findViewById(R.id.error_text);
        errorTextView.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(R.color.green));

        progressView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        // Pass all extra params directly to loader
        final Bundle loaderArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, loaderArgs, this);
    }


    private RecyclerView recyclerView;
    private ProgressBar progressView;
    private TextView errorTextView;

    @Nullable
    private MoviesAdapter adapter;


    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        progressView.setVisibility(View.INVISIBLE);
        if (data.resultType == ResultType.OK) {
            if (adapter == null) {
                adapter = new MoviesAdapter(this);
                recyclerView.setAdapter(adapter);
            }
            adapter.setMovies(data.data);
            recyclerView.setVisibility(View.VISIBLE);
            errorTextView.setVisibility(View.INVISIBLE);
        }
        else if (data.resultType == ResultType.NO_INTERNET) {
            recyclerView.setVisibility(View.INVISIBLE);
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("Waiting connection...");
        }
        else {
            recyclerView.setVisibility(View.INVISIBLE);
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("Something's gone wrong");
        }
    }



    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {

    }
}
