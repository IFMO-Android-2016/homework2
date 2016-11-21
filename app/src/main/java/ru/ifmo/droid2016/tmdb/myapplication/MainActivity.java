package ru.ifmo.droid2016.tmdb.myapplication;

import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import ru.ifmo.droid2016.tmdb.myapplication.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.myapplication.loader.MoviesDownloader;
import ru.ifmo.droid2016.tmdb.myapplication.loader.ResultType;
import ru.ifmo.droid2016.tmdb.myapplication.model.Movie;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    private String lastLang = Locale.getDefault().getLanguage();

    private RecyclerView recyclerView;
    private ProgressBar progressView;
    private TextView errorTextView;

    private MoviesRecyclerAdapter adapter;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        Fresco.initialize(this);

        setContentView(R.layout.activity_popular_movies);
        //here should be request permissions
        progressView = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error_text);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        //creating folder on sd card
        File folder = new File(MoviesDownloader.directory.toString());
        if (!folder.exists()) {
            Log.d("creating folder", "" + folder.mkdirs());
        }

        final Bundle loaderArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, loaderArgs, this).forceLoad();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        lastLang = Locale.getDefault().getLanguage();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
        //language changed
        if (!lastLang.equals(Locale.getDefault().getLanguage())){
            final Bundle loaderArgs = getIntent().getExtras();
            getSupportLoaderManager().restartLoader(0, loaderArgs, this).forceLoad();
        }
    }


    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int i, Bundle bundle) {
        return new MoviesDownloader(this, page);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> listLoadResult) {
        if (listLoadResult.resultType == ResultType.OK) {
            display(listLoadResult.data);
        } else {
            displayError(listLoadResult.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        displayEmptyData();
    }

    private void display(List<Movie> movies) {
        if (adapter == null) {
            adapter = new MoviesRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.setMovies(movies);
        progressView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayEmptyData() {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.nothing_found);
    }

    private void displayError(ResultType resultType) {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        if (resultType == ResultType.NO_INTERNET) {
            errorTextView.setText(R.string.no_internet);
        } else {
            errorTextView.setText(R.string.error);
        }
    }

}