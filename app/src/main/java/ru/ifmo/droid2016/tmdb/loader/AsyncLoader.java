package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.PopularMoviesActivity;
import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;
import ru.ifmo.droid2016.tmdb.utils.JsonParser;

/**
 * Created by Ivan-PC on 07.12.2016.
 */

public class AsyncLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private static final String LOG_TAG = AsyncLoader.class.getSimpleName();

    private int page;
    private LoadResult<List<Movie>> data;

    public AsyncLoader(Context context, int page) {
        super(context);
        this.page = page;
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        HttpURLConnection connection = null;
        try {
            connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage(), page);
            connection.setRequestMethod("GET");
            connection.connect();
            Log.d(LOG_TAG, "Loading...");
            InputStream in = connection.getInputStream();
            String strJson = IOUtils.readToString(in, "UTF-8");
            List<Movie> answer = JsonParser.parse(strJson);
            data = new LoadResult<>(ResultType.OK, answer);
            return data;
        }catch(IOException e) {
            Log.d(LOG_TAG, "No internet");
            return new LoadResult<>(ResultType.NO_INTERNET, null);
        }catch (JSONException e) {
            Log.d(LOG_TAG, "Can't parse JSON string");
            return new LoadResult<>(ResultType.ERROR, null);
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    public void deliverResult(LoadResult<List<Movie>> data) {
        this.data = data;
        super.deliverResult(data);
    }

    @Override
    public void forceLoad() {
        page = PopularMoviesActivity.page;
        super.forceLoad();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (data != null) {
            deliverResult(data);
        }
        else {
            forceLoad();
        }
    }
}
