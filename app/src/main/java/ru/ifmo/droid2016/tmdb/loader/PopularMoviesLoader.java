package ru.ifmo.droid2016.tmdb.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by Andrey on 21.11.2016.
 */

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private final String language;
    private final String page;
    private static final String TAG = "Loader";

    public PopularMoviesLoader(Context context, String lang, String page) {
        super(context);
        this.language = lang;
        this.page = page;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        ResultType resultType = ResultType.ERROR;
        List<Movie> data = null;

        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            connection = TmdbApi.getPopularMoviesRequest(language, page);
            Log.d(TAG, "Performing request: " + connection.getURL());

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                in = stethoManager.interpretResponseStream(in);

                data = MovieDomParser.parseMovies(in);

                resultType = ResultType.OK;

            } else {
                throw new BadResponseException("HTTP: " + connection.getResponseCode() + ", " +
                        connection.getResponseMessage());
            }


        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to get movies", e);

        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to get Movies: ", e);

        } finally {
            IOUtils.closeSilently(in);
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new LoadResult<>(resultType, data);
    }


}
