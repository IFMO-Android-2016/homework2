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
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.PopularMoviesLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.MoviesRecycleAdapter;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<LoadResult<List<Movie>>> {

    public static final String LANGUAGE = "lng";
    public static final String PAGE = "page";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextview;


    private MoviesRecycleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String lang;
        String page = null;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);


        if (savedInstanceState != null) {
            page = savedInstanceState.getString(PAGE);
        }

        progressBar = (ProgressBar) findViewById(R.id.progress);
        errorTextview = (TextView) findViewById(R.id.error_text);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.gray_a)));



        progressBar.setVisibility(View.VISIBLE);
        errorTextview.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        lang = Locale.getDefault().getLanguage();

        Bundle myBundle = new Bundle();
        myBundle.putString(LANGUAGE, lang);
        if(page != null){
            myBundle.putString(PAGE, page);
        }
        getSupportLoaderManager().initLoader(0, myBundle, this);

    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        final String lang;
        final String page;
        if (args != null && args.containsKey(LANGUAGE) && args.containsKey(PAGE)) {
            lang = args.getString(LANGUAGE);
            page = args.getString(PAGE);
        } else {
            lang = "en";
            page = "1";
        }
        return new PopularMoviesLoader(this, lang, page);

    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        if (result.resultType == ResultType.OK) {
            if (result.data != null && !result.data.isEmpty()) {
                displayContent(result.data);
            } else {
                displayError(result.resultType);
            }
        } else {
            displayError(result.resultType);
        }
    }


    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {

    }

    private void displayContent(List<Movie> movies) {
        //TODO(adapter)

        if(adapter == null){
            adapter = new MoviesRecycleAdapter(this);
            recyclerView.setAdapter(adapter);
        }

        adapter.setMovies(movies);
        progressBar.setVisibility(View.GONE);
        errorTextview.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayError(ResultType resultType) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextview.setVisibility(View.VISIBLE);

        final int messageId;
        if (resultType == ResultType.NO_INTERNET) {
            messageId = R.string.no_internet;
        } else {
            messageId = R.string.error;
        }
        errorTextview.setText(messageId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LANGUAGE, Locale.getDefault().getLanguage());

    }
}
