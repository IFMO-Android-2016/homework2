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
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView errorTextView;
    private int alreadyViewed;
    private int downloaded;
    private MovieRecyclerAdapter adapter;

    public void downloadMore() {
        getSupportLoaderManager().initLoader(downloaded, getIntent().getExtras(), this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        errorTextView = (TextView) findViewById(R.id.error);

        SimpleDraweeView image = (SimpleDraweeView) findViewById(R.id.image);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(getResources().getColor(R.color.gray_a)));

        progressBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        //download while scrolling
        alreadyViewed = 0;
        downloaded = 1;
        if (savedInstanceState != null) {
            downloaded = savedInstanceState.getInt("DOWNLOADED");
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int visible = 2, previous = 15;
            boolean isLoading = true;

            @Override
            public void onScrolled(RecyclerView recyclerView, int x, int y) {
                super.onScrolled(recyclerView, x, y);
                int itemCountAll = linearLayoutManager.getItemCount();
                int itemCountViewed = recyclerView.getChildCount();
                int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
                if (isLoading && (previous < itemCountAll)) {
                    previous = itemCountAll;
                    isLoading = false;
                }
                if (!isLoading && (firstItem + visible + 1) > itemCountAll - itemCountViewed) {
                    downloaded++;
                    isLoading = true;
                    downloadMore();
                }
            }
        });
        downloadMore();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("DOWNLOADED", downloaded);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(this, downloaded);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        if (result.resultType == ResultType.OK) {
            if (result.data != null && !result.data.isEmpty()) {
                displayNonEmptyData(result.data);
            } else {
                displayEmptyData();
            }
        } else {
            displayError(result.resultType);
        }
    }

    private void displayError(ResultType resultType) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        int message;
        if (resultType == ResultType.NO_INTERNET) {
            message = R.string.connection;
        } else {
            message = R.string.error;
        }
        errorTextView.setText(message);
    }

    private void displayEmptyData() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.not_found);
    }

    private void displayNonEmptyData(List<Movie> data) {
        if (adapter == null) {
            adapter = new MovieRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.setMovies(data);
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        displayEmptyData();
    }
}
