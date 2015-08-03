package com.nikmalov.portfolioproject.PopularVideoApp;

import android.graphics.Bitmap;

import java.util.Date;


public class Movie {

    public static final String MOVIE_ID = "id";
    public static final String TITLE = "original_title";
    public static final String OVERVIEW = "overview";
    public static final String POSTER_PATH = "poster_path";
    public static final String THUMBNAILS_PATH = "backdrop_path";
    public static final String USER_RATING = "vote_average";
    public static final String RELEASE_DATE = "release_date";

    private int movieId;
    private String title;
    private String thumbnailsPath;
    private double userRating;
    private String overview;
    private Bitmap poster;
    private Date releaseDate;

    public Movie(int movieId, String title, String thumbnailsPath, double userRating,
                 String overview, Date releaseDate)
    {
        this.movieId = movieId;
        this.title = title;
        this.thumbnailsPath = thumbnailsPath;
        this.userRating = userRating;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public void setPoster(Bitmap poster) {
        this.poster = poster;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getThumbnailsPath() {
        return thumbnailsPath;
    }

    public double getUserRating() {
        return userRating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Movie && movieId == ((Movie) o).getMovieId();
    }
}