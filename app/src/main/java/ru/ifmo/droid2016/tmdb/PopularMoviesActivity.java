package ru.ifmo.droid2016.tmdb;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.PopularMoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.MyRecyclerAdapter;
import ru.ifmo.droid2016.tmdb.utils.MyRecyclerAdapterLand;
import ru.ifmo.droid2016.tmdb.utils.MyRecyclerAdapterPortrait;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private static final String KEY_NEXT_PAGE = "NEXT_PAGE";

    int nextPage;

    private final String LOG_TAG = "my_tag";

    private final String LANG = "LANG";
    private Bundle mBundle;
    private Loader<LoadResult<List<Movie>>> mLoader;

    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_NEXT_PAGE, nextPage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        Display display = getWindowManager().getDefaultDisplay();
        Point dimens = new Point();
        display.getSize(dimens);

        Log.d(LOG_TAG, "diments : " + dimens.x + " " + dimens.y);

        //dimens.x = (int)(dimens.x / (float) getApplicationContext().getResources().getDisplayMetrics().density);
        //dimens.y = (int)(dimens.y / (float) getApplicationContext().getResources().getDisplayMetrics().density);

        TmdbDemoApplication.displayHeight = dimens.y;
        TmdbDemoApplication.displayWidth = dimens.x;


        Log.d(LOG_TAG, "diments : " + TmdbDemoApplication.displayHeight
                + " " + TmdbDemoApplication.displayWidth);


        mBundle = new Bundle();
        mBundle.putString(LANG, Locale.getDefault().getLanguage());
        mBundle.putInt("PAGE_ID", 1);

        if (getSupportLoaderManager().getLoader(0) == null) {
            Log.d(LOG_TAG, "don't exists!!!");

            mLoader = getSupportLoaderManager().initLoader(0, mBundle, this);

            mLoader.onContentChanged();
        } else {
            Log.d(LOG_TAG, "exists???");

            mLoader = getSupportLoaderManager().initLoader(0, mBundle, this);

            //mLoader = getSupportLoaderManager().getLoader(0);
        }

        Vector<String> myDataset = recyclerInit(100);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new RecylcerDividersDecorator(Color.RED));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mAdapter = new MyRecyclerAdapterPortrait();
            mRecyclerView.setAdapter((MyRecyclerAdapterPortrait)mAdapter);
        } else {
            mAdapter = new MyRecyclerAdapterLand();
            mRecyclerView.setAdapter((MyRecyclerAdapterLand)mAdapter);
        }
/*
        mRecyclerView.setOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        Log.d(LOG_TAG, dx + " " + dy);
                    }
                }
        );
*/
    }




    private Vector<String> recyclerInit(int num) {
        Vector<String> res = new Vector<>();

        for (int w = 0; w < num; w++) {
            res.addElement("item " + w);
        }
        return res;
    }



    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        Loader<LoadResult<List<Movie>>> mLoader = null;

        Log.d(LOG_TAG, "starting ###");

        mLoader = new PopularMoviesLoader(this, args);

        return mLoader;
    }


    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
    }

    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> res) {
        Log.d(LOG_TAG, "res : " + res.resultType);

        if (res.resultType == ResultType.OK) {
            for (Movie w : res.data) {
                mAdapter.addItemToEnd(w);
            }
        }

    }



    //int next = 1000;

    /*
    public void onClickBottom(View view) {



        mAdapter.addItem("item " + next);
        mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
        next++;

    }
    */
}
