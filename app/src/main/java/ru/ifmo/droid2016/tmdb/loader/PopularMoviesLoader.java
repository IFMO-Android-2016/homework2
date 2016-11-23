package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

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
 * Created by garik on 21.11.16.
 */

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private int page;

    public PopularMoviesLoader(Context context, int page) {
        super(context);
        this.page = page;
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {

        ResultType resultType = ResultType.ERROR;
        List<Movie> data = new ArrayList<>();

        HttpURLConnection connection = null;
        InputStream in = null;
        for (int i = 0; i < page; i++) {
            final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

            try {
                connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage(), page);
                stethoManager.preConnect(connection, null);
                connection.connect();
                stethoManager.postConnect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    in = connection.getInputStream();
                    in = stethoManager.interpretResponseStream(in);

                    data.addAll(JSONMoviesParser.parseMovies(in));

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
            } catch (BadResponseException | JSONException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeSilently(in);
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        return new LoadResult<>(resultType, data);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
