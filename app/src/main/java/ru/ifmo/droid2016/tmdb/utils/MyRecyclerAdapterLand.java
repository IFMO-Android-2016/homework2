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

import ru.ifmo.droid2016.tmdb.R;
import ru.ifmo.droid2016.tmdb.TmdbDemoApplication;
import ru.ifmo.droid2016.tmdb.model.Movie;

public class MyRecyclerAdapterLand extends RecyclerView.Adapter<MyRecyclerAdapterLand.ViewHolder>
        implements MyRecyclerAdapter {

    private static final String LOG_TAG = "my_tag";

    private Vector<Movie> movies = new Vector<>();

    public Vector<Movie> getData() {
        return movies;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView localizedTitle;
        public SimpleDraweeView mSimpleDraweeView;
        public TextView overviewText;

        public ViewHolder(View v) {
            super(v);

            localizedTitle = (TextView) v.findViewById(R.id.localized_title);
            mSimpleDraweeView = (SimpleDraweeView) v.findViewById(R.id.my_image_view);

            mSimpleDraweeView.getLayoutParams().width = (int)(TmdbDemoApplication.displayHeight / (1.4142));
            mSimpleDraweeView.getLayoutParams().height = TmdbDemoApplication.displayHeight;
            mSimpleDraweeView.requestLayout();

            Log.d(LOG_TAG + "new", " in new adapter");

            Log.d(LOG_TAG + "new", " ???? :" + mSimpleDraweeView.getLayoutParams().width + " "
                    + mSimpleDraweeView.getLayoutParams().height);


            overviewText = (TextView) v.findViewById(R.id.overview_text);
        }
    }

    public MyRecyclerAdapterLand() {
        movies = new Vector<Movie>();
    }

    @Override
    public MyRecyclerAdapterLand.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

        MyRecyclerAdapterLand.ViewHolder vh = new MyRecyclerAdapterLand.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyRecyclerAdapterLand.ViewHolder holder, int position) {
        holder.localizedTitle.setText(movies.elementAt(position).localizedTitle);

        String str = baseURL + "w500" + movies.elementAt(position).posterPath;
        holder.mSimpleDraweeView.setImageURI(Uri.parse(str));

        holder.overviewText.setText(movies.elementAt(position).overviewText);

        Log.d(LOG_TAG, str);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void addItemToEnd(Movie element) {
        movies.addElement(element);
        notifyItemInserted(movies.size() - 1);
    }
}
