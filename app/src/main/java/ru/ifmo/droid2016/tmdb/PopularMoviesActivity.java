package ru.ifmo.droid2016.tmdb;

import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MovieLoader;
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
    private String language = null;

    private boolean flagLoader;
    private MoviesRecyclerAdapter adapter;
    private MovieValue moviesValue;

    private final int difPosition = 4;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page", page);
        outState.putString("language", language);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        FragmentManager fragmentManager = getSupportFragmentManager();
        progressView = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error_text);
        adapter = new MoviesRecyclerAdapter(this);
        moviesValue = (MovieValue) fragmentManager.findFragmentByTag(MovieValue.TAG);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(R.color.colorPrimaryDark));
        recyclerView.setAdapter(adapter);
        if (moviesValue == null) {
            moviesValue = new MovieValue();
            fragmentManager.beginTransaction().add(moviesValue, MovieValue.TAG).commit();
        }
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        final PopularMoviesActivity context = this;

        if (savedInstanceState == null) {
            page = 1;
            language = Locale.getDefault().getLanguage();
            flagLoader = true;
            getSupportLoaderManager().initLoader(page, null, this);
        } else {
            page = savedInstanceState.getInt("page");
            language = savedInstanceState.getString("language");
            displayNonEmptyData((moviesValue).getPresentMovies());
        }

        String presentLanguage = getResources().getConfiguration().locale.getLanguage();
        final Bundle loaderArgs = new Bundle();
        loaderArgs.putString("language", presentLanguage);
        if (language == null) {
            language = presentLanguage;
        }
        if (!language.equals(presentLanguage)) {
            language = presentLanguage;
            getSupportLoaderManager().restartLoader(0, loaderArgs, this);
        } else {
            getSupportLoaderManager().initLoader(0, loaderArgs, this);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!flagLoader) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (linearLayoutManager.findLastVisibleItemPosition() + difPosition > adapter.getItemCount()) {
                        flagLoader = true;
                        getSupportLoaderManager().initLoader(++page, null, context);
                    }
                }
            }
        });
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this, this.language, this.page);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        flagLoader = false;
        if (result.resultType == ResultType.OK) {
            if (result.data != null && !result.data.isEmpty()) {
                displayNonEmptyData(result.data);
                (moviesValue).getPresentMovies().addAll(result.data);
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

    private void displayNonEmptyData(List<Movie> movies) {
        adapter.setMovies(movies);
        progressView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayEmptyData() {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.movies_not_found);
    }

    private void displayError(ResultType resultType) {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        if (resultType == ResultType.NO_INTERNET) {
            errorTextView.setText(R.string.no_internet_connection);
        } else {
            errorTextView.setText(R.string.error);
        }
    }
}