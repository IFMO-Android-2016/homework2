package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.model.Movie;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder>{

    private List<Movie> data = Collections.emptyList();
    private final Context context;
    private final LayoutInflater layoutInflater;
    public int curr;

    MoviesRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MoviesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MoviesRecyclerAdapter.ViewHolder holder, int position) {
        Movie m = data.get(position);
        holder.titleView.setText(m.localizedTitle);
        holder.overviewView.setText(m.overviewText);
        holder.voteView.setText(String.valueOf(m.averageVote));
        if (m.averageVote >= 7.0) {
            holder.voteView.setTextColor(context.getResources().getColor(R.color.colorVoteGreen));
        } else if (m.averageVote >= 4.0) {
            holder.voteView.setTextColor(context.getResources().getColor(R.color.colorVoteOrange));
        } else {
            holder.voteView.setTextColor(context.getResources().getColor(R.color.colorVoteRed));
        }
        holder.imageView.setImageURI(m.posterPath);
        curr = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void resetData(List<Movie> movies) {
        this.data = movies;
        notifyDataSetChanged();
    }


    public void pushData(List<Movie> movies) {
        if (this.data.isEmpty()) resetData(movies);
        else this.data.addAll(movies);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView titleView;
        final TextView overviewView;
        final TextView voteView;
        final SimpleDraweeView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView)itemView.findViewById(R.id.movie_title);
            overviewView = (TextView)itemView.findViewById(R.id.movie_overview);
            voteView = (TextView)itemView.findViewById(R.id.movie_vote);
            imageView = (SimpleDraweeView)itemView.findViewById(R.id.movie_image);
        }
        static ViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.popular_movie, parent, false);
            return new ViewHolder(view);
        }
    }
}
