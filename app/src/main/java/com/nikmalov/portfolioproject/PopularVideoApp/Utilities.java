package com.nikmalov.portfolioproject.PopularVideoApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_MOVIE_ID;
import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_MOVIE_TITLE;
import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_OVERVIEW;
import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_POSTER_PATH;
import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_RATING;
import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_RELEASE_DATE;

public class Utilities {
    public static final String URL_REQUEST_ANCHOR = "http://api.themoviedb.org/3/movie";
    public static final String ID = "/id/";
    public static final String IMAGE_URL_ANCHOR = "http://image.tmdb.org/t/p/";
    public static final String API_KEY = "?api_key=" + "41320596822de654b82ddb5d040d4127";
    public static final String GRID_IMG_QUALITY = "w500/";
    public static final String DETAILED_IMG_QUALITY = "w342/";
    public static final String VIDEOS_URI_PART = "/videos";
    public static final String REVIEWS_URI_PART = "/reviews";


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

    public static Movie createMovie(Context context, Cursor cur) throws IOException {
        Movie movie = new Movie(cur.getInt(cur.getColumnIndex(COLUMN_MOVIE_ID)),
                cur.getString(cur.getColumnIndex(COLUMN_MOVIE_TITLE)),
                cur.getDouble(cur.getColumnIndex(COLUMN_RATING)),
                cur.getString(cur.getColumnIndex(COLUMN_OVERVIEW)),
                cur.getString(cur.getColumnIndex(COLUMN_POSTER_PATH)),
                new Date(cur.getLong(cur.getColumnIndex(COLUMN_RELEASE_DATE))));
        movie.setPoster(Picasso.with(context).
                load(IMAGE_URL_ANCHOR + GRID_IMG_QUALITY + movie.getPosterPath()).get());
        return movie;
    }

}
