package ru.ifmo.droid2016.tmdb;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.loader.TMBDLoader;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private static String lang = Locale.getDefault().getLanguage();
    boolean changedLanguage = false;

    private RecyclerView recyclerView;
    private ProgressBar progressView;
    private TextView errorTextView;

    @Nullable
    private TmbdRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        progressView = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error_text);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.colorPrimary)));

        progressView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        final Bundle loaderArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, loaderArgs, this);
    }


    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader,
                               LoadResult<List<Movie>> result) {
        if (result.resultType == ResultType.OK) {
            Log.d(TAG, "ResultType is OK");
            if (result.data != null && !result.data.isEmpty()) {
                Log.d(TAG, "[loader " + loader.getId() + " finished with " + result.data.size() + " movies]");
                displayNonEmptyData(result.data);
            } else {
                displayEmptyData();
            }
        } else {
            displayError(result.resultType);
            Log.d(TAG, "There are some problems with app");
        }
    }

    @Override
    protected void onResume() {
        if (!lang.equals(Locale.getDefault().getLanguage())) {
            lang = Locale.getDefault().getLanguage();
            getSupportLoaderManager().restartLoader(0, null, this);
        }
        super.onResume();
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new TMBDLoader(this, lang);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        displayEmptyData();
    }

    private void displayEmptyData() {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.error);
    }

    private void displayNonEmptyData(List<Movie> movies) {
        if (adapter == null) {
            adapter = new TmbdRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.setMovies(movies);
        progressView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayError(ResultType resultType) {
        Log.d(TAG, "We enter to Error method");
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        final int messageResId;
        if (resultType == ResultType.NO_INTERNET) {

            messageResId = R.string.internet_error;
        } else {
            messageResId = R.string.error;
        }
        errorTextView.setText(messageResId);
    }

    private static final String TAG = "MovieActivity";


}
