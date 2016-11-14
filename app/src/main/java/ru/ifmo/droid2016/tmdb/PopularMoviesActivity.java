package ru.ifmo.droid2016.tmdb;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import ru.ifmo.droid2016.tmdb.adapter.MoviesRecyclerAdapter;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */


public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {


    private RecyclerView recyclerView;
    private TextView errorTextView;
    private ProgressBar progressView;

    private int page;
    private String language;

    private boolean flagForLoader;

    @Nullable
    private MoviesRecyclerAdapter adapter;
    private MoviesData moviesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        FragmentManager fm = getSupportFragmentManager();

        progressView = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error_text);

        adapter = new MoviesRecyclerAdapter(this);
        moviesData = (MoviesData) fm.findFragmentByTag(MoviesData.TAG);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(R.color.colorPrimaryDark));
        recyclerView.setAdapter(adapter);

        if (moviesData == null) {
            Log.w("moviesData", "I restart");
            moviesData = new MoviesData();
            fm.beginTransaction().add(moviesData, MoviesData.TAG).commit();
        }

        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        final PopularMoviesActivity context = this;

        if (savedInstanceState != null) {
            language = savedInstanceState.getString("language");
            page = savedInstanceState.getInt("page");
            displayNonEmptyData((moviesData).getCurrentMovies());
        } else {
            page = 1;
            language = Locale.getDefault().getLanguage();
            flagForLoader = true;
            getSupportLoaderManager().initLoader(page, null, this);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (flagForLoader) {
                    return;
                }
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (llm.findLastVisibleItemPosition() + 5 >= adapter.getItemCount()) {
                    flagForLoader = true;
                    getSupportLoaderManager().initLoader(++page, null, context);
                }
            }
        });
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(this, this.language, this.page);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        flagForLoader = false;
        if (result.resultType == ResultType.OK) {
            if (result.data != null && !result.data.isEmpty()) {
                displayNonEmptyData(result.data);
                (moviesData).getCurrentMovies().addAll(result.data);
            } else {
                displayEmptyData();
            }
        } else {
            displayError(result.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        displayEmptyData();
    }


    private void displayEmptyData() {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.movies_not_found);
    }

    private void displayNonEmptyData(List<Movie> movies) {
        adapter.addMovies(movies);
        progressView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayError(ResultType resultType) {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        final int messageResId;
        if (resultType == ResultType.NO_INTERNET) {
            messageResId = R.string.no_internet;
        } else {
            messageResId = R.string.error;
        }
        errorTextView.setText(messageResId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("language", language);
        outState.putInt("page", page);
    }
}
