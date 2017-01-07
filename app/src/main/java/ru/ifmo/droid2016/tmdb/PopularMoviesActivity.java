package ru.ifmo.droid2016.tmdb;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import android.net.Uri;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.FilmsLoader;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;
import ru.ifmo.droid2016.tmdb.utils.ScrollListener;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */

public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private RecyclerView filmsRecyclerView;
    private TextView errorTextView;
    private Button tryAgainButton;
    private ProgressBar progressBar;
    private ScrollListener scrollListener;
    private int page = 0;

    @Nullable
    private RecyclerAdapter adapter;

    @NonNull
    private List<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        filmsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        errorTextView = (TextView) findViewById(R.id.error_message);
        tryAgainButton = (Button) findViewById(R.id.try_again);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        filmsRecyclerView.setLayoutManager(linearLayoutManager);

        scrollListener = new ScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextData();
            }
        };
        filmsRecyclerView.addOnScrollListener(scrollListener);

        filmsRecyclerView.addItemDecoration(new RecylcerDividersDecorator(Color.LTGRAY));
        tryAgainButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);

        final Bundle leaderArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, leaderArgs, this);

    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        displayEmptyData();
    }


    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        if (result.resultType != ResultType.OK) {
            displayError(result.resultType);
        } else if (result.data != null && !result.data.isEmpty()) {
            movies.addAll(result.data);
            displayData(result.data);
        } else
            displayEmptyData();
    }


    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        page++;
        return new FilmsLoader(this, page);
    }

    private void displayEmptyData() {
        progressBar.setVisibility(View.GONE);
        filmsRecyclerView.setVisibility(View.GONE);
        tryAgainButton.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.not_found);
    }

    private void displayData(List<Movie> films) {
        if(adapter == null){
           adapter = new RecyclerAdapter(this);
           filmsRecyclerView.setAdapter(adapter);
        }
        adapter.setMovies(films);

        progressBar.setVisibility(View.GONE);
        filmsRecyclerView.setVisibility(View.VISIBLE);
        tryAgainButton.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
    }

    private void displayError(ResultType type) {
        progressBar.setVisibility(View.GONE);
        filmsRecyclerView.setVisibility(View.GONE);
        tryAgainButton.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
        if (type == ResultType.ERROR)
            errorTextView.setText(R.string.error);
        else
            errorTextView.setText(R.string.internet_connection);
    }

    public void tryAgain(View v) {
        Log.d("PAGE", Integer.toString(page));
        page--;
        loadNextData();
    }

    public void loadNextData() {
        progressBar.setVisibility(View.VISIBLE);
        getSupportLoaderManager().restartLoader(0, null, this);
    }


}
