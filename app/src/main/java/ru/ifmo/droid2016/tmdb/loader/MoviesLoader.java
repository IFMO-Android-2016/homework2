package ru.ifmo.droid2016.tmdb.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;


/**
 * Created by Roman on 12/11/2016.
 */

public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private final String LOG_TAG = "loadInBackground";

    public MoviesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        //TODO Stetho
        HttpURLConnection connection = null;
        InputStream in = null;
        ResultType resultType = ResultType.ERROR;
        List<Movie> movies = null;
        try {
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage());
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    in = connection.getInputStream();
                    resultType = ResultType.OK;
                    movies = MoviesPullParser.parseMovies(in);
                } else {
                    resultType = ResultType.ERROR;
                    throw new BadResponseException("HTTP: " + connection.getResponseCode() + ": " +
                            connection.getResponseMessage());
                }
            } else {
                resultType = ResultType.NO_INTERNET;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "failed to get list of movies", e);
        } finally {
            IOUtils.closeSilently(in);
            if (connection != null)
                connection.disconnect();
        }
        return new LoadResult<>(resultType, movies);
    }
}
