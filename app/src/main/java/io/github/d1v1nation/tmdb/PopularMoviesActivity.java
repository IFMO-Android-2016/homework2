package io.github.d1v1nation.tmdb;

import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;;

import io.github.d1v1nation.tmdb.loader.LoadResult;
import io.github.d1v1nation.tmdb.loader.MovieLoader;
import io.github.d1v1nation.tmdb.loader.ResultType;
import io.github.d1v1nation.tmdb.model.Movie;
import io.github.d1v1nation.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;

    private MovieRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error_text);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.gray_a)));

        progressBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        // Pass all extra params directly to loader
        final Bundle loaderArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, loaderArgs, this);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this);
    }


    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        if (data.resultType == ResultType.OK) {
            if (data.data != null && !data.data.isEmpty()) {
                routineNonEmpty(data.data);
            } else {
                routineEmpty();
            }
        } else {
            routineError(data.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        routineEmpty();
    }

    private void routineEmpty() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        Toast.makeText(PopularMoviesActivity.this, "Podliva: none loaded", Toast.LENGTH_SHORT).show();
    }

    private void routineNonEmpty(List<Movie> data) {
        if (adapter == null) {
            adapter = new MovieRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }

        adapter.setMovies(data);
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void routineError(ResultType r) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        final int messageResId;
        if (r == ResultType.NO_INTERNET) {
            messageResId = R.string.nointernet;
        } else {
            messageResId = R.string.error;
        }
        errorTextView.setText(messageResId);
    }
}
