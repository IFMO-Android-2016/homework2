package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.Parser;

public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private int[] pages;
    private int requestType;
    private String lang;

    public MoviesLoader(Context context, int[] pages, int requestType, String lang) {
        super(context);
        this.pages = pages;
        this.requestType = requestType;
        this.lang = lang;
    }

    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        if (!isOnline()) {
            return new LoadResult<>(ResultType.NO_INTERNET, null, requestType, pages.length);
        }
        HttpURLConnection conn = null;
        try {
            List<Movie> result = new ArrayList<>();
            for (Integer page : pages) {
                conn = TmdbApi.getPopularMoviesRequest(lang, page);
                result.addAll(Parser.parse(conn.getInputStream()));
                conn.disconnect();
            }
            return new LoadResult<>(ResultType.OK, result, requestType, pages.length);
        } catch (IOException e) {
            e.printStackTrace();
            return new LoadResult<>(ResultType.ERROR, null, requestType, pages.length);
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
