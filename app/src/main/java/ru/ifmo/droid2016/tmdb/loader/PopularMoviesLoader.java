package ru.ifmo.droid2016.tmdb.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;

import ru.ifmo.droid2016.tmdb.model.Movie;

import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by roman on 11.11.16.
 */

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private LoadResult<List<Movie>> movieList;
    private int page;

    public PopularMoviesLoader(Context context, int page) {
        super(context);
        this.page = page;
    }

    protected void onStartLoading() {
        if (movieList == null) {
            Log.d(TAG, "request new load");
            forceLoad();
        } else {
            deliverResult(movieList);
        }
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        ResultType resultType = ResultType.ERROR;
        List<Movie> data = new ArrayList<>();

        HttpURLConnection connection = null;
        InputStream in = null;
        for (int i = 1; i < page + 1; i++) {
            final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");
            try {
                connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage(), i);
                Log.d(TAG, "Performing request: " + connection.getURL());
                stethoManager.preConnect(connection, null);
                connection.connect();
                stethoManager.postConnect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    in = connection.getInputStream();
                    in = stethoManager.interpretResponseStream(in);

                    data = MovieParser.parseMovies(in);

                    resultType = ResultType.OK;
                }

            } catch (MalformedURLException e) {
                Log.e(TAG, "Failed to get movies: ", e);
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
        }
        movieList = new LoadResult<>(resultType, data);
        return movieList;
    }

    private static final String TAG = "Movies";
}
