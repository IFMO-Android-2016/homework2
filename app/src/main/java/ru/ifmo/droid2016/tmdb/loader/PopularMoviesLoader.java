package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private final int page;

    public PopularMoviesLoader(Context context, int page) {
        super(context);
        this.page = page;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        ResultType res = null;
        List<Movie> list = new ArrayList<>();
        HttpURLConnection conn;
        InputStream is = null;
        for (int i = 1; i <= page; i++) {
            StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");
            try {
                conn = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage(), i);
                stethoManager.preConnect(conn, null);
                conn.connect();
                stethoManager.postConnect();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    is = conn.getInputStream();
                    is = stethoManager.interpretResponseStream(is);
                    list.addAll(MoviesParser.parseMovies(is));
                    res = ResultType.OK;
                } else {
                    throw new BadResponseException("HTTP: " + conn.getResponseCode());
                }
            } catch (Exception e) {
                res = ResultType.ERROR;
            }
        }
        return new LoadResult<>(res, list);
    }
}
