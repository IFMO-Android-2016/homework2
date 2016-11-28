package ru.ifmo.droid2016.tmdb.loader;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by YA on 20.11.2016.
 */
public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private LoadResult<List<Movie>> result;
    private String language;

    public MoviesLoader (Context context, String language) {
        super(context);
        this.language = language;
    }

    @Override
    protected void onStartLoading() {
        Log.d("LoadingStart", "");
        if (result == null) {
            forceLoad();
        } else {
            deliverResult(result);
        }
    }

    boolean isInternetConnection () {
        NetworkInfo netState = ((ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return netState != null && netState.isConnectedOrConnecting();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground () {
        Log.d("LoadingInBackground", language);
        if (!isInternetConnection()) {
            return new LoadResult<>(ResultType.NO_INTERNET, null);
        }
        HttpURLConnection connection = null;
        List<Movie> curData = new ArrayList<Movie>();
        try {
            connection = TmdbApi.getPopularMoviesRequest(language);
            curData.addAll(MovieParser.parseMovies(connection.getInputStream()));
            connection.disconnect();
            result = new LoadResult<>(ResultType.OK, curData);
            return result;
        } catch (Exception e) {
            Log.e("loadInBackground", "some_error", e);
            return new LoadResult<>(ResultType.ERROR, null);
        }
        finally {
            if (connection != null)
                connection.disconnect();
        }
    }
}
