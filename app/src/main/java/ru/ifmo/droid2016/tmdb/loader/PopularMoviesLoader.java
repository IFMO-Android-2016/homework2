package ru.ifmo.droid2016.tmdb.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private static final String TAG = "Popular Movies";

    public PopularMoviesLoader(Context context) {
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

        try {
            connection = TmdbApi.getPopularMovies(Locale.getDefault().getLanguage());
            Log.d(TAG, "Performing request: " + connection.getURL());

            connection.setConnectTimeout(15000); // 15 sec
            connection.setReadTimeout(15000); // 15 sec

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                data = Parser.parse(
                        stethoManager.interpretResponseStream(connection.getInputStream()));
                resultType = ResultType.OK;
            } else {
                throw new BadResponseException("HTTP: " + connection.getResponseCode()
                        + ", " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);

            if (!IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.NO_INTERNET;
                Log.e(TAG, "Failed to get popular movies: internet connection is not available", e);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to get popular movies: unexpected error", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return new LoadResult<>(resultType, data);
    }

    private static class Parser {
        private Parser() {
        }

        private static JSONObject parseJSON(InputStream in) throws IOException, JSONException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            in = new BufferedInputStream(in);

            byte[] buffer = new byte[8192];
            int readSize;

            while ((readSize = in.read(buffer)) >= 0) {
                baos.write(buffer, 0, readSize);
            }

            in.close();

            String content = baos.toString("UTF-8");
            baos.close();

            return new JSONObject(content);
        }

        @NonNull
        static List<Movie> parse(InputStream in) throws IOException, JSONException {
            JSONArray res = parseJSON(in).getJSONArray("results");
            List<Movie> movies = new ArrayList<>();

            for (int i = 0; i < res.length(); i++) {
                JSONObject movie = res.getJSONObject(i);
                movies.add(new Movie(
                        movie.getString("title"),
                        movie.getString("original_title"),
                        movie.getString("overview"),
                        movie.getString("vote_average"),
                        movie.getString("poster_path"),
                        movie.getString("backdrop_path")
                ));
            }

            return movies;
        }
    }
}
