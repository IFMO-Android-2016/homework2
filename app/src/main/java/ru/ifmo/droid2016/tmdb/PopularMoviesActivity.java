package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
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
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.EndlessRecyclerViewScrollListener;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private static final String TAG = "ACTIVITY";
    private RecyclerView recyclerView;
    private ProgressBar progressView;
    private TextView errorTextView;

    @NonNull
    private ArrayList<Movie> movies = new ArrayList<>();
    private boolean firstLoad = true;

    private MoviesRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());

        progressView = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        progressView.setIndeterminate(true);

        progressView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(ContextCompat.getColor(this, R.color.colorDivider)));

        movies.add(null);
        adapter = new MoviesRecyclerAdapter(this, movies);
        recyclerView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(0, savedInstanceState, this);
    }

    private void loadMovies(int page) {
        movies.remove(movies.size() - 1);

        for (int i = 1; i <= 10; i++) {
            movies.add(new Movie(i, "#", "Original title", "Text", "Local title " + page + ":" + i, 5.0));
        }

        Handler handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void displayLoader() {
        if (firstLoad) {
            progressView.setVisibility(View.VISIBLE);
            errorTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            adapter.setLoading(true);
        }
    }

    public void displayMovies() {
        if (firstLoad) {
            progressView.setVisibility(View.GONE);
            errorTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            adapter.setLoading(false);
        }
    }

    public void displayError(ResultType resultType) {
        final int message;
        switch (resultType) {
            case NO_INTERNET:
                message = R.string.no_internet;
                break;
            case OK:
                message = R.string.movies_not_found;
                break;
            case ERROR:
            default:
                message = R.string.error;
        }
        if (movies.size() == 0) {
            errorTextView.setText(message);
            progressView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            errorTextView.setVisibility(View.VISIBLE);
        } else {
            adapter.setLoading(false);
            adapter.setErrorText(message);
        }
    }

    private void addMovies(List<Movie> data) {
        movies.remove(movies.size() - 1);
        movies.addAll(data);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void addMoviesFirst(List<Movie> data) {
        addMovies(data);
        displayMovies();
        EndlessRecyclerViewScrollListener listener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                movies.add(null);
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    public void run() {
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                });
                Bundle args = new Bundle();
                args.putInt("page", page);
                args.putString("lang", Locale.getDefault().getLanguage());
                getSupportLoaderManager().initLoader(page, args, PopularMoviesActivity.this);
            }
        };
        listener.setPageIndex(1);
        recyclerView.addOnScrollListener(listener);
        firstLoad = false;
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader() called");
        if (args == null) {
            args = new Bundle();
        }
        return new MoviesLoader(this,
                args.getString(Locale.getDefault().getLanguage(), "en-US"),
                args.getInt("page", 1));
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        Log.i(TAG, "onLoadFinished() called");
        if (result.resultType == ResultType.OK && result.data != null) {
            if (firstLoad) {
                addMoviesFirst(result.data);
            } else {
                addMovies(result.data);
            }
        } else {
            displayError(result.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        // displayEmptyData();
    }
}