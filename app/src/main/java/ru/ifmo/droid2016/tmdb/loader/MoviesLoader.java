package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.content.Loader;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by Nik on 08.03.2017.
 */

public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private int page;
    private final String TAG = "Loader";

    public MoviesLoader(Context context, int page) {
        super(context);
        this.page = page;
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        ResultType resultType = ResultType.ERROR;
        List<Movie> data = new ArrayList<>();

        HttpURLConnection connection = null;
        InputStream in = null;
        StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");
        try {
            connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage(), page);
            Log.d(TAG, "Performing request: " + connection.getURL());
            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                in = stethoManager.interpretResponseStream(in);

                data = MoviesParser.parseMovies(in);

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
        return new LoadResult<>(resultType, data);
    }
}
