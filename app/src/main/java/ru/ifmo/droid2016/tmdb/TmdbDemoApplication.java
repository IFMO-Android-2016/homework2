package ru.ifmo.droid2016.tmdb;

import android.app.Application;
import android.view.Display;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

public class TmdbDemoApplication extends Application {
    public static int displayHeight;
    public static int displayWidth;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        Fresco.initialize(this);
    }


    //public void download

}
