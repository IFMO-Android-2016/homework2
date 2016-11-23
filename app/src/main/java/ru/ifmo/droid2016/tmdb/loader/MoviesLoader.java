package ru.ifmo.droid2016.tmdb.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

import static android.content.Intent.ACTION_LOCALE_CHANGED;


/**
 * Created by Roman on 12/11/2016.
 */

public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private final String LOG_TAG = "loadInBackground";
    private LoadResult<List<Movie>> result;
    private BroadcastReceiver receiver = null;

    public MoviesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals(ACTION_LOCALE_CHANGED))
                        result = null;
                }
            };
        }
        getContext().registerReceiver(receiver,
                new IntentFilter(ACTION_LOCALE_CHANGED));
        if ((result == null) || (result.data == null))
            forceLoad();
        else
            deliverResult(result);
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("");
        HttpURLConnection connection = null;
        InputStream in = null;
        ResultType resultType = ResultType.ERROR;
        List<Movie> movies = null;
        try {
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage());
                Log.d(LOG_TAG, "Performing request: " + connection.getURL());
                stethoManager.preConnect(connection, null);
                connection.connect();
                stethoManager.postConnect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    in = connection.getInputStream();
                    in = stethoManager.interpretResponseStream(in);
                    resultType = ResultType.OK;
                    try {
                        movies = MoviesPullParser.parseMovies(in);
                    } catch (IOException e) {
                        throw new BadResponseException(e);
                    }
                } else {
                    resultType = ResultType.ERROR;
                    throw new BadResponseException("HTTP: " + connection.getResponseCode() + ": " +
                            connection.getResponseMessage());
                }
            } else {
                resultType = ResultType.NO_INTERNET;
            }
        } catch (BadResponseException e) {
            Log.e(LOG_TAG, "failed to get list of movies because of unexpected response from API", e);
        } catch (Exception e) {
            Log.e(LOG_TAG, "failed to get list of movies", e);
        } finally {
            IOUtils.closeSilently(in);
            if (connection != null)
                connection.disconnect();
        }
        result = new LoadResult<>(resultType, movies);
        return result;
    }
}
