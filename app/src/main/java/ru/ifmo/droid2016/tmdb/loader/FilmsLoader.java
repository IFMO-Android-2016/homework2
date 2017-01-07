package ru.ifmo.droid2016.tmdb.loader;

import android.support.v4.content.AsyncTaskLoader;

import java.util.List;
import ru.ifmo.droid2016.tmdb.model.Movie;


import android.content.Context;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;
/**
 * Created by Averin Maxim on 06.01.2017.
 */

public class FilmsLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private static final String TAG = "Films";
    private final int page;
    private boolean loaded = false;

    public FilmsLoader(Context context, int page) {
        super(context);
        this.page = page;
    }

    @Override
    protected void onStartLoading() {
        if (!loaded) {
            forceLoad();
        }
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");
        List<Movie> data = null;
        ResultType result = ResultType.ERROR;
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage(), page);
            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                JSONObject json = new JSONObject(IOUtils.readToString(in, "UTF-8"));
                Log.i(TAG, "Everything is ok!");
                data = FilmsParser.parseFilms(json);
                result = ResultType.OK;
            } else {
                throw new BadResponseException("HTTP: " + connection.getResponseCode()
                        + ", " + connection.getResponseMessage());
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to get films", e);

        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                result = ResultType.ERROR;
            } else {
                result = ResultType.NO_INTERNET;
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to get films: ", e);

        } finally {
            IOUtils.closeSilently(in);
            if (connection != null) {
                connection.disconnect();
            }
        }
        if (result == ResultType.OK) {
            loaded = true;
        }
        return new LoadResult<List<Movie>>(result, data);
    }
}
