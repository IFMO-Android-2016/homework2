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
import ru.ifmo.droid2016.tmdb.loader.MoviePostersLoader;
import ru.ifmo.droid2016.tmdb.loader.MoviesRecyclerAdapter;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private static final String FIRSTLOAD = "FIRSTLOAD";
    private static final String LOADMORE = "LOADMORE";
    private static final String LANGUAGE = "LANGUAGE";

    ProgressBar progressView;
    TextView errorTextView;
    RecyclerView recyclerView;
    private String language = null;

    @Nullable
    private MoviesRecyclerAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("Activity####", "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        progressView = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.gray_a)));
        if (adapter == null) {
            Log.d("Activity", "New adapter");
            adapter = new MoviesRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }

        String currentLang = getResources().getConfiguration().locale.getLanguage();
        Log.d("ACtivity###CurrentLang", currentLang);

        progressView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText("Происходит загрузка данных");
        recyclerView.setVisibility(View.GONE);

        final Bundle loaderArgs = new Bundle();
        language = currentLang;
        Log.d("Language", language);
        loaderArgs.putString(LANGUAGE, language);
        getSupportLoaderManager().initLoader(0, loaderArgs, this);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        Log.d("Loader#####", "Created");
        return new MoviePostersLoader(this, new int[]{1}, args.getString(LANGUAGE));
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        Log.d("Loader#####", "Finish");
        assert data.data != null;
        assert adapter != null;
        Log.d("Adapter size", String.valueOf(adapter.getItemCount()));
        if ((data.resultType == ResultType.OK && data.data.size() != 0) || adapter.getItemCount() != 0) {
            if (data.data.size() != 0) {
                adapter.setMovies(data.data);
            }
            if (adapter.getItemCount() == 0)
                displayEmptyData();
            else
                displayNonEmptyData();
        } else {
            displayError(data.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        Log.d("Loader#####", "Reset");
        displayEmptyData();
    }


    private void displayEmptyData() {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.no_movies_found);
    }


    private void displayNonEmptyData() {
        progressView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayError(ResultType resultType) {
        Log.d("displray error", resultType.toString());
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        final int messageResId;
        if (resultType == ResultType.NO_INTERNET) {
            messageResId = R.string.no_internet;
        } else {
            messageResId = R.string.error;
        }
        errorTextView.setText(messageResId);
    }

}
