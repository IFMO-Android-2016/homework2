package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import ru.ifmo.droid2016.tmdb.model.LoadingMovie;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.EndlessRecyclerViewScrollListener;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private static final String TAG = "ACTIVITY";

    private static final String MOVIES_TAG = "MOVIES";
    private static final String FIRSTLOAD_TAG = "FIRSTLOAD";
    private static final String LASTPAGE_TAG = "LASTPAGE";
    private static final String SCROLL_TAG = "SCROLL";
    private static final String LANG_TAG = "LANG";

    private final Handler handler = new Handler();

    private LinearLayout errorLayout;
    private Button tryAgainView;
    private RecyclerView recyclerView;
    private ProgressBar progressView;
    private TextView errorView;

    private ArrayList<Movie> movies;
    private boolean isFirstLoad;
    private int lastPage;
    private String lang;

    private MoviesRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        progressView = (ProgressBar) findViewById(R.id.progress);
        errorView = (TextView) findViewById(R.id.error);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        tryAgainView = (Button) findViewById(R.id.try_again);

        progressView.setIndeterminate(true);

        progressView.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(ContextCompat.getColor(this, R.color.colorDivider)));

        if (savedInstanceState != null) {
            loadExistingActivity(savedInstanceState);
        } else {
            Log.i(TAG, "SavedInstanceState is null");
            loadBlankActivity();
        }

        adapter = new MoviesRecyclerAdapter(this, movies);
        recyclerView.setAdapter(adapter);
    }

    private void loadBlankActivity() {
        Log.e(TAG, "###### Loading blank activity...");
        movies = new ArrayList<>();
        isFirstLoad = true;
        lastPage = 0;
        lang = Locale.getDefault().getLanguage();
        loadNextPage(false);
    }

    private void loadExistingActivity(@NonNull Bundle savedInstanceState) {
        ArrayList<Movie> savedMovies = savedInstanceState.getParcelableArrayList(MOVIES_TAG);
        String savedLang = savedInstanceState.getString(LANG_TAG, Locale.getDefault().getLanguage());
        int savedLastPage = savedInstanceState.getInt(LASTPAGE_TAG, 0);
        boolean savedFirstLoad = savedInstanceState.getBoolean(FIRSTLOAD_TAG, true);
        if (savedMovies != null
                && savedMovies.size() > 0
                && !savedFirstLoad && savedLastPage > 0
                && Locale.getDefault().getLanguage().equals(savedLang)) {
            Log.e(TAG, "###### Loading activity from saved instance...");
            movies = savedMovies;
            isFirstLoad = false;
            lastPage = savedLastPage;
            lang = savedLang;
            progressView.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(SCROLL_TAG));
            addListener();
        } else {
            if (!Locale.getDefault().getLanguage().equals(savedLang)) {
                Log.i(TAG, "New language: " + Locale.getDefault().getLanguage());
                Log.i(TAG, "Old language: " + savedLang);
                for (int i = savedLastPage; i != 0; i--) {
                    getSupportLoaderManager().destroyLoader(i);
                }
            }
            loadBlankActivity();
        }
    }

    private void addListener() {
        EndlessRecyclerViewScrollListener listener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                loadNextPage(false);
            }
        };
        //listener.setPageIndex(page);
        recyclerView.addOnScrollListener(listener);
    }

    public void loadNextPage(boolean force) {
        displayLoader();
        Log.i(TAG, "Next page");
        Bundle args = new Bundle();
        int page = lastPage + 1;
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
        Log.i(TAG, "New loader created");
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
            lastPage++;
            if (!isFirstLoad) {
                movies.remove(movies.size() - 1);
            }
            movies.addAll(result.data);
            handler.post(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
            if (isFirstLoad) {
                displayMovies();
                addListener();
                isFirstLoad = false;
            }
        } else {
            displayError(result.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        Log.e(TAG, "LOADER RESET");
        //displayError(ResultType.ERROR);
    }

    public void displayLoader() {
        if (isFirstLoad) {
            progressView.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            Movie last = movies.size() > 0 ? movies.get(movies.size() - 1) : null;
            if (last instanceof LoadingMovie) {
                Log.i(TAG, "Display existing loader");
                ((LoadingMovie) last).displayLoader();
                handler.post(new Runnable() {
                    public void run() {
                        adapter.notifyItemChanged(movies.size() - 1);
                    }
                });
            } else {
                Log.i(TAG, "Add new loader");
                movies.add(new LoadingMovie());
                handler.post(new Runnable() {
                    public void run() {
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                });
            }

        }
    }

    public void displayMovies() {
        if (isFirstLoad) {
            progressView.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            // Do nothing...
        }
    }

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
        if (isFirstLoad) {
            errorView.setText(message);
            progressView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            tryAgainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadNextPage(true);
                }
            });
        } else {
            Movie last = movies.get(movies.size() - 1);
            if (last instanceof LoadingMovie) {
                ((LoadingMovie) last).displayError(message);
                handler.post(new Runnable() {
                    public void run() {
                        adapter.notifyItemChanged(movies.size() - 1);
                    }
                });
            }
        }
    }

    private boolean isFirstLoad() {
        return movies != null && movies.size() > 0;
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(TAG, "Resume activity");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "Restore activity state");
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "Save activity state");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_TAG, movies);
        outState.putBoolean(FIRSTLOAD_TAG, isFirstLoad);
        outState.putInt(LASTPAGE_TAG, lastPage);
        outState.putString(LANG_TAG, lang);
        outState.putParcelable(SCROLL_TAG, recyclerView.getLayoutManager().onSaveInstanceState());
    }
}