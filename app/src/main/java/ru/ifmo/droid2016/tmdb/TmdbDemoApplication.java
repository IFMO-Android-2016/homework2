package ru.ifmo.droid2016.tmdb;

import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

public class TmdbDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
