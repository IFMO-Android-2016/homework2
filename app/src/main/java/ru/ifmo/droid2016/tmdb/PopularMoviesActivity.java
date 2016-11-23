package ru.ifmo.droid2016.tmdb;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.MoviesPullParser;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.MoviesRecyclerAdapter;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private RecyclerView moviesRecyclerView;
    private MoviesRecyclerAdapter adapter = null;
    private ProgressBar progressBar;
    private TextView errorTextView;

    private int page = 1;
    private boolean nextPageLoaded = false, langChanged = false;
    private String lang = Locale.getDefault().getLanguage();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("lang", lang);
        outState.putInt("page", page);
        outState.putBoolean("nextPageLoaded", nextPageLoaded);
        Parcelable state = moviesRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("state", state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        lang = savedInstanceState.getString("lang");
        page = savedInstanceState.getInt("page");
        nextPageLoaded = savedInstanceState.getBoolean("nextPageLoaded");
        Parcelable state = savedInstanceState.getParcelable("state");
        moviesRecyclerView.getLayoutManager().onRestoreInstanceState(state);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        errorTextView = (TextView) findViewById(R.id.error_message);
        errorTextView.setVisibility(View.GONE);

        moviesRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        moviesRecyclerView.setVisibility(View.GONE);
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        moviesRecyclerView.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.colorPrimaryDark)));

        initLoaders();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!this.lang.equals(Locale.getDefault().getLanguage())) {
            this.lang = Locale.getDefault().getLanguage();
            langChanged = true;

            destroyLoaders();
            page = 1;
            getSupportLoaderManager().initLoader(page, null, getCallback());
        }

        moviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled (RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!nextPageLoaded && layoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 6
                        && page != MoviesPullParser.totalPages) {
                    nextPageLoaded = true;
                    getSupportLoaderManager().initLoader(++page, null, getCallback());
                }
            }
        });
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int page, Bundle args) {
        return new MoviesLoader(this, lang, page);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        nextPageLoaded = false;
        if (result.resultType == ResultType.OK) {
            if (result.data != null && !result.data.isEmpty()) {
                displayData(result.data);
            } else {
                displayData();
            }
        } else {
            displayError(result.resultType);
        }
    }

    private PopularMoviesActivity getCallback() {
        return this;
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        if (langChanged) {
            adapter = null;
            langChanged = false;
        }
        displayData();
    }

    private void initLoaders() {
        for (int i = 1; i <= page; i++) {
            getSupportLoaderManager().initLoader(i, null, getCallback());
        }
    }

    private void destroyLoaders() {
        for (int i = 1; i <= page; i++) {
            getSupportLoaderManager().destroyLoader(i);
        }
    }

    private void displayData() {
        progressBar.setVisibility(View.GONE);
        moviesRecyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.movies_not_found);
    }

    private void displayData(List<Movie> movies) {
        if (adapter == null) {
            adapter = new MoviesRecyclerAdapter(this);
            moviesRecyclerView.setAdapter(adapter);
        }
        adapter.addMovies(movies);
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        moviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void displayError(ResultType resultType) {
        progressBar.setVisibility(View.GONE);
        moviesRecyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        final int messageResId;
        if (resultType == ResultType.NO_INTERNET) {
            messageResId = R.string.no_inernet;
        } else {
            messageResId = R.string.error;
        }
        errorTextView.setText(messageResId);
    }

    private static final String TAG = "Movies";
}
