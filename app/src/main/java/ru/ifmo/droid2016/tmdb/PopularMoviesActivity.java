package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    RecyclerView recycler;

    private MovieRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        getSupportLoaderManager().initLoader(0, null, this);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.colorPrimaryDark)));
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        if (adapter == null) {
            adapter = new MovieRecyclerAdapter(this);
            recycler.setAdapter(adapter);

        }
        adapter.setMovies(data.data);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {

    }
}
