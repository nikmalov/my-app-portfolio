package com.nikmalov.portfolioproject.PopularVideoApp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.*;

public class FavouriteMoviesProvider extends ContentProvider {

    private final UriMatcher uriMatcher = buildUriMatcher();
    private FavouriteMoviesDBHelper dbHelper;
    private static final SQLiteQueryBuilder moviesQueryBuilder = new SQLiteQueryBuilder();
    static {
        moviesQueryBuilder.setTables(FavouriteMovieEntry.TABLE_NAME);
    }

    private static final String movieByIdSelection =
            FavouriteMovieEntry.TABLE_NAME + "." + FavouriteMovieEntry.COLUMN_MOVIE_ID + " = ? ";

    static final int FAVOURITE_MOVIES = 100;
    static final int FAVOURITE_MOVIE_BY_ID = 101;

    @Override
    public boolean onCreate() {
        dbHelper = new FavouriteMoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            case FAVOURITE_MOVIES:
                retCursor = getFavouriteMovies(projection, sortOrder);
                break;
            case FAVOURITE_MOVIE_BY_ID:
                retCursor = getFavouriteMovieById(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case FAVOURITE_MOVIES:
                return FavouriteMovieEntry.CONTENT_TYPE;
            case FAVOURITE_MOVIE_BY_ID:
                return FavouriteMovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException(
                        "Cannot provide type for uri " + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case FAVOURITE_MOVIES: {
                long _id = db.insert(FavouriteMovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FavouriteMovieEntry.buildSingleMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result;
        switch (uriMatcher.match(uri)) {
            case FAVOURITE_MOVIE_BY_ID:
                result = db.delete(FavouriteMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (result != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result;
        switch (uriMatcher.match(uri)) {
            case FAVOURITE_MOVIE_BY_ID:
                result = db.update(FavouriteMovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (result != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    private Cursor getFavouriteMovieById(Uri uri, String[] projection, String sortOrder) {
        String[] selection_args = {FavouriteMovieEntry.getMovieIdStringFromUri(uri)};
        return moviesQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection, movieByIdSelection, selection_args, null, null, sortOrder);
    }

    private Cursor getFavouriteMovies(String[] projection, String sortOrder) {
        return moviesQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection, null, null, null, null, sortOrder);
    }

    static UriMatcher buildUriMatcher() {
        UriMatcher uriMather = new UriMatcher(UriMatcher.NO_MATCH);
        uriMather.addURI(CONTENT_AUTHORITY, PATH_FAVOURITE_MOVIES, FAVOURITE_MOVIES);
        uriMather.addURI(CONTENT_AUTHORITY, PATH_FAVOURITE_MOVIES + "/#", FAVOURITE_MOVIE_BY_ID);
        return uriMather;
    }
}
