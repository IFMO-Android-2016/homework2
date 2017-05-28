package ru.ifmo.droid2016.tmdb;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MovieLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {
    static final String STATE_PAGES = "pages";
    static final String STATE_PREV= "previous";
    static final String STATE_LANG= "language";
    //final LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> callbacks = this;
    private ProgressBar progressShow;
    private RecyclerView container;
    private TextView errorText;
    private MovieRecyclerAdapter adapter;
    private int pagesCount = 1;
    private RecyclerView.OnScrollListener listener;
    private String langRefer="";

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(STATE_PREV,previousTotal);
        savedInstanceState.putInt(STATE_PAGES, pagesCount);
        savedInstanceState.putString(STATE_LANG,langRefer);
        if (container.getVisibility()==View.VISIBLE)
            savedInstanceState.putInt("prShow",0);
        else
            savedInstanceState.putInt("prShow",1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            pagesCount = savedInstanceState.getInt(STATE_PAGES, 1);
            langRefer=savedInstanceState.getString(STATE_LANG, "");
        }
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        setContentView(R.layout.activity_popular_movies);

        progressShow = (ProgressBar) findViewById(R.id.progress);
        //progressShow.getProgressDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
        errorText = (TextView) findViewById(R.id.error_text);

        container = (RecyclerView) findViewById(R.id.recycler);
        container.setLayoutManager(layoutManager);
        container.addItemDecoration(
                new RecylcerDividersDecorator(R.color.colorPrimary));


        progressShow.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);

        //Log.e ("1"+langRefer,"2-"+Locale.getDefault().getLanguage());
        if (!langRefer.equals(Locale.getDefault().getLanguage()))
            langRefer = Locale.getDefault().getLanguage();
            getPage();
        listener = new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView rView, int dx, int dy) {
                super.onScrolled(rView, dx, dy);
                listenerScrollingChanges(layoutManager.findFirstVisibleItemPosition(), layoutManager.getItemCount());
            }
        };

        container.addOnScrollListener(listener);

    }

    ////////////
    private int previousTotal = 20;
    private boolean loading = false;

    private void listenerScrollingChanges(int firstPageItem, int totalCount) {
        final int visibleTreshold = 10;
        //Log.e(""+ firstPageItem,""+totalCount);

        if (loading && (totalCount > previousTotal)) {
            //Log.e("end","load");
            loading = false;
            previousTotal = totalCount;
        }
        if (!loading && totalCount <= (firstPageItem + visibleTreshold)) {
            loading = true;
            pagesCount++;
            getPage();
        }
    }
    private void getPage(){
        final Bundle loaderArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(pagesCount, loaderArgs, this);
    }
    ////////////
    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this, pagesCount);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        if (result.resultType == ResultType.OK) {
            if (result.data != null && !result.data.isEmpty())
                displayNonEmptyData(result.data);
            else
                displayEmptyData();
        } else
            displayError(result.resultType);
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        displayEmptyData();
    }

    private void displayEmptyData() {
        errorText.setText(R.string.movies_not_found);

        progressShow.setVisibility(View.GONE);
        container.setVisibility(View.INVISIBLE);
        errorText.setVisibility(View.VISIBLE);
    }

    private void displayNonEmptyData(List<Movie> movies) {
        if (adapter == null) {
            adapter = new MovieRecyclerAdapter(this);
            container.setAdapter(adapter);
        }
        adapter.setMovies(movies);

        progressShow.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
    }

    private void displayError(ResultType resultType) {
        if (resultType == ResultType.NO_INTERNET)
            errorText.setText(R.string.no_internet);
        else
            errorText.setText(R.string.no_no_internet);

        progressShow.setVisibility(View.GONE);
        container.setVisibility(View.INVISIBLE);
        errorText.setVisibility(View.VISIBLE);
    }

}
