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

public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private static final String DOWNLOADED_PAGES = "downloaded_pages";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;

    private MovieRecyclerAdapter adapter;
    private int downloadedPages;
    private int shownPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        SimpleDraweeView image = (SimpleDraweeView) findViewById(R.id.movie_image);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        errorTextView = (TextView) findViewById(R.id.error_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(getResources().getColor(R.color.colorAccent)));

        progressBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        downloadedPages = 1;
        shownPages = 0;
        if (savedInstanceState != null) {
            downloadedPages = savedInstanceState.getInt(DOWNLOADED_PAGES);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean loading;
            int previousTotal = 20;
            private int visibleThreshold = 2;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    loading = true;
                    downloadedPages++;
                    downloadPages();
                }
            }
        });
        downloadPages();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DOWNLOADED_PAGES, downloadedPages);
    }

    private void downloadPages() {
        final Bundle loaderArgs = getIntent().getExtras();
            getSupportLoaderManager().initLoader(downloadedPages, loaderArgs, this);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new PopularMoviesLoader(this, downloadedPages);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        if (data.resultType == ResultType.OK) {
            if (data.data != null && data.data.isEmpty()) {
                displayEmptyData();
            } else {
                displayData(data.data);
            }
        } else {
            displayError(data.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        displayEmptyData();
    }

    private void displayEmptyData() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.movies_not_found);
    }

    private void displayData(List<Movie> movies) {
        if (adapter == null) {
            adapter = new MovieRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.setMovies(movies);
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayError(ResultType resultType) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        int message;
        if (resultType == ResultType.NO_INTERNET) {
            message = R.string.no_internet_message;
        } else {
            message = R.string.error_message;
        }
        errorTextView.setText(message);
    }

    private static final String TAG = "PopularMovies";
}
