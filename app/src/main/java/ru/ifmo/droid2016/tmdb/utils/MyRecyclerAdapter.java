package ru.ifmo.droid2016.tmdb.utils;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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

    protected final int MIN_NUM_REST_FILMS = 15;
    protected final String baseURL = "https://image.tmdb.org/t/p/";

    protected boolean isInternetAvailable = true;

    protected Set<Integer> pagesNeededToUpd = new HashSet<>();
    public Set<Integer> pagesWithError = new HashSet<>();

    protected Vector<Movie> movies = new Vector<>();

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

            overviewText = (TextView) v.findViewById(R.id.overview_text);
        }
    }

    public MyRecyclerAdapter() {
        movies = new Vector<>();
    }

    public void init(Vector<Movie> movies, Set<Integer> pagesWithError) {
        this.movies = movies;
        this.pagesWithError = pagesWithError;
        if (movies == null) {
            this.movies = new Vector<>();
        }
        if (pagesWithError == null) {
            this.pagesWithError = new HashSet<>();
        }
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

    protected void setErrorItem(MyRecyclerAdapter.ViewHolder holder) {
        holder.progressBar.setVisibility(View.INVISIBLE);
        holder.mSimpleDraweeView.setVisibility(View.INVISIBLE);
        holder.localizedTitle.setVisibility(View.INVISIBLE);
        holder.overviewText.setVisibility(View.INVISIBLE);

        holder.imageErrorView.setVisibility(View.VISIBLE);

        holder.mSimpleDraweeView.getLayoutParams().width = 100;
        holder.mSimpleDraweeView.getLayoutParams().height = 100;
        holder.mSimpleDraweeView.requestLayout();
    }

    @Override
    public void onBindViewHolder(MyRecyclerAdapter.ViewHolder holder, int position) {
        holder.imageErrorView.setVisibility(View.INVISIBLE);

        if (position == movies.size()) {
            if (isInternetAvailable) {
                setItemInDownload(holder);
            } else {
                setErrorItem(holder);
            }
            return;
        }

        if (pagesWithError.contains(movies.elementAt(position).page)) {
            setErrorItem(holder);
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

        if (movies.size() - position <= MIN_NUM_REST_FILMS) {
            if (movies.size() > 0) {
                pagesNeededToUpd.add(movies.elementAt(movies.size() - 1).page + 1);
            }
        }
    }

    public void updInternetState(boolean state) {
        isInternetAvailable = state;
        notifyItemChanged(movies.size());
    }

    public void addPageWithError(int page) {
        pagesNeededToUpd.remove(page);
        pagesWithError.add(page);
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