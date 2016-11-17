package ru.ifmo.droid2016.tmdb.loader;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by Михаил on 16.11.2016.
 */

public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private final int page;
    public MoviesLoader(Context context, int page) {
        super(context);
        this.page = page;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {

        ResultType resultType = ResultType.ERROR;
        final List<Movie> data = new ArrayList<>();

        HttpURLConnection connection = null;
        InputStream in = null;

        for (int pageNumber = 1; pageNumber < page + 1; pageNumber++) {
            final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");
            try {
                connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage(), String.valueOf(pageNumber));
                Log.d(TAG, "Performing request: " + connection.getURL());
                stethoManager.preConnect(connection, null);
                connection.connect();
                stethoManager.postConnect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    in = connection.getInputStream();
                    in = stethoManager.interpretResponseStream(in);

                    data.addAll(Parser.parse(in));

                    resultType = ResultType.OK;
                } else {
                    throw new BadResponseException("HTTP: "
                            + connection.getResponseCode()
                            + ", "
                            + connection.getResponseMessage());
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
        }


        return new LoadResult<>(resultType, data);
    }
    private final static String TAG = "Movies";
}
