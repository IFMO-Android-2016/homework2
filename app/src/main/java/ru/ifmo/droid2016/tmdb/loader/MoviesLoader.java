package ru.ifmo.droid2016.tmdb.loader;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * Created by penguinni on 20.11.16.
 */

public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    public MoviesLoader(Context context, String lang, int page) {
        super(context);
        this.lang = lang;
        this.page = page;
    }

    private String lang;
    private int page;
    private LoadResult<List<Movie>> result = null;

    public boolean finished() {
        return this.result != null;
    }

    @Override
    protected void onStartLoading() {
        if (result == null) {
            Log.i(TAG, "onStartLoading: page=" + page);
            forceLoad();
        } else {
            deliverResult(this.result);
        }
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        ResultType resultType = ResultType.ERROR;
        HttpURLConnection connection = null;
        InputStream in = null;
        List<Movie> movies = null;

        try {
            connection = TmdbApi.getPopularMoviesRequest(lang, page);
                Log.d(TAG, "Performing request: " + connection.getURL());

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = stethoManager.interpretResponseStream(connection.getInputStream());

                movies = MoviesPullParser.parseMovies(in);
                resultType = ResultType.OK;
            } else {
                throw new BadResponseException("HTTP: " + connection.getResponseCode()
                        + ", " + connection.getResponseMessage());
            }

        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to get movies: ", e);

        } finally {
            IOUtils.closeSilently(in);
            if (connection != null) {
                connection.disconnect();
            }
        }

        this.result = new LoadResult<>(resultType, movies, page);
        return this.result;
    }

    private static final String TAG = "Movies";
}