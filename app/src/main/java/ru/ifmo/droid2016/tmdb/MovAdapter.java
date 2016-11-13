package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

public class MovAdapter extends RecyclerView.Adapter<MovAdapter.ViewHolder>{
    private final Context context;
    private final LayoutInflater layoutInflater;

    @NonNull
    private List<Movie> data = new ArrayList<>();

    public MovAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void updateData(List<Movie> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie m = data.get(position);
        holder.description.setText(m.overviewText);
        holder.imageView.setImageURI(m.posterPath);
        holder.titleView.setText(m.localizedTitle);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView titleView, description;

        private ViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_pic);
            titleView = (TextView) itemView.findViewById(R.id.movie_title);
            description = (TextView) itemView.findViewById(R.id.movie_description);
        }

        static ViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.movie_item, parent, false);
            return new ViewHolder(view);
        }
    }
}
