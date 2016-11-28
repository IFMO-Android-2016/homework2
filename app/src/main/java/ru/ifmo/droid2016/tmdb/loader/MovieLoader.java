package ru.ifmo.droid2016.tmdb.loader;

        import android.content.Context;
        import android.support.v4.content.AsyncTaskLoader;
        import android.util.Log;

        import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

        import java.io.IOException;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.util.List;
        import java.io.InputStream;

        import ru.ifmo.droid2016.tmdb.api.TmdbApi;
        import ru.ifmo.droid2016.tmdb.model.Movie;
        import ru.ifmo.droid2016.tmdb.utils.IOUtils;

        import org.json.JSONObject;

/**
 * Created by Anya on 21.11.2016.
 */

public class MovieLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    public final String TAG = "Movies";
    private LoadResult<List<Movie>> result;

    private final String language;
    private final int page;

    public MovieLoader(Context context, String language, int page) {
        super(context);
        this.page = page;
        this.language = language;
    }

    @Override
    protected void onStartLoading() {
        if (result == null || result.resultType != ResultType.OK) {
            forceLoad();
        } else {
            deliverResult(result);
        }
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
            connection = TmdbApi.getPopularMoviesRequest(page, language);
            Log.d(TAG, "Performing request: " + connection.getURL());

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = stethoManager.interpretResponseStream(connection.getInputStream());
                JSONObject json = new JSONObject(IOUtils.readToString(in, "UTF-8"));
                data = MovieParser.parseMovies(json);
                resultType = ResultType.OK;

            } else {
                // consider all other codes as errors
                throw new BadResponseException("HTTP: " + connection.getResponseCode()
                        + ", " + connection.getResponseMessage());
            }


        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to get movies", e);
            resultType = ResultType.ERROR;

        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to get movies: ", e);
            resultType = ResultType.ERROR;

        } finally {
            IOUtils.closeSilently(in);
            if (connection != null) {
                connection.disconnect();
            }
        }
        if (resultType != ResultType.OK) {
            data = null;
        }
        result = new LoadResult<>(resultType, data);
        return result;
    }

    public LoadResult<List<Movie>> getResult() {
        return result;
    }
}