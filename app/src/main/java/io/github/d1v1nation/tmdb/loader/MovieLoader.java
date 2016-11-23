package io.github.d1v1nation.tmdb.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;

import io.github.d1v1nation.tmdb.api.TmdbApi;
import io.github.d1v1nation.tmdb.model.Movie;
import io.github.d1v1nation.tmdb.utils.IOUtils;

/**
 * @author d1v1nation (catanaut@gmail.com)
 *         <p>
 *         23.11.16 of movies | io.github.d1v1nation.tmdb.loader
 */
public class MovieLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private final static String TAG = "Movies::Loader";


    public MovieLoader(Context context) {
        super(context);
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

                data = MovieParser.parseMovies(in);

                resultType = ResultType.OK;

            } else {
                // consider all other codes as errors
                throw new BadResponseException("HTTP: " + connection.getResponseCode()
                        + ", " + connection.getResponseMessage());
            }


        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to get stuff", e);

        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to get stuff: ", e);

        } finally {
            IOUtils.closeSilently(in);
            if (connection != null) {
                connection.disconnect();
            }
        }

        return new LoadResult<>(resultType, data);
    }
}
