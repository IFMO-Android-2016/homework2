package ru.ifmo.droid2016.tmdb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.PopularMoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.MyRecyclerAdapter;
import ru.ifmo.droid2016.tmdb.utils.MyRecyclerAdapterLand;
import ru.ifmo.droid2016.tmdb.utils.MyRecyclerAdapterPortrait;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import android.widget.Toast;
/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private static final String KEY_NEXT_PAGE = "NEXT_PAGE";
    private static final String KEY_PREVIOUS_LANG = "PREVIOUS_LANG";
    private static final String KEY_PAGE_IN_PROCESS = "PAGE_IN_PROCESS";

    int nextPage = 1;
    private String currentLang;
    int pageInProcess;

    Set<Integer> queue;

    private final String LOG_TAG = "my_tag";

    private final String LANG = "LANG";
    private Loader<LoadResult<List<Movie>>> mLoader;

    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private final String BROADCAST_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BROADCAST_ACTION)) {
                final ConnectivityManager connectivityManager =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final android.net.NetworkInfo wifi = connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                final android.net.NetworkInfo mobile = connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifi.isAvailable() || mobile.isAvailable()) {
                    Log.d(LOG_TAG, "Available");
                    mAdapter.updInternetState(true);

                    for (Integer w : mAdapter.pagesWithError) {
                        queue.add(w);
                    }
                    mAdapter.pagesWithError.clear();

                    startDownloading();
                }

            }
        }
    };


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_NEXT_PAGE, nextPage);
        outState.putString(KEY_PREVIOUS_LANG, currentLang);
        outState.putInt(KEY_PAGE_IN_PROCESS, pageInProcess);
        TmdbDemoApplication.savedMovies = mAdapter.getData();
    }

    void initDisplay() {
        Display display = getWindowManager().getDefaultDisplay();
        Point dimens = new Point();
        display.getSize(dimens);

        Log.d(LOG_TAG, "dimens : " + dimens.x + " " + dimens.y);

        TmdbDemoApplication.displayHeight = dimens.y;
        TmdbDemoApplication.displayWidth = dimens.x;


        Log.d(LOG_TAG, "dimens : " + TmdbDemoApplication.displayHeight
                + " " + TmdbDemoApplication.displayWidth);
    }

    void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new RecylcerDividersDecorator(Color.RED));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mAdapter = new MyRecyclerAdapterPortrait();
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter = new MyRecyclerAdapterLand();
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        queue = TmdbDemoApplication.queue;

        initDisplay();
        currentLang = Locale.getDefault().getLanguage();
        initRecyclerView();

        if (savedInstanceState == null) {
            nextPage = 1;
            pageInProcess = -1;
            addPageToQueue(1);
        } else {
            nextPage = savedInstanceState.getInt(KEY_NEXT_PAGE);
            mAdapter.init(TmdbDemoApplication.savedMovies, TmdbDemoApplication.savedPagesWithError);
            pageInProcess = savedInstanceState.getInt(KEY_PAGE_IN_PROCESS);

            String previousLang = savedInstanceState.getString(KEY_PREVIOUS_LANG);

            if (!previousLang.equals(currentLang)) {
                queue.clear();
                mLoader.reset();
                pageInProcess = -1;
                addPageToQueue(1);

                mAdapter.pagesWithError.clear();
            }
        }

        mRecyclerView.setOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == SCROLL_STATE_IDLE) {
                            Log.d(LOG_TAG, "stop scrolling");

                            Set<Integer> pagesNeededToUpd = mAdapter.getPagesNeededToUpd();
                            if (pagesNeededToUpd.size() > 0) {
                                addPageToQueue(pagesNeededToUpd.iterator().next());
                            }
                        }
                    }
                }
        );
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "starting ###");

        return new PopularMoviesLoader(this, args);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> res) {
        Log.d(LOG_TAG, "res : " + res.resultType);

        int numFilms = 0;

        if (res.resultType == ResultType.OK) {
            int page = -1;
            for (Movie w : res.data) {
                page = w.page;
                if (w.page == nextPage) {
                    mAdapter.addItemToEnd(w);
                } else {
                    mAdapter.updItem((page - 1) * 20 + numFilms, w);
                    numFilms++;
                }
            }
            Log.d(LOG_TAG, "page " + page);
            if (page == nextPage) {
                nextPage++;
            }

            Log.d(LOG_TAG, "nextPage " + nextPage);

            queue.remove(page);
            mAdapter.pagesWithError.remove(page);

            Set<Integer> pagesNeededToUpd = mAdapter.getPagesNeededToUpd();
            pagesNeededToUpd.remove(page);
            pageInProcess = -1;

            loader.reset();
            if (pagesNeededToUpd.size() > 0) {
                addPageToQueue(pagesNeededToUpd.iterator().next());
            }
            startDownloading();
        } else {
            loader.reset();

            mAdapter.addPageWithError(pageInProcess);
            queue.remove(pageInProcess);

            if (res.resultType == ResultType.NO_INTERNET) {
                mAdapter.updInternetState(false);

                if (pageInProcess != nextPage) {
                    for (int w = 0; w < 20; w++) {
                        mAdapter.updItem((pageInProcess - 1) * 20 + w,
                                new Movie("", "", "", "", nextPage, ""));
                    }
                }

                Toast.makeText(this, "No internet", Toast.LENGTH_LONG).show();
            }
            if (res.resultType == ResultType.ERROR) {
                if (pageInProcess == nextPage) {
                    for (int w = 0; w < 20; w++) {
                        mAdapter.addItemToEnd(new Movie("", "", "", "", nextPage, ""));
                    }
                    nextPage++;
                } else {
                    for (int w = 0; w < 20; w++) {
                        mAdapter.updItem((pageInProcess - 1) * 20 + w,
                                new Movie("", "", "", "", nextPage, ""));
                    }
                }

                Toast.makeText(this, "Error in downloading.", Toast.LENGTH_LONG).show();
            }
            pageInProcess = -1;

            Set<Integer> pagesNeededToUpd = mAdapter.getPagesNeededToUpd();

            if (pagesNeededToUpd.size() > 0) {
                addPageToQueue(pagesNeededToUpd.iterator().next());
            }
            startDownloading();
        }
    }

    public void downloadNextPage() {
        Log.d(LOG_TAG, "downloadNextPage !!!");
        addPageToQueue(nextPage);
    }

    void addPageToQueue(int page) {
        queue.add(page);
        startDownloading();
    }

    void startDownloading() {
        Log.d(LOG_TAG, "startDownloading " + queue.size() + " " + pageInProcess);
        if ((queue.size() > 0) && (pageInProcess == -1)) {
            Iterator<Integer> it = queue.iterator();

            Integer w = it.next();

            Bundle mBundle = new Bundle();
            mBundle.putString("LANG", currentLang);
            mBundle.putInt("PAGE_ID", w);

            Log.d(LOG_TAG, "startDownloading " + w);

            pageInProcess = w;

            mLoader = getSupportLoaderManager().restartLoader(0, mBundle, this);
            mLoader.onContentChanged();
        }
    }

}
