package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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

    private RecyclerView recyclerView;
    private TextView errorView;
    private ProgressBar progressView;
    String language;

    @Nullable
    private MoviesRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        progressView = (ProgressBar) findViewById(R.id.progress_bar);
        errorView = (TextView) findViewById(R.id.error_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.purple)));

        if (savedInstanceState != null) {
            language = savedInstanceState.getString("LANGUAGE");
        }

        progressView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText("Загрузка данных...");
        recyclerView.setVisibility(View.GONE);

        String newLang = getResources().getConfiguration().locale.getLanguage();
        final Bundle loaderArgs = new Bundle();
        if (language == null) {
            language = newLang;
            Log.d("Language", "empty language");
        } else {
            Log.d("Language", language);
        }
        Log.d("Language", newLang);
        if (!language.equals(newLang)) {
            language = newLang;
            getSupportLoaderManager().restartLoader(0, loaderArgs, this);
        } else {
            getSupportLoaderManager().initLoader(0, loaderArgs, this);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        savedState.putString("LANGUAGE", language);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        Log.d("Loading", "star");
        return (new MoviesLoader(this, language));
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        Log.d("Loading", "reset");
        displayEmptyData();
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader,
                               LoadResult<List<Movie>> result) {
        Log.d("Loading", "finished " + language);
        if (result.resultType == ResultType.OK) {
            if (adapter == null) {
                adapter = new MoviesRecyclerAdapter(this);
                recyclerView.setAdapter(adapter);
            }

            if (result.data != null && !result.data.isEmpty()) {
                displayNonEmptyData(result.data);
            } else {
                displayEmptyData();
            }
        } else {
            displayError(result.resultType);
        }
    }

    private void displayNonEmptyData(List<Movie> movies) {
        if (adapter == null) {
            adapter = new MoviesRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.setMovies(movies);
        progressView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayError(ResultType resultType) {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        final int messageResId;
        if (resultType == ResultType.NO_INTERNET) {
            messageResId = R.string.no_inernet;
        } else {
            messageResId = R.string.error;
        }
        errorView.setText(messageResId);
    }

    private void displayEmptyData() {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(R.string.movies_not_found);
    }
}
