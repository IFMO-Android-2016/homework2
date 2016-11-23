package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Aleksandr Tukallo on 23.11.16.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final Context context;
    private final LayoutInflater layoutInflater;

    private List<Movie> movieArrayList = new ArrayList<>();

    public Adapter(Context con) {
        context = con;
        layoutInflater = LayoutInflater.from(context);
        movieArrayList = new ArrayList<>();
    }

    public void updateData(List<Movie> data) {
        this.movieArrayList = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movieArrayList.get(position);
        holder.description.setText(movie.overviewText);

//        String imagePath =  + movie.posterPath;
//        holder.imageView.setImageURI(Uri.parse(imagePath)); //TODO mb error
        holder.imageView.setImageURI("https://image.tmdb.org/t/p/w500" + movie.posterPath);

        holder.titleView.setText(movie.localizedTitle);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView imageView;
        TextView titleView, description;

        private ViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.image);
            titleView = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
        }

        static ViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.movie, parent, false);
            return new ViewHolder(view);
        }
    }
}
