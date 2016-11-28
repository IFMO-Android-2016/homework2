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

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MovieLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressView;
    private TextView progressTextView;
    private ArrayList<Movie> movies;
    @Nullable
    private MyAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        progressView = (ProgressBar) findViewById(R.id.progress);
        progressTextView = (TextView) findViewById(R.id.status);

// use this setting to improve performance if you know that changes
// in content do not change the layout size of the RecyclerView
       // mRecyclerView.setHasFixedSize(true);

// use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

// specify an adapter (see also next example)
        progressView.setVisibility(View.VISIBLE);
        progressTextView.setVisibility(View.VISIBLE);
        progressTextView.setText("Происходит загрузка данных");
        mRecyclerView.setVisibility(View.GONE);
        mAdapter = new MyAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        final Bundle args = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0,args,this);
    }
    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        Loader<String> loader = null;
        Log.d("My","onCreateLoader");
        return new MovieLoader(this);
    }
    @Override
        public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result){
        if(result.resultType == ResultType.OK){
            Log.d("My","done!!!");
            if(movies==null){
                movies = new ArrayList<>();
            }
            movies.addAll(result.data);
            Log.d("My","Da ept");
            displayData(result.data);
            Log.d("My","Da ept");
        }else {
            displayError(result.resultType);
        }
    }
    private void displayError(ResultType resultType) {
        progressView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        progressTextView.setVisibility(View.VISIBLE);
        final int messageResId;
        if (resultType == ResultType.NO_INTERNET) {
            messageResId = R.string.no_internet;
        } else {
            messageResId = R.string.error;
        }
        progressTextView.setText(messageResId);
    }
    private void displayData(List<Movie> Movies){
        if (Movies != null && !Movies.isEmpty()){
            if (mAdapter == null) {
                mAdapter = new MyAdapter(this);
                mRecyclerView.setAdapter(mAdapter);
            }
            Log.d("My",Integer.toString(Movies.size()));
            mAdapter.setMovies(Movies);
            progressView.setVisibility(View.GONE);
            progressTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

        } else{
            progressView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            progressTextView.setVisibility(View.VISIBLE);
            progressTextView.setText(R.string.no_movies);

        }
    }
    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        displayData(null);
        Log.d("kek", "onLoaderReset for loader " + loader.hashCode());
    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movies",movies);
        Log.d("My","onSave");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movies = savedInstanceState.getParcelableArrayList("movies");
    }

}
