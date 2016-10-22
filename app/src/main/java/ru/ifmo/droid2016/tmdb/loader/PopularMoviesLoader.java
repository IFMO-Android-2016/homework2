package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    public PopularMoviesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        Log.d(TAG, "Start loading");

        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        ResultType resultType = ResultType.ERROR;
        List<Movie> data = null;

        HttpURLConnection connection;

        try {
            connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage());
            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private final static String TAG = "MoviesLoader";
}
