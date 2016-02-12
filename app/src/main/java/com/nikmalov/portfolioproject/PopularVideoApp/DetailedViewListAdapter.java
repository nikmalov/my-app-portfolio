package com.nikmalov.portfolioproject.PopularVideoApp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.nikmalov.portfolioproject.R;

import java.util.ArrayList;
import java.util.List;

public class DetailedViewListAdapter extends BaseAdapter {

    private static final int MOVIE_DETAIL_VIEW_TYPE = 0;
    private static final int MOVIE_TRAILERS_VIEW_TYPE = 1;
    private static final int MOVIE_REVIEWS_VIEW_TYPE = 2;

    Movie movie;
    List<String[]> trailersList = new ArrayList<>();
    List<String[]> reviews = new ArrayList<>();
    Context mContext;
    LayoutInflater mInflater;

    public DetailedViewListAdapter(Context context, Movie movie, List<String[]> trailers,
                                   List<String[]> reviews)
    {
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.movie = movie;
        trailersList = trailers;
        this.reviews = reviews;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setTrailersList(List<String[]> trailersList) {
        this.trailersList = trailersList;
    }

    public void setReviews(List<String[]> reviews) {
        this.reviews = reviews;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MOVIE_DETAIL_VIEW_TYPE;
        } else if (position > 0 && position <= trailersList.size()) {
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
        return 1 + trailersList.size() + reviews.size();
    }

    @Override
    public Object getItem(int position) {
        switch (getItemViewType(position)) {
            case MOVIE_DETAIL_VIEW_TYPE: {
                return movie;
            }
            case MOVIE_TRAILERS_VIEW_TYPE: {
                return trailersList.get(position - 1);
            }
            case MOVIE_REVIEWS_VIEW_TYPE: {
                return reviews.get(position - trailersList.size() - 1);
            }
            default:
                throw new IndexOutOfBoundsException("There are no " + position + " details item.");
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
//        view = convertView;
        switch (getItemViewType(position)) {
            case MOVIE_DETAIL_VIEW_TYPE: {
//                if (view == null)
                view = mInflater.inflate(R.layout.detailed_view_upper_list_item, parent, false);
                ((ImageView)view.findViewById(R.id.detailed_view_poster_image)).
                        setImageBitmap(movie.getPoster());
                ((TextView)view.findViewById(R.id.release_date)).
                        setText(Utilities.formatDate(movie.getReleaseDate()));
                ((TextView)view.findViewById(R.id.duration)).setText(mContext.getResources().
                        getString(R.string.duration_format, movie.getDuration()));
                ((TextView)view.findViewById(R.id.rating)).
                        setText(Utilities.formatRating(movie.getUserRating()));
                ((TextView)view.findViewById(R.id.overview)).setText(movie.getOverview());
                return view;
            }
            case MOVIE_TRAILERS_VIEW_TYPE: {
                view = mInflater.inflate(R.layout.detailed_view_trailer_list_item, parent, false);
                String[] trailer = ((String[])getItem(position));
                ((TextView)view.findViewById(R.id.trailerName)).setText(trailer[0]);
                view.setOnClickListener(
                        new TrailerOnClickListener(Utilities.getYouTubeLink(trailer[1])));
                return view;
            }
            case MOVIE_REVIEWS_VIEW_TYPE: {
                String[] review = (String[])getItem(position);
                view = mInflater.inflate(R.layout.detailed_view_review_list_item, parent, false);
                ((TextView)view.findViewById(R.id.review_author)).setText(review[0]);
                ((TextView)view.findViewById(R.id.review_text)).setText(review[1]);
                return view;
            }
        }
        return null;
    }

    class TrailerOnClickListener implements View.OnClickListener {

        private Uri trailerUri;

        public TrailerOnClickListener(Uri uri) {
            trailerUri = uri;
        }

        @Override
        public void onClick(View v) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, trailerUri));
        }
    }
}
