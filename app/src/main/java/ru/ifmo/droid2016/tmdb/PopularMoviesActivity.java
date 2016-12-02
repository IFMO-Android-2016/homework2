package ru.ifmo.droid2016.tmdb;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.PopularMovieLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {
    private static final int LOADER_ID = 1;

//    private static final String TAG = PopularMoviesActivity.class.getName();

    private RecyclerView recyclerView;
    private MoviesAdapter adapter = new MoviesAdapter();
    private ProgressBar progress;
    private TextView error;
    private TextView noInternet;
    Loader<LoadResult<List<Movie>>> loader;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        progress = (ProgressBar) findViewById(R.id.progress);
        error = (TextView) findViewById(R.id.error_while_loading);
        noInternet = (TextView) findViewById(R.id.no_internet_connection);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Bundle bundle = new Bundle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bundle.putString(PopularMovieLoader.LOCALE, getResources().getConfiguration().getLocales().get(0).getLanguage());
        } else {
            bundle.putString(PopularMovieLoader.LOCALE, getResources().getConfiguration().locale.getLanguage());
        }

        loader = getLoaderManager().initLoader(LOADER_ID, bundle, this);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int i, Bundle bundle) {
        return new PopularMovieLoader(this, bundle);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> listLoadResult) {
        progress.setVisibility(View.GONE);
        if (listLoadResult.resultType == ResultType.OK) {
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setMovies(listLoadResult.data);
        }
        if (listLoadResult.resultType == ResultType.ERROR) {
            error.setVisibility(View.VISIBLE);
        }
        if (listLoadResult.resultType == ResultType.NO_INTERNET) {
            noInternet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
    }
}
