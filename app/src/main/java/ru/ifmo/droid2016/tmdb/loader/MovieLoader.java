package ru.ifmo.droid2016.tmdb.loader;

import android.content.res.Resources;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONException;

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
 * Created by maria on 17.11.16.
 */
public class MovieLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private static final String TAG = "Movies";
    private String currentLang;

    public MovieLoader(Context context, String currentLang) {
        super(context);
        this.currentLang = currentLang;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");
        ResultType resultType = ResultType.ERROR;
        List<Movie> data = null;
        HttpURLConnection connection = null;
        InputStream input = null;
        if (!Locale.getDefault().getLanguage().equals(currentLang)) {
            currentLang = Locale.getDefault().getLanguage();
        }

        try {
            connection = TmdbApi.getPopularMoviesRequest(currentLang);
            Log.d(TAG, "Performing request: " + connection.getURL());
            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                input = connection.getInputStream();
                input = stethoManager.interpretResponseStream(input);
                Log.d("Response", "All right");
                data = ResponseParser.parseMovies(input);
                resultType = ResultType.OK;
            }
        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            try {
                if (IOUtils.isConnectionAvailable(getContext(), false)) {
                    resultType = ResultType.NO_INTERNET;
                } else {
                    resultType = ResultType.ERROR;
                }
            } catch (Exception k) {
                Log.e("Loading movie", "Failed");
            }
        } catch (Exception e) {
            Log.e("Loading movie", "Failed");
        } finally {
            IOUtils.closeSilently(input);
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new LoadResult<>(resultType, data);
    }
}
