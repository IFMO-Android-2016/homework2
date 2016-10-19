package ru.ifmo.droid2016.tmdb.apiLoaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by Alex on 19.10.2016.
 */

public class apiLoaderGetPopularMovies extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private final int page;

    public apiLoaderGetPopularMovies(Context context, int page) {
        super(context);
        this.page = page;
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        if (!IOUtils.isConnectionAvailable(getContext(), false)) {
            return new LoadResult<>(ResultType.NO_INTERNET, null);
        }

        final List<Movie> movies = new ArrayList<>();

        try {
            HttpURLConnection connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage().toString(), page);
            //String response = IOUtils.readToString(connection.getInputStream(), "utf-8");
            JsonReader jr = new JsonReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            jr.beginObject();
            while (jr.hasNext())
            {
                if (jr.nextName() == "results")
                {
                    jr.beginArray();
                    while (jr.hasNext())
                    {
                        movies.add(Movie.parseMovie(jr));
                    }
                    jr.endArray();
                } else {
                    jr.skipValue();
                    continue;
                }
            }
            jr.endObject();
            jr.close();
        } catch (IOException e) {
            return new LoadResult<>(ResultType.ERROR, null);
        }

        return new LoadResult<>(ResultType.OK, movies);
    }



}
