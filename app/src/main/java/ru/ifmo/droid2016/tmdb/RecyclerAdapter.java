package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.content.Loader;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Ivan-PC on 07.12.2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String LOG_TAG = RecyclerAdapter.class.getSimpleName();
    private static final int INF = 2000000;
    private List<Movie> data;
    private LayoutInflater layoutInflater;
    private static RecyclerAdapter instance;
    private Context context;


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, overview;
        SimpleDraweeView image;

        public ViewHolder(View v) {
            super(v);
            image = (SimpleDraweeView) v.findViewById(R.id.imageInRecyclerView);
            title = (TextView) v.findViewById(R.id.titleInRecyclerView);
            overview = (TextView) v.findViewById(R.id.overviewInRecyclerView);
        }
    }

    static RecyclerAdapter getInstance(Context context, List<Movie> data) {
        if (instance == null) {
            instance = new RecyclerAdapter(context, data);
        }
        else {
            instance.data.addAll(data);
        }
        return instance;
    }

    public RecyclerAdapter (Context context, List<Movie> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position >= data.size()) {
            PopularMoviesActivity.page += 1;
            PopularMoviesActivity.loader.forceLoad();

            holder.image.setImageURI(Uri.parse("android.resource://ru.ifmo.droid2016.tmdb/drawable/ico.png"));
            holder.title.setText(context.getString(R.string.downloading));
            holder.overview.setText("");
            return;
        }

        Log.d(LOG_TAG, "onBindViewHolder");
        Uri uri = Uri.parse("https://image.tmdb.org/t/p/w500" + data.get(position).posterPath);
        Log.d(LOG_TAG, "Download Picture" + uri.toString());
        holder.image.setImageURI(uri);

        holder.title.setText(data.get(position).localizedTitle);
        holder.overview.setText(data.get(position).overviewText);
    }

    @Override
    public int getItemCount() {
        return INF;
    }
}
