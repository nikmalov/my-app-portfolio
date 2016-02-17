package com.nikmalov.portfolioproject.PopularVideoApp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry;
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
//        view = convertView; TODO: implement Holder pattern
        switch (getItemViewType(position)) {
            case MOVIE_DETAIL_VIEW_TYPE: {
//                if (view == null)
                view = mInflater.inflate(R.layout.detailed_view_description_list_item, parent, false);
                ((ImageView)view.findViewById(R.id.detailed_view_poster_image)).
                        setImageBitmap(movie.getPoster());
                ToggleButton addToFavouritesButton =
                        ((ToggleButton)view.findViewById(R.id.addToFavouriteButton));
                addToFavouritesButton.setChecked(isInFavourites(movie));
                addToFavouritesButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            addToFavourites(movie);
                        } else {
                            removeFromFavourites(movie);
                        }
                    }
                });
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
                        new OpenUrlOnClickListener(Utilities.getYouTubeLink(trailer[1])));
                return view;
            }
            case MOVIE_REVIEWS_VIEW_TYPE: {
                String[] review = (String[])getItem(position);
                view = mInflater.inflate(R.layout.detailed_view_review_list_item, parent, false);
                ((TextView)view.findViewById(R.id.review_author)).setText(review[0]);
                ((TextView)view.findViewById(R.id.review_text)).setText(review[1]);
                view.setOnClickListener(new OpenUrlOnClickListener(Uri.parse(review[2])));
                return view;
            }
        }
        return null;
    }

    private void addToFavourites(Movie movie) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(FavouriteMovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
        movieValues.put(FavouriteMovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        movieValues.put(FavouriteMovieEntry.COLUMN_RATING, movie.getUserRating());
        movieValues.put(FavouriteMovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate().getTime());
        movieValues.put(FavouriteMovieEntry.COLUMN_DURATION, movie.getDuration());
        movieValues.put(FavouriteMovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        movieValues.put(FavouriteMovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        mContext.getContentResolver().insert(FavouriteMovieEntry.CONTENT_URI, movieValues);
    }

    private void removeFromFavourites(Movie movie) {
        mContext.getContentResolver().delete(
                FavouriteMovieEntry.buildSingleMovieUri(movie.getMovieId()), null, null);
    }

    private boolean isInFavourites(Movie movie) {
        boolean isInFavourites = false;
        Cursor cursor = mContext.getContentResolver().query(
                FavouriteMovieEntry.buildSingleMovieUri(movie.getMovieId()),
                new String[]{FavouriteMovieEntry._ID},
                null, null, null);
        if (cursor != null && cursor.moveToFirst())
            isInFavourites = true;
        if (cursor != null)
            cursor.close();
        return isInFavourites;
    }

    class OpenUrlOnClickListener implements View.OnClickListener {

        private Uri uriToOpen;

        public OpenUrlOnClickListener(Uri uri) {
            uriToOpen = uri;
        }

        @Override
        public void onClick(View v) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, uriToOpen));
        }
    }
}
