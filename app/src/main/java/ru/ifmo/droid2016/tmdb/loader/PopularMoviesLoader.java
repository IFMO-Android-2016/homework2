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

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private final int page;

    public PopularMoviesLoader(Context context, int page) {
        super(context);
        this.page = page;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");
        Log.d(TAG, "Load pages " + page);
        ResultType resultType = ResultType.ERROR;
        final List<Movie> data = new ArrayList<>();
        HttpURLConnection connection;
        InputStream in = null;
        for (int cntPage = 1; cntPage <= page; cntPage++){
            try {
                connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage(), cntPage);
                stethoManager.preConnect(connection, null);
                connection.connect();
                stethoManager.postConnect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    in = connection.getInputStream();
                    in = stethoManager.interpretResponseStream(in);

                    data.addAll(MoviesParser.parseMovies(in));

                    resultType = ResultType.OK;
                } else {
                    stethoManager = new StethoURLConnectionManager("API");
                    throw new BadResponseException("HTTP: " + connection.getResponseCode());
                }
                stethoManager = new StethoURLConnectionManager("API");
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
    private final static String TAG = "MoviesLoader";
}