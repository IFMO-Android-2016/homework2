package ru.ifmo.droid2016.tmdb;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import ru.ifmo.droid2016.tmdb.model.Movie;

public class TmdbDemoApplication extends Application {
    private static final String LOG_TAG = "my_tag";

    public static int displayHeight;
    public static int displayWidth;

    public static Vector<Movie> savedMovies = null;
    public static Set<Integer> queue = new TreeSet<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        Fresco.initialize(this);
    }
}
