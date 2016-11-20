package ru.ifmo.droid2016.tmdb.utils;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Vector;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import ru.ifmo.droid2016.tmdb.R;
import ru.ifmo.droid2016.tmdb.TmdbDemoApplication;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Vlad_kv on 12.11.2016.
 */

public interface MyRecyclerAdapter {
    String LOG_TAG = "my_tag";
    int MIN_NUM_REST_FILMS = 2;
    String baseURL = "https://image.tmdb.org/t/p/";


    @OverridingMethodsMustInvokeSuper
    public int getItemCount();
    public void addItemToEnd(Movie element);

    public Vector<Movie> getData();

}
