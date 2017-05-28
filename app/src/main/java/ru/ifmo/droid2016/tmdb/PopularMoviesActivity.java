package ru.ifmo.droid2016.tmdb;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.adapter.MoviesRecyclerAdapter;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

import static ru.ifmo.droid2016.tmdb.utils.MoviesSerialization.deserialize;
import static ru.ifmo.droid2016.tmdb.utils.MoviesSerialization.serialize;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {
    private RecyclerView recyclerView;
    private ProgressBar progressView;

    private int lastPage, scrollPosition;
    private boolean langChanged, showDialog = true;
    private AlertDialog dialog;
    private final int ANS_SIZE = 20;


    @Nullable
    private MoviesRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.d("OnCr", "On create launched");
        progressView = (ProgressBar) findViewById(R.id.progress);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (savedInstanceState != null) {
            try {
                restoreData(savedInstanceState);
            } catch (IOException | ClassNotFoundException e) {
                Log.d("Activity", e.getCause() + " " + e.getMessage());
                e.printStackTrace();
            }
        }

        recyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {

            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                scrollPosition = holder.getAdapterPosition();

                if ((adapter.getItemCount() - holder.getAdapterPosition()) <= ANS_SIZE / 2
                        && getSupportLoaderManager().getLoader(lastPage) == null) {
                    Log.d("OnCreate", "Downloading next page " + (lastPage + 1));
                    getSupportLoaderManager().initLoader(++lastPage, null, PopularMoviesActivity.this);
                }
            }
        });


        if (savedInstanceState == null)
            getSupportLoaderManager().initLoader(1, null, PopularMoviesActivity.this);

    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        if (id == 1) progressView.setVisibility(View.VISIBLE);
        return new MoviesLoader(this, id);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        getSupportLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    @UiThread
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        Log.d("MovAct", "Download finished");
        if (result.resultType == ResultType.OK) {
            if (result.data != null && !result.data.isEmpty()) {
                if (loader.getId() == 1) {
                    adapter = new MoviesRecyclerAdapter(this, result.data);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    adapter.onUpdate(result.data);
                }

                if (loader.getId() < lastPage) {
                    getSupportLoaderManager().initLoader(loader.getId() + 1, null, this);
                }

                if (loader.getId() == lastPage && langChanged) {
                    recyclerView.scrollToPosition(scrollPosition);
                }
            } else {
                showError("No movies available.", this, loader.getId(), null);
            }
        } else {
            if (result.resultType == ResultType.NO_INTERNET) {
                showError("No internet connection.", this, loader.getId(), null);
            }
            if (result.resultType == ResultType.ERROR) {
                showError("An error occurred.Please, try again later.", this, loader.getId(), null);
            }
        }

        if (!langChanged) progressView.setVisibility(View.GONE);
        onLoaderReset(loader);
    }


    private void showError(String message, final LoaderManager.LoaderCallbacks callbacks, final int id, final Bundle bundle) {
        if (!showDialog || (dialog != null && dialog.isShowing())) return;

        if (bundle == null) lastPage--;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Error")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDialog = false;
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSupportLoaderManager().initLoader(id, bundle, callbacks);
                        dialog.dismiss();
                    }
                });

        dialog = builder.create();
        dialog.show();
    }



    @SuppressWarnings("unchecked")
    private void restoreData(Bundle savedInstance) throws IOException, ClassNotFoundException{
        lastPage = savedInstance.getInt("page");
        scrollPosition = savedInstance.getInt("scroll_p");
        String lastLanguage = savedInstance.getString("locale");
        if (lastLanguage != null && !lastLanguage.equals(Locale.getDefault().getLanguage())) {
            Log.d("RESTDATA", "Language changed. Uploading pages");
            langChanged = true;
            getSupportLoaderManager().initLoader(1, null, this);
            return;
        }


        List<Movie> movies = (List<Movie>) deserialize(savedInstance.getByteArray("movies"));
        adapter = new MoviesRecyclerAdapter(this, movies);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        Log.d("ACTIVITY", "On save instance");
        super.onSaveInstanceState(state);
        if (adapter == null || adapter.getMovies() == null) return;

        try {
            List<Movie> temp = adapter.getMovies();
            state.putByteArray("movies", serialize(temp));
        } catch (IOException e) {
            Log.d("SER", "IOException while serializing movies list");
            e.printStackTrace();
        }

        state.putInt("page", lastPage);
        state.putString("locale", Locale.getDefault().getLanguage());
        state.putInt("scroll_p", scrollPosition);
    }


}
