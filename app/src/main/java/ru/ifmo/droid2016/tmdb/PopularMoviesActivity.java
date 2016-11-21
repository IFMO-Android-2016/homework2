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
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.loader.TmdbLoader;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecyclerDividersDecorator;

import static ru.ifmo.droid2016.tmdb.loader.ResultType.ERROR;
import static ru.ifmo.droid2016.tmdb.loader.ResultType.OK;
import static ru.ifmo.droid2016.tmdb.utils.Constants.ITEMS_ON_PAGE;

public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private static final String TAG = "PopularMoviesActivity";

    private static String lang = Locale.getDefault().getLanguage();
    private static boolean langChanged = false;
    private static int id = 0;

    private RecyclerView recyclerView;
    private ProgressBar progressView;
    private TextView errorTextView;
    private LinearLayoutManager lm;

    @Nullable
    private RecyclerAdapter adapter;


    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);


        progressView = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.addItemDecoration(new RecyclerDividersDecorator(
                getResources().getColor(R.color.divier)));


        progressView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        loadLoaders();
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new TmdbLoader(this, lang, ++id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", id);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        id = savedInstanceState.getInt("id");
    }

    @Override
    protected void onResume() {
        if (!lang.equals(Locale.getDefault().getLanguage())) {
            removeLoaders();
            lang = Locale.getDefault().getLanguage();
            langChanged = true;
            id = 0;
            getSupportLoaderManager().restartLoader(id, null, this);
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (lm == null)
                    lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (lm.findLastVisibleItemPosition() >= ((id + 1) * ITEMS_ON_PAGE) - 4) {
                    getSupportLoaderManager().initLoader(++id, null, getCallback());
                }
            }
        });
        super.onResume();
    }

    private void removeLoaders() {
        for (int i = 0; i <= id; i++) {
            getSupportLoaderManager().destroyLoader(i);
        }
    }

    private void loadLoaders() {
        for (int i = 0; i <= id; i++) {
            getSupportLoaderManager().initLoader(i, null, getCallback());
        }
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        Log.i(TAG, "onLoadFinished: loaderId=" + loader.getId() + " finished");
        if (result.resultType == OK) {
            display(result);
        } else {
            error(result.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        adapter = null;
    }

    private void display(LoadResult<List<Movie>> result) {
        if (result.data.isEmpty()) {
            error(ERROR);
        } else {
            if (adapter == null) {
                adapter = new RecyclerAdapter(this);
                recyclerView.setAdapter(adapter);
            }
            if (langChanged) adapter.setMovies(result);
            else adapter.addMovies(result);
            langChanged = false;
            progressView.setVisibility(View.GONE);
            errorTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void error(ResultType error) {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(error.toString());
    }

    private PopularMoviesActivity getCallback() {
        return this;
    }

}
