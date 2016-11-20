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

    private static final String EXTRA_LANG = "lang";
    private static final String EXTRA_PAGE = "page";

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
        tv = (TextView) findViewById(R.id.error_text);
        tv.setVisibility(View.GONE);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new RecylcerDividersDecorator(R.color.colorPrimaryDark));

        final Bundle extraArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, extraArgs, this);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        final String lang;
        final int page;

        if (args != null && args.containsKey(EXTRA_LANG) && args.containsKey(EXTRA_PAGE)) {
            lang = args.getString(EXTRA_LANG);
            page = args.getInt(EXTRA_PAGE);
        } else {
            lang = Locale.getDefault().getLanguage();
            page = 1;
        }
        return new PopularMoviesLoader(this, lang, page);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        if (data.resultType == ResultType.OK) {
            if (data.data != null && !data.data.isEmpty()) {
                display(data.data);
            } else {
                hide();
            }
        } else {
            error(data.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        hide();
    }

    private void display(List<Movie> data) {
        if (adapter == null) {
            adapter = new MovieRecyclerAdapter(this);
            rv.setAdapter(adapter);
        }
        adapter.setMovies(data);
        pb.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
    }

    private void hide() {
        pb.setVisibility(View.GONE);
        rv.setVisibility(View.GONE);
        tv.setText(R.string.movies_not_found);
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
