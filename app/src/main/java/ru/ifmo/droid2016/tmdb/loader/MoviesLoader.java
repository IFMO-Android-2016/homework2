package ru.ifmo.droid2016.tmdb.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;


/**
 * Created by vadim on 06.11.16.
 */

public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private final String lang;
    Context context;

    public MoviesLoader(Context context, String lang) {
        super(context);
        Log.d(TAG, "MoviesLoader");
        this.lang = lang;
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        onForceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        ResultType rt = ResultType.ERROR;
        List<Movie> data = null;
        Log.d(TAG, "loadInBackground");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        if (!IOUtils.isConnectionAvailable(context, false)) {
            rt = ResultType.NO_INTERNET;
            Log.d(TAG, "No internet");
        } else {
            try {
                HttpURLConnection connection = TmdbApi.getPopularMoviesRequest(lang);
                connection.connect();
                Log.d(TAG, "Perfoming request " + connection.getURL());
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    rt = ResultType.OK;
                    InputStream in = connection.getInputStream();
                    data = MovieJSONParser.parseMovies(in);
                } else {
                    throw new Exception("Unexpected ResponseCode from HTTP " + connection.getResponseCode());
                }
            } catch (Exception e) {
                Log.d(TAG, "ERROR " + e.getMessage());
            }
        }
        return new LoadResult<>(rt, data);
    }

    static final String TAG = "main";
}
