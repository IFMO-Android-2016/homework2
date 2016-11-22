package ru.ifmo.droid2016.tmdb.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Методы для работы с The Movie DB API
 *
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    private static final String API_KEY = "8a1092b70d758813cfe9d9a9ad805253";
    private final static String BASE_URL = "https://api.themoviedb.org/3/";


    private TmdbApi() {}

    private static HttpURLConnection getPopularMoviesRequest(String lang, int page) throws IOException {
        return (HttpURLConnection) new URL(BASE_URL +
                new Params("movie/popular",
                    "api_key", API_KEY, "language", lang, "page", page
                ).toString()
        ).openConnection();
    }

    private static JSONArray getPopularMoviesInJson(String lang, int page) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getPopularMoviesRequest(lang, page).getInputStream()));
        String read = reader.readLine();
        reader.close();
        JSONObject answer = new JSONObject(read);
        return answer.getJSONArray("results");
    }

    public static LoadResult<List<Movie>> getPopularMovies(String lang, int page) {
        try {
            JSONArray array = getPopularMoviesInJson(lang, page);
            List<Movie> result = new ArrayList<>(array.length());
            for(int i = 0; i < array.length(); ++i) {
                JSONObject json = array.getJSONObject(i);
                Movie movie = new Movie(json.getString("poster_path"), json.getString("original_title"),
                        json.getString("overview"), json.getString("title"));
                result.add(movie);
            }
            return new LoadResult<>(ResultType.OK, result);
        }catch(Exception ex) {
            return new LoadResult<>(ex instanceof UnknownHostException ? ResultType.NO_INTERNET : ResultType.ERROR, null);
        }
    }

    public static LoadResult<List<Movie>> getPopularMoviesForReloader(String lang, int page) {
        List<Movie> result = new ArrayList<>();
        for(int i = 1; i <= page; ++i) {
            LoadResult<List<Movie>> lr = getPopularMovies(lang, i);
            if(lr.resultType != ResultType.OK)
                return lr;
            result.addAll(lr.data);
        }
        return new LoadResult<>(ResultType.OK, result);
    }

}
