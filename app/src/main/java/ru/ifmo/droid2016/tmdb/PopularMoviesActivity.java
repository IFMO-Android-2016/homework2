package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.FilmsListLoader;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {
    private static final String TAG = "MyTag";
    private static final String RECYCLER_TAG = "recycler";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private Button tryAgainButton;
    private EndlessRecyclerViewScrollListener scrollListener;
    private int page = 0;

    @NonNull
    private List<Movie> movies = new ArrayList<>();

    @Nullable
    private FilmsRecyclerAdapter adapter;

    public void tryAgain(View v) {
        Log.d("PAGE", Integer.toString(page));
        --page;
        loadNextData();
    }

    public void loadNextData() {
        progressBar.setVisibility(View.VISIBLE);
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        recyclerView = (RecyclerView) findViewById(R.id.reycler);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error_text);
        tryAgainButton = (Button) findViewById(R.id.try_again);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextData();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(R.color.colorPrimaryDark));

        progressBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        final Bundle leaderArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, leaderArgs, this);

    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        ++page;
        return new FilmsListLoader(this, page);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        if (result.resultType == ResultType.OK) {
            if (result.data != null && !result.data.isEmpty()) {
                movies.addAll(result.data);
                displayNonEmptyData(movies);
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
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tryAgainButton.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.films_not_found);
    }

    private void displayNonEmptyData(List<Movie> movies) {
        if (adapter == null) {
            adapter = new FilmsRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.setMovies(movies);
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        tryAgainButton.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayError(ResultType resultType) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tryAgainButton.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
        final int messageResId;
        if (resultType == ResultType.NO_INTERNET) {
            messageResId = R.string.no_inernet;
        } else {
            messageResId = R.string.error_text;
        }
        errorTextView.setText(messageResId);
    }

}
