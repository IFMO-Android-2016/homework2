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
import android.widget.Toast;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;


/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
                                    implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    private ViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(R.color.colorPrimary));

        adapter = new ViewAdapter(this);
        recyclerView.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, bundle, this);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        if (result.resultType == ResultType.OK) {
            if (result.data != null && !result.data.isEmpty()) {
                if (adapter == null) {
                    adapter = new ViewAdapter(this);
                    recyclerView.setAdapter(adapter);
                }
                adapter.updateMoviesList(result.data);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

                Toast toast = Toast.makeText(this, "Не удалось загрузить ни одного фильма.", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(this, "Ошибка" +
                    (result.resultType == ResultType.NO_INTERNET ? ": Нет подключения." : "!"), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        Toast toast = Toast.makeText(this, "Не удалось загрузить ни одного фильма.", Toast.LENGTH_LONG);
        toast.show();
    }
}
