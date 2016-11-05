package ru.ifmo.droid2016.tmdb;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.apiLoaders.apiLoaderGetPopularMovies;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecyclerViewAdapter;
import ru.ifmo.droid2016.tmdb.utils.SaveMoviesData;

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
    Handler handler;
    int page;


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
        recyclerAdapter = new RecyclerViewAdapter(currentMovies, rv);
        rv.setAdapter(recyclerAdapter);
        String language = Locale.getDefault().getLanguage();

        if (savedInstanceState != null) {
            page = savedInstanceState.getInt("page");
            lastLanguage = savedInstanceState.getString("locale");
        }

        if (lastLanguage != null && lastLanguage.compareTo(language) == 0) {
            getLoaderManager().initLoader(1, null, this);
        } else {
            Bundle args = new Bundle();
            args.putString("lang", language.toString());
            args.putInt("page", 1);
            page = 1;
            getLoaderManager().restartLoader(0, args, this);
        }

        lastLanguage = language;

        handler = new Handler();
        recyclerAdapter.onLoadNewItems(() -> {
            Log.d("API", "I HAVE START LOADED");
            Bundle args = new Bundle();
            args.putString("lang", lastLanguage);
            args.putInt("page", page);

            handler.post(() -> {
                currentMovies.add(null);
                recyclerAdapter.notifyItemInserted(currentMovies.size() - 1);
                getLoaderManager().restartLoader(0, args, this);
            });
        });


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("page", page);
        outState.putString("locale", lastLanguage);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == 0) {
            Loader<LoadResult<List<Movie>>> loader = new apiLoaderGetPopularMovies(this, args.getInt("page"), args.getString("lang"));
            loader.forceLoad();
            return loader;
        } else {
            Log.d("API","CREATE NEW LOADER");
            Loader<LoadResult<List<Movie>>> loader = new SaveMoviesData(this, currentMovies);
            loader.forceLoad();
            return loader;
        }
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        recyclerAdapter.setLoaded();
        if (loader.getId() == 0) {
            if (currentMovies.size() > 0) {
                currentMovies.remove(currentMovies.size() - 1);
                recyclerAdapter.notifyItemRemoved(currentMovies.size());
            }

            if (data.resultType == ResultType.OK) {
                currentMovies.addAll(data.data);
                recyclerAdapter.notifyItemRangeInserted(currentMovies.size() - data.data.size(), data.data.size());
                errorTextView.setVisibility(View.GONE);
                errorTextView.setText("");
                getLoaderManager().restartLoader(1, null, this);
                ++page;
            } else {
                String error = data.resultType == ResultType.ERROR ? "Произошла ошибка. Очень жаль." : "Вы забыли включить соединение с интернетом.";
                rv.setVisibility(View.GONE);
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText(error);
            }
        } else if (loader.getId() == 1 && currentMovies.size() == 0) {
            currentMovies.addAll(data.data);
            recyclerAdapter.notifyItemRangeInserted(0, data.data.size());
            errorTextView.setVisibility(View.GONE);
            errorTextView.setText("");
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
    }


}

