package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.database.CursorJoiner;
import android.support.v4.content.AsyncTaskLoader;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.Parser;
import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private int page;
    private String lang;
    private LoadResult<List<Movie>> result;

    public PopularMoviesLoader(Context context, int page) {
        super(context);
        this.page = page;
        this.lang = Locale.getDefault().getLanguage();
    }

    @Override
    protected void onStartLoading() {
        if (result != null) {
            deliverResult(result);
        }
        else {
            forceLoad();
        }
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");
        ResultType resultType = ResultType.ERROR;
        HttpURLConnection conn = null;
        List<Movie> data = null;
        InputStream in = null;
        try {
            conn = TmdbApi.getPopularMoviesRequest(lang, page);
            stethoManager.preConnect(conn, null);
            conn.connect();
            stethoManager.postConnect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                in = stethoManager.interpretResponseStream(in);
                data = Parser.parse(in);
                resultType = ResultType.OK;
            }
            else {
                throw new BadResponseException("HTTP " + conn.getResponseCode());
            }
        }
        catch (Exception e)
        {
            if (!IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.NO_INTERNET;
            }
            data = null;
        }
        finally {
            IOUtils.closeSilently(in);
            if (conn != null) conn.disconnect();
        }
        result = new LoadResult<>(resultType, data);
        return result;
    }
}
