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

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.PopularMoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private static final String TAG = "PopularMovies";
    private static final String DOWNLOADED_PAGES = "downloaded_pages";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;

    private MovieScroll adapter;
    private int downloadedPages;

    private PopularMoviesActivity a = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(getResources().getColor(R.color.colorGray)));

        progressBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        downloadedPages = 1;
        if (savedInstanceState != null) {
            downloadedPages = savedInstanceState.getInt(DOWNLOADED_PAGES);
        }
        getSupportLoaderManager().initLoader(downloadedPages, getIntent().getExtras(), this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int visibleThreshold = 2;
            boolean loading;
            int previousTotal = 20;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (loading && totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
                if (!loading && totalItemCount <= (firstVisibleItem + visibleThreshold + visibleItemCount)) {
                    loading = true;
                    downloadedPages++;
                    getSupportLoaderManager().initLoader(downloadedPages, getIntent().getExtras(), a);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DOWNLOADED_PAGES, downloadedPages);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new PopularMoviesLoader(this, downloadedPages);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        if (data.resultType == ResultType.OK) {
            if (adapter == null) {
                adapter = new MovieScroll(this);
                recyclerView.setAdapter(adapter);
            }
            adapter.setMovies(data.data);
            errorTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            if (data.resultType == ResultType.NO_INTERNET)
                errorTextView.setText(R.string.no_internet_message);
             else
                errorTextView.setText(R.string.error_message);
            errorTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        errorTextView.setText(R.string.movie_miss);
    }

}