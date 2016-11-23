package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.PopularMoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private final static String KEY_LOADED_PAGES = "LOADED_PAGES";
    private final static String KEY_LOCALE = "LOCALE";
    private final static String KEY_CURR = "CURR";

    private RecyclerView recycler;
    private ProgressBar progressBar;
    private TextView error;
    private LinearLayoutManager layoutManager;

    private MoviesRecyclerAdapter movieAdapter;

    private String locale;
    private int loaded_pages;
    private int curr = 0;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_popular_movies);
        recycler = (RecyclerView)findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.colorPrimary)));
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        error = (TextView) findViewById(R.id.error);

        recycler.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        error.setVisibility(GONE);

        String locale = (state != null) ? state.getString(KEY_LOCALE) : "";
        String localeUpd = Locale.getDefault().getLanguage();

        if (state != null) curr = state.getInt(KEY_CURR);
        loaded_pages = (state != null && locale.equals(localeUpd))?
                state.getInt(KEY_LOADED_PAGES) : 1;
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recycler, int dx, int dy) {
                int last = layoutManager.findLastVisibleItemPosition();
                if (last + 5 > layoutManager.getItemCount()) {
                    loaded_pages++;
                    initLoader(loaded_pages);
                }
            }
        });
        if (!locale.equals(localeUpd)) {
            for (int i = 1; i <= loaded_pages; i++) restartLoader(i);
        }
        initLoader(1);
    }


    private void restartLoader(int lp) {
        getSupportLoaderManager().restartLoader(lp, getIntent().getExtras(), this);
    }


    private void initLoader(int lp) {
        getSupportLoaderManager().initLoader(lp, getIntent().getExtras(), this);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(KEY_LOADED_PAGES, loaded_pages);
        state.putString(KEY_LOCALE, Locale.getDefault().getLanguage());
        state.putInt(KEY_CURR, movieAdapter.curr);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new PopularMoviesLoader(this, loaded_pages);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        if (data.resultType == ResultType.OK && data.data != null && !data.data.isEmpty()) {
            boolean was = false;
            if (movieAdapter == null) {
                movieAdapter = new MoviesRecyclerAdapter(this);
                recycler.setAdapter(movieAdapter);
            } else {
                was = true;
            }
            movieAdapter.pushData(data.data);
            recycler.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
            error.setVisibility(GONE);
            if (curr < movieAdapter.getItemCount() && !was) recycler.scrollToPosition(curr);
        }
        else {
            showError(data.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        showError(ResultType.ERROR);
    }

    private void showError(ResultType result) {
        recycler.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        error.setVisibility(VISIBLE);
        error.setText(result == ResultType.NO_INTERNET ? R.string.error_internet : R.string.error);
    }
}
