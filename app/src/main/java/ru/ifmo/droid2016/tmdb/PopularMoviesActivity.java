package ru.ifmo.droid2016.tmdb;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;
import ru.ifmo.droid2016.tmdb.utils.RequestType;

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
    private EndlessScrollListener scrollListener;

    private String lang;
    private int lastPage, currentPosition, lastRequestType = RequestType.LOAD_MORE;

    public static final String PAGES = "PAGES", REQUEST_TYPE = "REQUEST_TYPE",
            LANG = "LANG", LAST_PAGE = "PAST_PAGE", CURRENT_POSITION = "CURRENT_POSITION",
            LAST_REQUEST_TYPE = "LAST_REQUEST_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.colorPrimaryDark)));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        errorImage = (ImageView) findViewById(R.id.error_image);
        recycler.setVisibility(GONE);
        progressBar.setVisibility(View.VISIBLE);
        if (savedInstanceState != null) {
            lang = savedInstanceState.getString(LANG);
            lastPage = savedInstanceState.getInt(LAST_PAGE);
            currentPosition = savedInstanceState.getInt(CURRENT_POSITION);
            lastRequestType = savedInstanceState.getInt(LAST_REQUEST_TYPE);
        }
        String currentLanguage = getResources().getConfiguration().locale.getLanguage();
        if (lang == null) {
            lang = currentLanguage;
        }
        Bundle args = new Bundle();
        args.putIntArray(PAGES, new int[]{0});
        args.putInt(REQUEST_TYPE, RequestType.LOAD_MORE);
        if (lastRequestType == RequestType.LOAD_MORE) {
            getSupportLoaderManager().initLoader(0, args, this);
        } else {
            getSupportLoaderManager().restartLoader(0, args, this);
        }
        if (!lang.equals(currentLanguage)) {
            lang = currentLanguage;
            int[] pagesToLoad = new int[lastPage + 1];
            for (int i = 0; i < lastPage + 1; i++) {
                pagesToLoad[i] = i;
            }
            args = new Bundle();
            args.putIntArray(PAGES, pagesToLoad);
            args.putInt(REQUEST_TYPE, RequestType.RELOAD);
            getSupportLoaderManager().restartLoader(0, args, this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        savedState.putString(LANG, lang);
        savedState.putInt(LAST_REQUEST_TYPE, lastRequestType);
        if (scrollListener != null) {
            savedState.putInt(LAST_PAGE, scrollListener.lastPage);
        }
        if (adapter != null) {
            savedState.putInt(CURRENT_POSITION, adapter.getCurrentPosition());
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            lastPage = savedInstanceState.getInt(LAST_PAGE);
            currentPosition = savedInstanceState.getInt(CURRENT_POSITION);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recycler.clearOnScrollListeners();
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(this, args.getIntArray(PAGES), args.getInt(REQUEST_TYPE), lang);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        hideAll();
        if (data.resultType == ResultType.OK) {
            if (adapter == null) {
                adapter = new MovieRecyclerAdapter(this);
                recycler.setAdapter(adapter);
                scrollListener = new EndlessScrollListener(getSupportLoaderManager(),
                        adapter, lastPage, this, loader.getId());
                recycler.addOnScrollListener(scrollListener);
            }
            scrollListener.setLastFinishedLoader(loader.getId());
            if (data.requestType == RequestType.LOAD_MORE) {
                adapter.addMovies(data.data);
            } else {
                adapter.setMovies(data.data);
                recycler.scrollToPosition(currentPosition);
                scrollListener.setLastFinishedLoader(data.pagesLoaded - 1);
            }
            recycler.setVisibility(View.VISIBLE);
            lastRequestType = RequestType.LOAD_MORE;
        } else {
            showError(data.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) { }

    private void showError(ResultType result) {
        hideAll();
        if (adapter == null || adapter.getItemCount() == 0) {
            if (result == ResultType.NO_INTERNET) {
                errorImage.setImageBitmap(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.offline_image));
            } else {
                errorImage.setImageBitmap(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.error_image));
            }
            errorImage.setVisibility(View.VISIBLE);
        } else {
            recycler.setVisibility(View.VISIBLE);
        }
    }

    private void hideAll() {
        recycler.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        errorImage.setVisibility(GONE);
    }
}
