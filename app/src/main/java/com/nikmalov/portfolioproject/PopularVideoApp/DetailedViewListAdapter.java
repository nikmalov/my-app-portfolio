package com.nikmalov.portfolioproject.PopularVideoApp;

import android.net.Uri;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailedViewListAdapter extends BaseAdapter {

    private static final int MOVIE_DETAIL_VIEW_TYPE = 0;
    private static final int MOVIE_TRAILERS_VIEW_TYPE = 1;
    private static final int MOVIE_REVIEWS_VIEW_TYPE = 2;

    List<Uri> trailersUriList = new ArrayList<>();
    Map<String, String> reviews = new ArrayMap<>();

    public DetailedViewListAdapter(Movie movie) {
        loadAdditionalMovieData(movie.getMovieId());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MOVIE_DETAIL_VIEW_TYPE;
        } else if (position > 0 && position <= trailersUriList.size()) {
            return MOVIE_TRAILERS_VIEW_TYPE;
        } else {
            return MOVIE_REVIEWS_VIEW_TYPE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return 0;
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
        return null;
    }

    private void loadAdditionalMovieData(int movieId) {
        //TODO: fill trailersUriList and reviews

    }
}
