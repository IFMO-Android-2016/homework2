package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.EndlessRecyclerViewScrollListener;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private static final String MOVIES_TAG = "MOVIES";
    private static final String LANG_TAG = "LANG";
    private static final String READY_TAG = "READY";
    private static final String RESTORE_TAG = "RESTORE";
    private static final String PAGE_TAG = "PAGE";
    private static final String SCROLL_TAG = "SCROLL";

    private final Handler handler = new Handler();

    private LinearLayout errorLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressView;
    private TextView errorView;

    private ArrayList<Movie> movies = new ArrayList<>();
    private String lang = Locale.getDefault().getLanguage();
    private boolean ready = false;
    private int restore = 0;
    private int page = 0;
    private Parcelable scroll = null;

    private MoviesRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        progressView = (ProgressBar) findViewById(R.id.progress);
        errorView = (TextView) findViewById(R.id.error);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        Button tryAgainView = (Button) findViewById(R.id.try_again);
        tryAgainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextPage(true);
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(ContextCompat.getColor(this, R.color.colorDivider)));

        adapter = new MoviesRecyclerAdapter(this, movies);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                if (ready) {
                    loadNextPage(false);
                }
            }
        });

        if (savedInstanceState == null) {
            loadNextPage(false);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ArrayList<Movie> savedMovies = savedInstanceState.getParcelableArrayList(MOVIES_TAG);
        String savedLang = savedInstanceState.getString(LANG_TAG, Locale.getDefault().getLanguage());
        boolean savedReady = savedInstanceState.getBoolean(READY_TAG, false);
        int savedRestore = savedInstanceState.getInt(RESTORE_TAG, 0);
        int savedPage = savedInstanceState.getInt(PAGE_TAG, 0);
        Parcelable savedScroll = savedInstanceState.getParcelable(SCROLL_TAG);

        if (savedMovies == null) {
            savedMovies = new ArrayList<>();
        }

        if (savedMovies.size() > 0 || savedRestore > 0) {
            Log.i(TAG, "Restore content");
            movies.clear();
            movies.addAll(savedMovies);
            lang = savedLang;
            ready = savedReady;
            restore = savedRestore;
            page = savedPage;
            scroll = savedScroll;

            if (ready) {
                handler.post(new Runnable() {
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                displayMovies();
                recyclerView.getLayoutManager().onRestoreInstanceState(scroll);
                scroll = null;
            } else {
                Log.i(TAG, "Restored content is not ready");
                if (restore > 0) { // Rotated during restoration
                    loadNextPage(true);
                } else { // Rotated during first load
                    loadNextPage(false);
                }
            }
        } else {
            Log.i(TAG, "Start blank activity");
            loadNextPage(false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_TAG, movies);
        outState.putString(LANG_TAG, lang);
        outState.putBoolean(READY_TAG, ready);
        outState.putInt(RESTORE_TAG, restore);
        outState.putInt(PAGE_TAG, page);
        outState.putParcelable(SCROLL_TAG, scroll == null ? recyclerView.getLayoutManager().onSaveInstanceState() : scroll);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Locale.getDefault().getLanguage().equals(lang)) {
            Log.e(TAG, "Language changed: " + lang + " -> " + Locale.getDefault().getLanguage());

            for (int i = 1; i <= page; i++) {
                getSupportLoaderManager().destroyLoader(i);
            }

            scroll = recyclerView.getLayoutManager().onSaveInstanceState();
            movies.clear();
            lang = Locale.getDefault().getLanguage();
            ready = false;
            restore = page;
            page = 0;

            loadNextPage(true);
        }
    }

    // Show progress bar and init next page loading
    public void loadNextPage(boolean force) {
        displayLoader();
        Bundle args = new Bundle();
        int page = this.page + 1;
        args.putInt("page", page);
        args.putString("lang", lang);
        if (force) {
            getSupportLoaderManager().restartLoader(page, args, this);
        } else {
            getSupportLoaderManager().initLoader(page, args, this);
        }
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        if (args == null) {
            args = new Bundle();
        }
        return new MoviesLoader(this,
                args.getString("lang", Locale.getDefault().getLanguage()),
                args.getInt("page", 1));
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        if (result.resultType == ResultType.OK && result.data != null) {
            if (movies.size() > 0 && movies.get(movies.size() - 1).isLoader()) {
                movies.remove(movies.size() - 1);
            }
            movies.addAll(result.data);
            page++;
            if (restore > 0) {
                if (page < restore) {
                    loadNextPage(true);
                } else {
                    restore = 0;
                    handler.post(new Runnable() {
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    displayMovies();
                    ready = true;
                    recyclerView.getLayoutManager().onRestoreInstanceState(scroll);
                    scroll = null;
                }
            } else {
                handler.post(new Runnable() {
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                if (!ready) {
                    displayMovies();
                    ready = true;
                }
            }
        } else {
            displayError(result.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        // Do nothing
    }

    // Display a progress bar
    public void displayLoader() {
        if (!ready) {
            progressView.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            Movie last = movies.size() > 0 ? movies.get(movies.size() - 1) : null;
            if (last != null && last.isLoader()) {
                last.displayLoader();
                handler.post(new Runnable() {
                    public void run() {
                        adapter.notifyItemChanged(movies.size() - 1);
                    }
                });
            } else {
                last = new Movie();
                last.displayLoader();
                movies.add(last);
                handler.post(new Runnable() {
                    public void run() {
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                });
            }
        }
    }

    // Display movies list
    public void displayMovies() {
        if (movies.size() == 0) {
            displayError(ResultType.OK);
        } else {
            progressView.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    // Display an error message based on resultType
    public void displayError(ResultType resultType) {
        final int message;
        switch (resultType) {
            case NO_INTERNET:
                message = R.string.no_internet;
                break;
            case OK:
                message = R.string.movies_not_found;
                break;
            case ERROR:
            default:
                message = R.string.error;
        }
        if (!ready) {
            errorView.setText(message);
            progressView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        } else {
            Movie last = movies.size() > 0 ? movies.get(movies.size() - 1) : null;
            if (last != null && last.isLoader()) {
                last.displayError(message);
                handler.post(new Runnable() {
                    public void run() {
                        adapter.notifyItemChanged(movies.size() - 1);
                    }
                });
            } else {
                last = new Movie();
                last.displayError(message);
                movies.add(last);
                handler.post(new Runnable() {
                    public void run() {
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                });
            }
        }
    }

    // Logger tag
    private static final String TAG = "ACTIVITY";
}