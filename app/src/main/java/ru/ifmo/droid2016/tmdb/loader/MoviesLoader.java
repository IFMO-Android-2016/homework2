package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.Parser;

public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    public MoviesLoader(Context context) {
        super(context);
    }

    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        if (!isOnline()) {
            return new LoadResult<>(ResultType.NO_INTERNET, null);
        }
        HttpURLConnection conn = null;
        try {
            conn = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage());
            List<Movie> result = Parser.parse(conn.getInputStream());
            conn.disconnect();
            return new LoadResult<>(ResultType.OK, result);
        } catch (IOException e) {
            e.printStackTrace();
            return new LoadResult<>(ResultType.ERROR, null);
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
