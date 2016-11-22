package ru.ifmo.droid2016.tmdb.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

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
        if (adapter == null) {
            adapter = new MoviesRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }

        //creating folder on sd card
        File folder = new File(MoviesDownloader.directory.toString());
        if (!folder.exists()) {
            Log.d("creating folder", "" + folder.mkdirs());
        }

        if (savedInstanceState == null || !savedInstanceState.containsKey("last language")
                || !savedInstanceState.getString("last language").equals(Locale.getDefault().getLanguage())
                || !savedInstanceState.containsKey("movies")) {

            displayLoading();
            if(isOnline()) {
                final Bundle loaderArgs = getIntent().getExtras();
                getSupportLoaderManager().initLoader(0, loaderArgs, this).forceLoad();
            } else {
                displayError(ResultType.NO_INTERNET);
            }
        } else{
            List<Movie> movies = savedInstanceState.getParcelableArrayList("movies");
            assert movies != null;
            adapter.setMovies(movies);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("last language", Locale.getDefault().getLanguage());
        if (adapter.getItemCount() > 0)
            outState.putParcelableArrayList("movies", (ArrayList<? extends Parcelable>) adapter.getMovies());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //language changed
        if (!lastLang.equals(Locale.getDefault().getLanguage())) {
            final Bundle loaderArgs = getIntent().getExtras();
            lastLang = Locale.getDefault().getLanguage();
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

    private void displayLoading(){
        progressView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}