package com.nikmalov.portfolioproject.PopularVideoApp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class MoviePosterAdapter extends BaseAdapter {

    Context mContext;
    List<Movie> movieList;
    private int defaultNumberOfPosters = 15;

    public MoviePosterAdapter(Context context, List<Movie> movieList) {
        mContext = context;
        this.movieList = movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(movieList.get(position).getPoster());
        return imageView;
    }

}