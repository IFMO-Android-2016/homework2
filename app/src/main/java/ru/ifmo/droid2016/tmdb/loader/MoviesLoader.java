package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    public MoviesLoader(Context context) {
        super(context);
    }
    List <Movie> ans = null;
    ResultType res = ResultType.ERROR;

    @Override
    protected void onStartLoading() {
        if (ans != null)
            deliverResult(new LoadResult<>(res, ans));
        else
            forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        StethoURLConnectionManager manager = new StethoURLConnectionManager("API");
        HttpURLConnection con = null;
        InputStream in = null;

        try {
            con = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage(), 1);
            manager.preConnect(con, null);

            con.connect();
            manager.postConnect();

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = con.getInputStream();
                in = manager.interpretResponseStream(in);
                ans = Parser.parseMovie(in);
                res = ResultType.OK;
            }
        } catch (IOException e) {
            manager.httpExchangeFailed(e);
            try {
                if (IOUtils.isConnectionAvailable(getContext(), false)) {
                    res = ResultType.ERROR;
                } else {
                    res = ResultType.NO_INTERNET;
                }
            } catch (Exception f) {
                f.printStackTrace();
            }
        } catch (Exception e) {
            Log.e("Mov", "Something bad happened while taking movies");
        } finally {
            IOUtils.closeSilently(in);
            if (con != null)
                con.disconnect();
        }
        return new LoadResult<>(res, ans);
    }
}
