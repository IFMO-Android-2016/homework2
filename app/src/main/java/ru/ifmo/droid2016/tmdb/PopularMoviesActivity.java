package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
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
import ru.ifmo.droid2016.tmdb.loader.PopularMoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {


    private RecyclerView recyclerView;
    private ProgressBar progressView;
    private TextView errorTextView;

    private int downloadedPages, viewedPages, scrollPosition;

    private String downloaded = "DOWNLOADED";
    private String scrollPos = "SCROLL_POSITION";

    private MoviesRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        progressView = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error_text);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(getResources().getColor(R.color.gray_a)));
        progressView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        viewedPages = 0;
        downloadedPages = 1;
        if (savedInstanceState != null) {
            Log.d("Actitity restore", "restore");
            downloadedPages = savedInstanceState.getInt(downloaded);
            scrollPosition = savedInstanceState.getInt(scrollPos);
            //recyclerView.scrollToPosition(scrollPosition);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private static final String TAG = "Scroll listener";
            private int visibleTreshold = 5;
            private int loaded = 10;
            boolean state = true;

            @Override
            public void onScrolled(RecyclerView recyclerView, int x, int y) {
                super.onScrolled(recyclerView, x, y);
                scrollPosition = adapter.position;
                Log.d(TAG, "" + scrollPosition);
                int itemCount = layoutManager.getItemCount();
                int itemViewed = recyclerView.getChildCount();
                int firstItem = layoutManager.findFirstVisibleItemPosition();
                if (state && loaded < itemCount) {
                    loaded = itemCount;
                    state = false;
                }
                if (!state && (firstItem + visibleTreshold + 1) > itemCount - itemViewed) {
                    downloadedPages++;
                    state = true;
                    downloadMovies();
                }
            }
        });
        downloadMovies();
    }
    public void downloadMovies() {
        getSupportLoaderManager().initLoader(downloadedPages, getIntent().getExtras(), this);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new PopularMoviesLoader(this, id);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        if (data.resultType == ResultType.OK) {
            if (data.data != null && !data.data.isEmpty()) {
                displayNonEmptyData(data.data);
            } else {
                displayEmptyData();
            }
        } else {
            displayError(data.resultType);
        }

    }

    private void displayNonEmptyData(List<Movie> movies) {
        if (adapter == null) {
            adapter = new MoviesRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.addMovies(movies);
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);

    }


    private void displayError(ResultType resultType) {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        final int messageResId;
        if (resultType == resultType.NO_INTERNET) {
            messageResId = R.string.no_internet;
        } else {
            messageResId = R.string.error;
        }
        errorTextView.setText(messageResId);
    }

    private void displayEmptyData() {
        if (adapter == null) {
            adapter = new MoviesRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setText(R.string.movies_not_found);
        errorTextView.setText(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(downloaded, downloadedPages);
        bundle.putInt(scrollPos, scrollPosition);
    }
}
