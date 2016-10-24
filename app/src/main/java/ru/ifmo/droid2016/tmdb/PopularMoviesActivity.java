package ru.ifmo.droid2016.tmdb;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.apiLoaders.apiLoaderGetPopularMovies;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecyclerViewAdapter;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    RecyclerView rv;
    LinearLayoutManager llm;
    RecyclerViewAdapter recyclerAdapter;
    List<Movie> currentMovies;
    TextView errorTextView;
    String lastLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        rv = (RecyclerView) findViewById(R.id.rv);
        errorTextView = (TextView) findViewById(R.id.errorText);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        currentMovies = new ArrayList<>();
        recyclerAdapter = new RecyclerViewAdapter(currentMovies);
        rv.setAdapter(recyclerAdapter);
        String language = Locale.getDefault().getLanguage();
        Bundle args = new Bundle();
        args.putString("lang", language.toString());
        args.putInt("page", 1);

        if (lastLanguage != null && lastLanguage.compareTo(language) != 0 ) {
            getLoaderManager().restartLoader(0, args, this);
            Log.d("Movies", "restartLoader");
        } else {
            getLoaderManager().initLoader(0, args, this);
            Log.d("Movies", "init");
        }
        lastLanguage = language;
    }


    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        Loader<LoadResult<List<Movie>>> loader = new apiLoaderGetPopularMovies(this, args.getInt("page"), args.getString("lang"));
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        if (data.resultType == ResultType.OK) {
            currentMovies.addAll(data.data);
            rv.setAdapter(new RecyclerViewAdapter(currentMovies));
            errorTextView.setVisibility(View.GONE);
            errorTextView.setText("");
        } else {
            String error = data.resultType == ResultType.ERROR ? "Произошла ошибка. Очень жаль." : "Вы забыли включить соединение с интернетом.";
            rv.setVisibility(View.GONE);
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText(error);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {

    }
}

