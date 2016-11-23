package ru.ifmo.droid2016.tmdb.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
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

/**
 * Created by egorsafronov on 00.11.16.
 */

public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>>{

    public MoviesLoader(Context context) {
        super(context);
    }

    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public LoadResult<List<Movie>> loadInBackground() {

        ResultType resultType = ResultType.ERROR;
        List<Movie> data = null;
        HttpURLConnection connection = null;
        InputStream in = null;

        StethoURLConnectionManager stechoManager = new StethoURLConnectionManager("API");
        try {
            connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage());
            stechoManager.preConnect(connection, null);
            connection.connect();
            stechoManager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                in = stechoManager.interpretResponseStream(in);
                data = MoviesParser.parse(in);
                resultType = ResultType.OK;
            } else {
                throw new BadResponseException("HTTP: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            stechoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }
        }
        catch (Exception e) {
            Log.e("Movies", "Failed to load movies: ", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return new LoadResult<>(resultType, data);
    }
}
