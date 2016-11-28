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

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MovieLoader;
import ru.ifmo.droid2016.tmdb.loader.MoviesRecyclerAdapter;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {


    private static final String LANGUAGE = "LANGUAGE";

    ProgressBar progressBar;
    TextView errorText;
    RecyclerView recycler;
    private String language = null;

    @Nullable
    private MoviesRecyclerAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        errorText = (TextView) findViewById(R.id.errorText);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.gray_a)));
        if (adapter == null) {
            adapter = new MoviesRecyclerAdapter(this);
            recycler.setAdapter(adapter);
        }

        String currentLang = getResources().getConfiguration().locale.getLanguage();

        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.VISIBLE);
        errorText.setText("Загрузка данных");
        recycler.setVisibility(View.GONE);

        final Bundle loaderArgs = new Bundle();
        language = currentLang;
        loaderArgs.putString(LANGUAGE, language);
        getSupportLoaderManager().initLoader(0, loaderArgs, this);
    }


    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        assert data.data != null;
        assert adapter != null;
        if ((data.resultType == ResultType.OK && data.data.size() != 0) || adapter.getItemCount() != 0) {
            if (data.data.size() != 0) {
                adapter.setMovies(data.data);
            }
            if (adapter.getItemCount() == 0)
                displayEmpty();
            else
                displayData();
        } else {
            displayError(data.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        displayEmpty();
    }

    private void displayData() {
        progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
        recycler.setVisibility(View.VISIBLE);
    }

    private void displayError(ResultType resultType) {
        recycler.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.VISIBLE);
        final int message;
        if (resultType == resultType.NO_INTERNET) {
            message = R.string.noInternet;
        } else {
            message = R.string.error;
        }
        errorText.setText(message);
    }

    private void displayEmpty() {
        recycler.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.VISIBLE);
        errorText.setText(R.string.noFilms);
    }

}
