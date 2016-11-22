package ru.ifmo.droid2016.tmdb.utils;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

import ru.ifmo.droid2016.tmdb.R;
import ru.ifmo.droid2016.tmdb.TmdbDemoApplication;
import ru.ifmo.droid2016.tmdb.model.Movie;


public abstract class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    protected final static String LOG_TAG = "my_tag";
    protected final int MIN_NUM_REST_FILMS = 10;
    protected final String baseURL = "https://image.tmdb.org/t/p/";

    protected boolean toDownload = false;
    protected Set<Integer> pagesNeededToUpd = new HashSet<>();
    protected Set<Integer> pagesWithError;

    protected Vector<Movie> movies = new Vector<>();
    //protected Vector<>

    public Vector<Movie> getData() {
        return movies;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView localizedTitle;
        public SimpleDraweeView mSimpleDraweeView;
        public TextView overviewText;
        public ProgressBar progressBar;
        public ImageView imageErrorView;

        public ViewHolder(View v) {
            super(v);

            localizedTitle = (TextView) v.findViewById(R.id.localized_title);
            mSimpleDraweeView = (SimpleDraweeView) v.findViewById(R.id.my_image_view);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            imageErrorView = (ImageView) v.findViewById(R.id.image_error_view);

            Log.d(LOG_TAG + "new", " in new adapter");

            Log.d(LOG_TAG + "new", " ???? :" + mSimpleDraweeView.getLayoutParams().width + " "
                    + mSimpleDraweeView.getLayoutParams().height);


            overviewText = (TextView) v.findViewById(R.id.overview_text);
        }
    }

    public MyRecyclerAdapter() {
        movies = new Vector<>();
    }

    public void init(Vector<Movie> films) {
        movies = films;
        if (films == null) {
            movies = new Vector<>();
        }
    }

    public boolean isNeededInDownloading() {
        if (toDownload) {
            toDownload = false;
            return true;
        }
        return false;
    }

    public void updItem(int pos, Movie film) {
        movies.setElementAt(film, pos);
        notifyItemChanged(pos);
    }

    public Set<Integer> getPagesNeededToUpd() {
        return pagesNeededToUpd;
    }

    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

        MyRecyclerAdapter.ViewHolder vh = new MyRecyclerAdapter.ViewHolder(v);
        return vh;
    }

    protected void setNormalItem(MyRecyclerAdapter.ViewHolder holder) {
        holder.progressBar.setVisibility(View.INVISIBLE);
        holder.mSimpleDraweeView.setVisibility(View.VISIBLE);
        holder.localizedTitle.setVisibility(View.VISIBLE);
        holder.overviewText.setVisibility(View.VISIBLE);

        holder.mSimpleDraweeView.getLayoutParams().width = (int)(TmdbDemoApplication.displayHeight / (1.4142));
        holder.mSimpleDraweeView.getLayoutParams().height = TmdbDemoApplication.displayHeight;
        holder.mSimpleDraweeView.requestLayout();
    }

    protected void setItemInDownload(MyRecyclerAdapter.ViewHolder holder) {
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.mSimpleDraweeView.setVisibility(View.INVISIBLE);
        holder.localizedTitle.setVisibility(View.INVISIBLE);
        holder.overviewText.setVisibility(View.INVISIBLE);

        holder.mSimpleDraweeView.getLayoutParams().width = 100;
        holder.mSimpleDraweeView.getLayoutParams().height = 100;
        holder.mSimpleDraweeView.requestLayout();
    }

    @Override
    public void onBindViewHolder(MyRecyclerAdapter.ViewHolder holder, int position) {

        if (position == movies.size()) {
            setItemInDownload(holder);
            return;
        }

        if (!movies.elementAt(position).locLang.equals(Locale.getDefault().getLanguage())) {
            pagesNeededToUpd.add(movies.elementAt(position).page);
            setItemInDownload(holder);
            return;
        }

        setNormalItem(holder);

        holder.localizedTitle.setText(movies.elementAt(position).localizedTitle);

        String str = baseURL + "w500" + movies.elementAt(position).posterPath;
        holder.mSimpleDraweeView.setImageURI(Uri.parse(str));

        holder.overviewText.setText(movies.elementAt(position).overviewText);

        Log.d(LOG_TAG, str);

        if (movies.size() - position <= MIN_NUM_REST_FILMS) {
            toDownload = true;
        }
    }

    @Override
    public int getItemCount() {
        return movies.size() + 1;
    }

    public void addItemToEnd(Movie element) {
        movies.addElement(element);
        notifyItemChanged(movies.size() - 1);
        notifyItemInserted(movies.size());
    }
}