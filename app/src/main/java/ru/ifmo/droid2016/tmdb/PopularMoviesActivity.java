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
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.PopularMoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private ProgressBar pb;
    private RecyclerView rv;
    private TextView tv;
    @Nullable
    private MovieRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        init();
    }

    private void init() {
        pb = (ProgressBar) findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);
        rv = (RecyclerView) findViewById(R.id.recycler);
        rv.setVisibility(View.GONE);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new RecylcerDividersDecorator(R.color.colorPrimaryDark));
        tv = (TextView) findViewById(R.id.error_text);
        tv.setVisibility(View.GONE);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rv.clearOnScrollListeners();
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new PopularMoviesLoader(this, Locale.getDefault().getLanguage(), id);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        if (data.resultType == ResultType.OK) {
            display(data.data);
        } else {
            error(data.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        hide();
    }

    private void hide() {
        pb.setVisibility(View.GONE);
        rv.setVisibility(View.GONE);
        tv.setText(R.string.movies_not_found);
    }

    private void display(List<Movie> data) {
        if (adapter == null) {
            adapter = new MovieRecyclerAdapter(this);
            rv.setAdapter(adapter);
            rv.addOnScrollListener(new EndlessScroller(getSupportLoaderManager(), adapter, this));
        }
        adapter.addMovies(data);
        pb.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
    }

    private void error(ResultType resultType) {
        pb.setVisibility(View.GONE);
        rv.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);

        if (resultType == ResultType.NO_INTERNET) {
            tv.setText(R.string.no_internet);
        } else {
            tv.setText(R.string.error);
        }
    }
}
