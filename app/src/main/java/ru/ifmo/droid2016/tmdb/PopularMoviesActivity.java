package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import ru.ifmo.droid2016.tmdb.loader.MovieLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {
    private RecyclerView recyclerView;
    private ProgressBar progressView;
    private TextView errorTextView;


    @Nullable
    private WebcamsRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);


        progressView = (ProgressBar) findViewById(R.id.progressBar);
        errorTextView = (TextView) findViewById(R.id.error_massage);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.colorPrimaryDark)));

        progressView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        // Pass all extra params directly to loader
        final Bundle loaderArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, loaderArgs, this);
    }


    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {

    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {

    }

    private void displayEmptyData() {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void displayNonEmptyData(List<Movie> webcams) {
        if (adapter == null) {
            adapter = new WebcamsRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.setWebcams(webcams);
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
            messageResId = R.string.no_inernet;
        } else {
            messageResId = R.string.error;
        }
        errorTextView.setText(messageResId);
    }

    private static final String TAG = "Webcams";
}
