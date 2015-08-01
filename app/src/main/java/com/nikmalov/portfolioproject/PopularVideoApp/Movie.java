package com.nikmalov.portfolioproject.PopularVideoApp;

import android.graphics.Bitmap;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Movie {

    public static final String MOVIE_ID = "id";
    public static final String TITLE = "title";
    public static final String GENRE_IDS = "genre_ids";
    public static final String IS_ADULT = "adult";
    public static final String POSTER_PATH = "poster_path";

    public static final String GENRE_ID = "id";
    public static final String GENRE_NAME = "name";

    private static final String genreListGetQuery = "http://api.themoviedb.org/3/genre/movie/list";
    private static final HashMap<Integer, String> idGenreMapping = new HashMap<>();

    private int movieId;
    private String title;
    private List<String> genres;
    private boolean isAdult;
    private Bitmap poster;

/*    static {
        try {
            URLConnection connection = new URL(genreListGetQuery).openConnection();
            JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
            try {
                reader.beginArray();
                while(reader.hasNext()) {
                    handleGenreEntry(reader);
                }
                reader.endArray();
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            Log.e("Movie", "Unable to parse movie genres.");
        }
    }
*/
    public Movie(String movieId, String title, String genreIds, String isAdult) {
        this.movieId = Integer.parseInt(movieId);
        this.title = title;
        genres = extractGenres(genreIds);
        this.isAdult = isAdult.equalsIgnoreCase("true");
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

    public List<String> getGenres() {
        return genres;
    }

    public boolean isAdult() {
        return isAdult;
    }

    private static List<String> extractGenres(String genreIds) {
        String[] splitedGenreIds = genreIds.split(",");
        List<String> genres = new ArrayList<>();
/*        for (String genreId : splitedGenreIds) {
            genres.add(idGenreMapping.get(Integer.parseInt(genreId)));
        }
        return genres;
*/      return genres;
    }

    private static void handleGenreEntry(JsonReader reader) throws IOException {
        int genreId = 0;
        String genreName = "";
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(GENRE_ID))
                genreId = reader.nextInt();
            if (name.equals(GENRE_NAME))
                genreName = reader.nextString();
        }
        reader.endObject();
        idGenreMapping.put(genreId, genreName);
    }
}