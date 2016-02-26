package com.nikmalov.portfolioproject.PopularVideoApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.nikmalov.portfolioproject.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_DURATION;
import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_MOVIE_ID;
import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_MOVIE_TITLE;
import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_OVERVIEW;
import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_POSTER;
import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_POSTER_PATH;
import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_RATING;
import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_RELEASE_DATE;

public class Utilities {
    public static final String URL_REQUEST_ANCHOR = "http://api.themoviedb.org/3/movie";
    public static final String ID = "/id/";
    public static final String IMAGE_URL_ANCHOR = "http://image.tmdb.org/t/p/";
    public static final String API_KEY = "?api_key=" + "41320596822de654b82ddb5d040d4127";
    public static final String GRID_IMG_QUALITY = "w500/";
    public static final String VIDEOS_URI_PART = "/videos";
    public static final String REVIEWS_URI_PART = "/reviews";
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String formatRating(double rating) {
        return String.format("%.1f/10", rating);
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy").format(date);
    }

    public static Uri getYouTubeLink(String videoId) {
        return Uri.parse("http://www.youtube.com/watch?v=" + videoId);
    }

    public static Movie createMovie(Cursor cur) {
        Movie movie = new Movie(cur.getInt(cur.getColumnIndex(COLUMN_MOVIE_ID)),
                cur.getString(cur.getColumnIndex(COLUMN_MOVIE_TITLE)),
                cur.getDouble(cur.getColumnIndex(COLUMN_RATING)),
                cur.getString(cur.getColumnIndex(COLUMN_OVERVIEW)),
                cur.getString(cur.getColumnIndex(COLUMN_POSTER_PATH)),
                new Date(cur.getLong(cur.getColumnIndex(COLUMN_RELEASE_DATE))));
        movie.setDuration(cur.getInt(cur.getColumnIndex(COLUMN_DURATION)));
        byte[] posterByteArray = cur.getBlob(cur.getColumnIndex(COLUMN_POSTER));
        movie.setPoster(BitmapFactory.decodeByteArray(posterByteArray, 0, posterByteArray.length));
        return movie;
    }

    /**
     * Note that created Movie object does not have Duration specified.
     */
    public static Movie createMovieOnline(Context context, JSONObject movieObject) throws
            JSONException
    {
        String posterPath = movieObject.getString(Movie.POSTER_PATH);
        Date releaseDate = dateFormat.
                parse(movieObject.getString(Movie.RELEASE_DATE), new ParsePosition(0));
        final Movie movie = new Movie(movieObject.getInt(Movie.MOVIE_ID),
                movieObject.getString(Movie.TITLE),
                movieObject.getDouble(Movie.USER_RATING),
                movieObject.getString(Movie.OVERVIEW),
                posterPath,
                releaseDate);
        try {
            movie.setPoster(Picasso.with(context).
                    load(IMAGE_URL_ANCHOR + GRID_IMG_QUALITY + posterPath).get());
        } catch (IOException e) {
            Log.e("Creating Movie object:", "Was not able to download and set poster.");
            e.printStackTrace();
        }
        return movie;
    }

    public static MovieListType getCurrentSortingType(Context context) {
        String sortType = PreferenceManager.getDefaultSharedPreferences(context).
                getString(context.getString(R.string.sorting_type_key),
                        context.getString((R.string.sorting_type_popularity)));
        if (sortType.equalsIgnoreCase(context.getString(R.string.sorting_type_top_rated))) {
            return MovieListType.TOP_RATED;
        } else if (sortType.equalsIgnoreCase(context.getString(R.string.sorting_type_popularity))) {
            return MovieListType.POPULAR;
        } else {
            return MovieListType.FAVOURITES;
        }
    }

}
