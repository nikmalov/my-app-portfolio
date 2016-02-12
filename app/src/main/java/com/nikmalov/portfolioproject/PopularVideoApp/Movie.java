package com.nikmalov.portfolioproject.PopularVideoApp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


public class Movie implements Parcelable {

    public static final String MOVIE = "movie";
    public static final String MOVIE_ID = "id";
    public static final String TITLE = "original_title";
    public static final String OVERVIEW = "overview";
    public static final String DURATION = "runtime";
    public static final String POSTER_PATH = "poster_path";
    public static final String USER_RATING = "vote_average";
    public static final String RELEASE_DATE = "release_date";

    private int movieId;
    private String title;
    private double userRating;
    private int duration;
    private String overview;
    private String posterPath;
    private Date releaseDate;
    private Bitmap poster;

    public Movie(int movieId, String title, double userRating, String overview, String posterPath,
                 Date releaseDate)
    {
        this.movieId = movieId;
        this.title = title;
        this.userRating = userRating;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public String getPosterPath() {
        return posterPath;
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

    /**
        Use bitmap-free copy of this object to pass it through as Parcelable.
     */
    public static Movie getCopyWithoutPoster(Movie movieToCopy) {
        return new Movie(movieToCopy.movieId, movieToCopy.title, movieToCopy.userRating,
                movieToCopy.overview, movieToCopy.posterPath, movieToCopy.releaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(title);
        dest.writeDouble(userRating);
        dest.writeInt(duration);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeLong(releaseDate.getTime());
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            int id = source.readInt();
            String title = source.readString();
            double rating = source.readDouble();
            int duration = source.readInt();
            String overview = source.readString();
            String posterPath = source.readString();
            Date releaseDate = new Date(source.readLong());
            Movie newInstance = new Movie(id, title, rating, overview, posterPath, releaseDate);
            newInstance.setDuration(duration);
            return newInstance;
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}