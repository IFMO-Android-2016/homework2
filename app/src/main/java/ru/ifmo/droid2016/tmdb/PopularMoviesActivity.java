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
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>>{

    ProgressBar pb;
    RecyclerView rv;
    TextView errorText;
    private MovAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        pb = (ProgressBar) findViewById(R.id.progress_bar);
        rv = (RecyclerView) findViewById(R.id.movies_recycler);
        errorText = (TextView) findViewById(R.id.error_text);
        pb.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.INVISIBLE);
        rv.setVisibility(View.INVISIBLE);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new RecylcerDividersDecorator(R.color.colorPrimary));

        Bundle b = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, b, this);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        hideOrShow(false);
        pb.setVisibility(View.INVISIBLE);
        if (data.resultType == ResultType.OK) {
            if (adapter == null) {
                adapter = new MovAdapter(this);
                rv.setAdapter(adapter);
            }
            adapter.updateData(data.data);
            rv.setVisibility(View.VISIBLE);
        } else if (data.resultType == ResultType.ERROR || (data.data != null && data.data.size() == 0)) {
            hideOrShow(true);
            errorText.setText("Произошла какая-то ошибка");
        } else if (data.resultType == ResultType.NO_INTERNET) {
            hideOrShow(true);
            errorText.setText("Нет интернета");
        }
    }

    private void hideOrShow(boolean hideRV) {
        if (hideRV) {
            rv.setVisibility(View.INVISIBLE);
            errorText.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            errorText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {

    }
}
