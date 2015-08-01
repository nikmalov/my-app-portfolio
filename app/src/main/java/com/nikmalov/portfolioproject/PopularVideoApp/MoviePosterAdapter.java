package com.nikmalov.portfolioproject.PopularVideoApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviePosterAdapter extends BaseAdapter {

    Context mContext;
    List<Movie> movieList;
    private int defaultNumberOfPosters = 15;
    private String testUrl = "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";

    public MoviePosterAdapter(Context context, List<Movie> movieList) {
        mContext = context;
        this.movieList = movieList;
    }

    @Override
    public int getCount() {
        return defaultNumberOfPosters;
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
        Picasso.with(mContext).load(testUrl).into(imageView);//for testing
        //imageView.setImageBitmap(movieList.get(position).getPoster());
        return imageView;
    }

}