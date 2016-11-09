package ru.ifmo.droid2016.tmdb.loader;

import android.support.v4.content.AsyncTaskLoader;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private final String LOG_TAG = "my_tag";

    private int pageId;
    private String language;
    private Context appContext;

    public PopularMoviesLoader(Context context, Bundle bundle) {
        super(context);

        Log.d(LOG_TAG, " constructor");

        appContext = context;

        this.language = bundle.getString("LANG");
        pageId = bundle.getInt("PAGE_ID");
    }

    @Override
    public void onStartLoading() {
        super.onStartLoading();

        Log.d(LOG_TAG, "onStartLoading");
        super.forceLoad();
    }

    @Override
    public void forceLoad() {
        Log.d(LOG_TAG, "forceLoad");
        super.forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        Log.d(LOG_TAG, " loadInBackground");

        HttpsURLConnection connection;
        try {
            connection = TmdbApi.getPopularMoviesRequest(pageId, language);
        } catch (Exception ex) {
            Log.d(LOG_TAG, "Error getPopularMoviesRequest\n");
            return new LoadResult(ResultType.ERROR, null);
        }
        LoadResult<List<Movie>> rez = null;


        Log.d(LOG_TAG, " connection " + String.valueOf(IOUtils.isConnectionAvailable(appContext, false)));
        //return "------";
        try {
            Log.d(LOG_TAG, String.valueOf(connection.getResponseCode()));
        } catch (Exception ex) {
            Log.d(LOG_TAG, "ERROR!!!!!!!!!!");
        }

        try {
            Log.d(LOG_TAG, "  start");

            rez = GetPopularPullParser.parse(new BufferedInputStream(connection.getInputStream()));

            Log.d(LOG_TAG, "OK_connection");
        } catch (Exception ex) {

            Log.d(LOG_TAG, "something wrong");

            Log.d(LOG_TAG, "???" + ex.toString());
        } finally {
            connection.disconnect();
        }
        /**/
        return new LoadResult(ResultType.OK, rez);
    }

    @Override
    public void deliverResult(LoadResult<List<Movie>> rez) {
        Log.d("deliverResult", "-");
        super.deliverResult(rez);
    }
}