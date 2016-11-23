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

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by ivan on 21.11.16.
 */

public class TMBDLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private final String lang;
    private static final String TAG = "Loader";
    private boolean isLoaded = false;
    private LoadResult<List<Movie>> listOfMovies;

    public TMBDLoader(Context context, String lang) {
        super(context);
        this.lang = lang;
    }


    @Override
    protected void onStartLoading() {
        if(!isLoaded) {
            forceLoad();
        } else {
            deliverResult(listOfMovies);
        }
    }


    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        ResultType resultType = ResultType.ERROR;
        List<Movie> data = null;

        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            connection = TmdbApi.getPopularMoviesRequest(lang);
            Log.d(TAG, "Performing request: " + connection.getURL());

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "HTTP_OK");
                in = connection.getInputStream();
                in = stethoManager.interpretResponseStream(in);

                data = DOMParser.parseList(in);

                resultType = ResultType.OK;
                isLoaded = true;
                Log.d(TAG, "ResultType.OK");
            } else {
                throw new BadResponseException("HTTP: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }

        } catch (IOException e) {
            Log.d(TAG, "There are some problems woth IOException");
            stethoManager.httpExchangeFailed(e);
            if(IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }
        } catch (JSONException e) {
            Log.d(TAG, "Failed to get movies: ", e);
        } catch (BadResponseException e) {

        } finally {
            IOUtils.closeSilently(in);
            if(connection != null) {
                connection.disconnect();
            }
        }

        Log.d(TAG, "Loader finished his job");
        listOfMovies = new LoadResult<>(resultType, data);
        return listOfMovies;
    }

}
