package ru.ifmo.droid2016.tmdb.loader;

import android.support.v4.content.AsyncTaskLoader;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private int pageId;
    private String language;
    private Context appContext;

    public PopularMoviesLoader(Context context, Bundle bundle) {
        super(context);

        init(context, bundle);
    }

    public void init(Context context, Bundle bundle) {
        appContext = context;

        this.language = bundle.getString("LANG");
        pageId = bundle.getInt("PAGE_ID");
    }

    @Override
    public void onStartLoading() {
        super.onStartLoading();
        super.forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        HttpsURLConnection connection;
        try {
            connection = TmdbApi.getPopularMoviesRequest(pageId, language);
        } catch (Exception ex) {
            return new LoadResult<>(ResultType.ERROR, null);
        }
        LoadResult<List<Movie>> res = new LoadResult<>(ResultType.ERROR, null);

        if (!IOUtils.isConnectionAvailable(appContext, false)) {
            return new LoadResult<>(ResultType.NO_INTERNET, null);
        }

        int connectionResponseCode = 0;

        try {
            connectionResponseCode = connection.getResponseCode();
            if (connectionResponseCode != 200) {
                return res;
            }
        } catch (Exception ex) {
            return res;
        }

        try {
            res = GetPopularPullParser.parse(new BufferedInputStream(connection.getInputStream()));
        } catch (Exception ex) {
        } finally {
            connection.disconnect();
        }
        return res;
    }
}