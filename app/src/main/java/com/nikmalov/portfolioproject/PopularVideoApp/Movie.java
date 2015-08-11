package com.nikmalov.portfolioproject.PopularVideoApp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


public class Movie implements Parcelable {

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
    private Date releaseDate;
    private Bitmap poster;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        poster.writeToParcel(dest, 0);
        dest.writeInt(movieId);
        dest.writeString(title);
        dest.writeString(thumbnailsPath);
        dest.writeDouble(userRating);
        dest.writeString(overview);
        dest.writeLong(releaseDate.getTime());
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            Bitmap poster = Bitmap.CREATOR.createFromParcel(source);
            Movie newInstance =
                    new Movie(source.readInt(), source.readString(), source.readString(),
                            source.readDouble(), source.readString(), new Date(source.readLong()));
            newInstance.setPoster(poster);
            return newInstance;
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}