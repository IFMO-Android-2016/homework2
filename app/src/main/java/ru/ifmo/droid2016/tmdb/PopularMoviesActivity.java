package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MovieLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>>, View.OnClickListener {

    public static final String LANG_TAG = "language";
    public static final String LAST_PAGE_TAG = "lastPage";
    public static final String LOG_TAG = "MainActivity";
    public static final String MOVIES_ARRAY_TAG = "moviesData";
    public static final String SCROLL_TAG = "scroll";
    public static final String PAGE_LOADED_TAG = "pageLoaded";

    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private CardView infoCard;
    private TextView tvErrorMessage;
    private Button btnRepeat;

    private boolean langChange = false;

    private ArrayList<Movie> movies = new ArrayList<>();

    private MovieRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int lastPage = 0;
    private int pageLoaded = 0;

    private String language;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Main Activity", "onCreate " + String.valueOf(mLayoutManager));
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        Fresco.initialize(this);

        setContentView(R.layout.activity_popular_movies);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        infoCard = (CardView) findViewById(R.id.infoCard);
        tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);
        btnRepeat = (Button) findViewById(R.id.btnRepeat);

        btnRepeat.setText(R.string.repeat);
        btnRepeat.setOnClickListener(this);


        mAdapter = new MovieRecyclerAdapter(this, movies);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLayoutManager.findLastVisibleItemPosition() >= mAdapter.getItemCount() - 3) {
                    Log.d("OnScroll", "last page = " + lastPage);
                    Bundle bundle = new Bundle();
                    bundle.putString(LANG_TAG, language);
                    if (lastPage == pageLoaded) {
                        lastPage++;
                    }
                    bundle.putInt(LAST_PAGE_TAG, pageLoaded + 1);
                    getSupportLoaderManager().initLoader(pageLoaded + 1, bundle, PopularMoviesActivity.this);
                }
            }
        });

        infoCard.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        btnRepeat.setVisibility(View.GONE);
        tvErrorMessage.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);

        if (savedInstanceState == null) {
            language = Locale.getDefault().getLanguage();
            Bundle bundle = new Bundle();
            bundle.putString(LANG_TAG, language);
            lastPage = 1;
            getSupportLoaderManager().initLoader(lastPage, bundle, this);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getInt(PAGE_LOADED_TAG, 0) == 0) {
            infoCard.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            btnRepeat.setVisibility(View.GONE);
            tvErrorMessage.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);

            language = Locale.getDefault().getLanguage();
            Bundle bundle = new Bundle();
            bundle.putString(LANG_TAG, language);
            lastPage = 1;
            getSupportLoaderManager().initLoader(lastPage, bundle, this);
            Log.d(LOG_TAG, "returned");
            return;
        }
        language = Locale.getDefault().getLanguage();
        Bundle bundle = new Bundle();
        bundle.putString(LANG_TAG, language);
        ArrayList<Movie> savedMovies = savedInstanceState.getParcelableArrayList(MOVIES_ARRAY_TAG);

        String oldLang = savedInstanceState.getString(LANG_TAG, " ");
        lastPage = savedInstanceState.getInt(LAST_PAGE_TAG, 1);
        if (language.equals(oldLang)) {
            movies.addAll(savedMovies);
            mAdapter.notifyDataSetChanged();
            infoCard.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(SCROLL_TAG));
        } else {
            langChange = true;
            for (int i = 1; i <= lastPage; ++i) {
                bundle.putInt(LAST_PAGE_TAG, i);
                getSupportLoaderManager().restartLoader(lastPage, bundle, this);
            }
            langChange = false;

            infoCard.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(SCROLL_TAG));
        }
    }


    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        Log.d(LOG_TAG, "onLoadFinished");
        //Если часть данных отображена и мы хотим отобразить следующую сраницу
        if (mRecyclerView.getVisibility() == View.VISIBLE) {
            if (data.resultType == ResultType.OK) {
                movies.addAll(data.data);
                mAdapter.notifyDataSetChanged();
                pageLoaded++;
                infoCard.setVisibility(View.GONE);
            } else {
                infoCard.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                btnRepeat.setVisibility(View.VISIBLE);
                tvErrorMessage.setVisibility(View.VISIBLE);
                if (data.resultType == ResultType.NO_INTERNET) {
                    tvErrorMessage.setText(R.string.noInternetMessage);
                } else {
                    tvErrorMessage.setText(R.string.errorMessage);
                }
            }
        } else {
            if (data.resultType == ResultType.OK) {
                movies.addAll(data.data);
                pageLoaded++;
                if (!langChange) {
                    mAdapter.notifyDataSetChanged();
                    infoCard.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                infoCard.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                btnRepeat.setVisibility(View.VISIBLE);
                tvErrorMessage.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                if (data.resultType == ResultType.NO_INTERNET) {
                    tvErrorMessage.setText(getText(R.string.noInternetMessage));
                } else {
                    tvErrorMessage.setText(getText(R.string.errorMessage));
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {

    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(LANG_TAG, language);
        bundle.putInt(LAST_PAGE_TAG, pageLoaded + 1);
        progressBar.setVisibility(View.VISIBLE);
        getSupportLoaderManager().restartLoader(pageLoaded + 1, bundle, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE_LOADED_TAG, pageLoaded);
        outState.putString(LANG_TAG, language);
        outState.putParcelableArrayList(MOVIES_ARRAY_TAG, movies);
        outState.putParcelable(SCROLL_TAG, mLayoutManager.onSaveInstanceState());
    }

}
