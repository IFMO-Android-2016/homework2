package ru.ifmo.droid2016.tmdb.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.PopularMoviesActivity;
import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by Aleksandr Tukallo on 21.11.16.
 */
public class PagesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private List<Movie> data = null;
    ResultType resultType = ResultType.ERROR;

    public PagesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (data == null)
            forceLoad();
        else
            deliverResult(new LoadResult<List<Movie>>(resultType, data));
    }

    private void parseResponse(InputStream in) throws Exception {
        final JSONObject jsonObj = new JSONObject(IOUtils.readToString(in, "UTF-8"));
        parseJson(jsonObj);
    }

    private void parseJson(JSONObject jsonObj) throws Exception {
        data = new ArrayList<>();
        JSONArray arr = jsonObj.getJSONArray("results");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject movie = arr.optJSONObject(i);
            if (movie == null)
                continue;
            data.add(new Movie(movie.optString("poster_path"),
                    movie.optString("original_title"),
                    movie.optString("overview"),
                    movie.optString("title")));
        }
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        final StethoURLConnectionManager manager = new StethoURLConnectionManager("API");

        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage());
            Log.d(PopularMoviesActivity.TAG, "Establishing connection: " + connection.getURL());

            manager.preConnect(connection, null);
            connection.connect();
            manager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                in = manager.interpretResponseStream(in);

//                IOUtils.readFully(in); //tmp, while parser isn't ready
                parseResponse(in);

                resultType = ResultType.OK;
            } else {
                // consider all other codes as errors
                throw new BadResponseException("HTTP: " + connection.getResponseCode()
                        + ", " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            manager.httpExchangeFailed(e);
            try {
                if (IOUtils.isConnectionAvailable(getContext(), false)) {
                    resultType = ResultType.ERROR;
                } else {
                    resultType = ResultType.NO_INTERNET;
                }
            } catch (Exception f) {
                f.printStackTrace();
            }
        } catch (Exception e) {
            Log.e(PopularMoviesActivity.TAG, "Couldn't get movies: ", e);
        } finally {
            IOUtils.closeSilently(in);
            if (connection != null)
                connection.disconnect();
        }
        return new LoadResult<>(resultType, data);
    }
}
