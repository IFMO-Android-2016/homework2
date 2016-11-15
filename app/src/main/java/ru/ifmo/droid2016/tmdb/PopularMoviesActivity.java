package ru.ifmo.droid2016.tmdb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

import static android.view.View.GONE;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private RecyclerView recycler;
    private ProgressBar progressBar;
    private ImageView errorImage;

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
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        errorImage = (ImageView) findViewById(R.id.error_image);
        recycler.setVisibility(GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        hideAll();
        if (data.resultType == ResultType.OK) {
            if (adapter == null) {
                adapter = new MovieRecyclerAdapter(this);
                recycler.setAdapter(adapter);
            }
            adapter.setMovies(data.data);
            recycler.setVisibility(View.VISIBLE);
        } else {
            showError(data.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) { }

    private void showError(ResultType result) {
        hideAll();
        if (result == ResultType.NO_INTERNET) {
            errorImage.setImageBitmap(
                    BitmapFactory.decodeResource(getResources(), R.mipmap.offline_image));
        } else {
            errorImage.setImageBitmap(
                    BitmapFactory.decodeResource(getResources(), R.mipmap.error_image));
        }
        errorImage.setVisibility(View.VISIBLE);
    }

    private void hideAll() {
        recycler.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        errorImage.setVisibility(GONE);
    }
}
