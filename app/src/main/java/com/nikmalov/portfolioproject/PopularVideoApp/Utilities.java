package com.nikmalov.portfolioproject.PopularVideoApp;

import android.annotation.SuppressLint;
import android.net.Uri;

import com.nikmalov.portfolioproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}
