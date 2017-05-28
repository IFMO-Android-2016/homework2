package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

public class PopularMoviesActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private MoviesRecyclerAdapter adapter;

    private ProgressBar progress;
    private TextView errorText;
    private RecyclerView rec;

    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_popular_movies);
        progress = (ProgressBar) findViewById(R.id.progress);

        errorText = (TextView) findViewById(R.id.errorText);
        rec = (RecyclerView) findViewById(R.id.recycler);
        rec.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MoviesRecyclerAdapter(this);
        rec.setAdapter(adapter);
        rec.addItemDecoration(new RecylcerDividersDecorator(R.color.red));

        displayProgress();

        lang = Locale.getDefault().getLanguage();

        Bundle bundle = new Bundle();
        bundle.putString("language", lang);

        if (savedInstanceState != null && !lang.equals(savedInstanceState.getString("language"))) {
            getSupportLoaderManager().restartLoader(0, bundle, this);
        }
        getSupportLoaderManager().initLoader(0, bundle, this);
    }

    private void displayProgress() {
        progress.setVisibility(View.VISIBLE);
        rec.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle x) {
        super.onSaveInstanceState(x);
        x.putString("language", lang);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        String lang = Locale.getDefault().getLanguage();
        Log.d(TAG, "onCreateLoader");
        return new MoviesLoader(this, lang);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        Log.d(TAG, "onLoaderReset");
        displayEmptyData();
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader,
                               LoadResult<List<Movie>> result) {
        Log.d(TAG, "onLoadFinished");
        if (result.resultType == ResultType.OK) {
            adapter.setMovies(result.data);
            displayMovies();
        } else {
            errorText.setText(result.resultType.toString());
            displayError();
        }
    }

    private void displayEmptyData() {
        Log.d(TAG, "DisplayEmptyData");
        rec.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        errorText.setText("Popular movies weren't founded");
        errorText.setVisibility(View.VISIBLE);
    }

    private void displayMovies() {
        Log.d(TAG, "displayMovies");
        rec.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
    }

    private void displayError() {
        Log.d(TAG, "displayError");
        errorText.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        rec.setVisibility(View.GONE);
    }

    private static String TAG = "main";
}
