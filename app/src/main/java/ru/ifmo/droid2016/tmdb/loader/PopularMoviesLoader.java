package ru.ifmo.droid2016.tmdb.loader;

//import android.content.AsyncTaskLoader;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

import static ru.ifmo.droid2016.tmdb.loader.ResultType.OK;

/**
 * Created by Mr.Fiskerton on 012 12.11.16.
 */

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private final String language;
    private final int    page;
    private boolean      isLoaded = false;
    private LoadResult<List<Movie>> listLoadResult;

    public PopularMoviesLoader(Context context, String language, int page) {
        super(context);
        this.language = language;
        this.page = page;
    }

    @Override
    protected void onStartLoading() {
        if (!isLoaded || listLoadResult == null) {
          //  forceLoad();
        }  else {
            deliverResult(listLoadResult); //return data to activity
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
            connection = TmdbApi.getPopularMoviesRequest(language, page);
            Log.d(TAG, "Performing request: " + (connection.getURL()).toString());

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = stethoManager.interpretResponseStream(connection.getInputStream());
                data = MoviesDomParser.parseMovies(in);
                resultType = OK;
            } else {
                // consider all other codes as errors
                throw new BadResponseException("HTTP: " + connection.getResponseCode()
                        + ", " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(TAG, "loadInBackground: IOE ", e);
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }
        } catch (BadResponseException e) {
            Log.e(TAG, "Failed to get response", e);
        } catch (JSONException e) {
            Log.e(TAG, "Parsing filed: ", e);
        } catch (Exception e) {
            Log.e(TAG, "Failed to get movies: ", e);
        } finally {
            IOUtils.closeSilently(in);
            if (connection != null) {
                connection.disconnect();
            }
        }

        if (resultType == OK) { isLoaded = true; }

        return new LoadResult<>(resultType, data);
    }

    private static final String TAG = "BackgroundMoviesLoader";
}
