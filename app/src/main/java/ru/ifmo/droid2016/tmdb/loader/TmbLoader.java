package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

public class TmbLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private static final String TAG = "Movies";

    public TmbLoader(Context context) {
        super(context);
        Log.d(TAG, "TmbLoader: constructor");
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    public void deliverResult(LoadResult<List<Movie>> data) {
        Log.d(TAG, "deliverResult");
        super.deliverResult(data);
    }

    @Override
    protected void onForceLoad() {
        Log.d(TAG, "onForceLoad");
        super.onForceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        ResultType resultType = ResultType.ERROR;
        List<Movie> data = null;

        HttpURLConnection connection = null;
        InputStream in = null;

        try {

            connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage());

            Log.d(TAG, "Performing request: " + connection.getURL());

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                in = connection.getInputStream();
                in = stethoManager.interpretResponseStream(in);

                data = TmbParser.parseTmb(in);

                resultType = ResultType.OK;

            } else {
                throw new BadResponseException("HTTP: " + connection.getResponseCode()
                        + ", " + connection.getResponseMessage());
            }


        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to get movies", e);

        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.NO_INTERNET;
            } else {
                resultType = ResultType.ERROR;
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to get films: ", e);

        } finally {
            IOUtils.closeSilently(in);
            if (connection != null) {
                connection.disconnect();
            }
        }

        Log.d(TAG, "Request finished");

        return new LoadResult<>(resultType, data);
    }

}
