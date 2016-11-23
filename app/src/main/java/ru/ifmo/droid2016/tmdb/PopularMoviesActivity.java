package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;

public class PopularMoviesActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private final String DOWNLOADED_PAGES = "DOWNLOADED_PAGES";
    private int downloadedPages;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private MovieRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        if (savedInstanceState != null) {
            downloadedPages = savedInstanceState.getInt(DOWNLOADED_PAGES);
        } else {
            downloadedPages = 1;
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        errorTextView = (TextView) findViewById(R.id.error_text);
        adapter = new MovieRecyclerAdapter(this);

        final LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean loading = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItem = manager.findFirstVisibleItemPosition();
                int totalItemCount = manager.getItemCount();

                if (!loading && totalItemCount - visibleItem < 2) {
                    downloadMovies();
                    loading = true;
                }
                if (totalItemCount - visibleItem > 2) {
                    loading = false;
                }

            }
        });
        downloadMovies();

    }

    private void downloadMovies() {
        final Bundle loaderArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(downloadedPages++, loaderArgs, this);
    }


    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(this, downloadedPages);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        if (data.resultType == ResultType.OK) {
            if (data.data != null && !data.data.isEmpty()) {
                adapter.addMovies(data.data);
                progressBar.setVisibility(View.GONE);
                errorTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                onLoaderReset(loader);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            errorTextView.setVisibility(View.VISIBLE);
            if (data.resultType == ResultType.NO_INTERNET) {
                errorTextView.setText(R.string.no_internet_connection);
            } else {
                errorTextView.setText(R.string.unknown_error);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DOWNLOADED_PAGES, downloadedPages);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.movies_not_found);
    }
}
