package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    public PopularMoviesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        Log.d(TAG, "Start loading");

        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        ResultType resultType = ResultType.ERROR;
        List<Movie> data = null;

        HttpURLConnection connection;
        InputStream in = null;

        try {
            connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage());
            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                in = stethoManager.interpretResponseStream(in);

                data = MoviesParser.parseMovies(in);

                resultType = ResultType.OK;
            } else {
                throw new BadResponseException("HTTP: " + connection.getResponseCode());
            }

        } catch (BadResponseException | JSONException e) {
            Log.e(TAG, "Failed to get movies", e);
        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }
        }

        return new LoadResult<>(resultType, data);
    }

    private final static String TAG = "MoviesLoader";
}
