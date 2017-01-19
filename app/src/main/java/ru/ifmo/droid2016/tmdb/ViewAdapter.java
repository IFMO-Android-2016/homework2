package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Koroleva Yana.
 */

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<Movie> movies;

    public void updateMoviesList(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public ViewAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie m = movies.get(position);
        holder.overview.setText(m.overviewText);
        holder.originalTitle.setText(m.originalTitle);
        holder.title.setText(m.localizedTitle);
        holder.simpleDraweeView.setImageURI(m.posterPath);
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView simpleDraweeView;
        TextView title, originalTitle, overview;
        public ViewHolder(View view) {
            super(view);
            simpleDraweeView = (SimpleDraweeView)view.findViewById(R.id.poster);
            title = (TextView)view.findViewById(R.id.title);
            originalTitle = (TextView)view.findViewById(R.id.originalTitle);
            overview = (TextView)view.findViewById(R.id.overview);
        }

        public static ViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            View view = layoutInflater.inflate(R.layout.item, viewGroup, false);
            return new ViewHolder(view);
        }
    }
}
