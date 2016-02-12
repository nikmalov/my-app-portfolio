package com.nikmalov.portfolioproject.PopularVideoApp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class MoviePosterAdapter extends BaseAdapter {

    Context mContext;
    List<Movie> movieList;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent movieDetailsIntent = new Intent(mContext, MovieDetailActivity.class);
                fillIntentWithMovieData(movieDetailsIntent, movieList.get(position));
                mContext.startActivity(movieDetailsIntent);
            }
        });
        imageView.setImageBitmap(movieList.get(position).getPoster());
        return imageView;
    }

    private void fillIntentWithMovieData(Intent intent, Movie movie) {
        intent.putExtra(Movie.MOVIE_ID, movie.getMovieId());
        intent.putExtra(Movie.TITLE, movie.getTitle());
        intent.putExtra(Movie.POSTER_PATH, movie.getPosterPath());
        intent.putExtra(Movie.OVERVIEW, movie.getOverview());
        intent.putExtra(Movie.USER_RATING, movie.getUserRating());
        intent.putExtra(Movie.RELEASE_DATE, movie.getReleaseDate().getTime());
    }

}