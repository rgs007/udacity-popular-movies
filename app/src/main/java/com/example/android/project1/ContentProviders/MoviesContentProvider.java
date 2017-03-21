package com.example.android.project1.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.android.project1.database.MoviesContract;
import com.example.android.project1.database.MoviesDBHelper;

public class MoviesContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper mOpenHelper;

    public static final int MOVIE = 100;
    public static final int MOVIE_ID = 101;


    private Cursor getFavoriteMovies(Uri uri) {
        SQLiteQueryBuilder favoriteMovieQueryBuilder = new SQLiteQueryBuilder();
        favoriteMovieQueryBuilder.setTables(MoviesContract.MoviesEntry.TABLE_NAME);

        return favoriteMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(), null,
                null, null, null, null, null);
    }

    private Cursor getFavoriteMovie(Uri uri) {
        SQLiteQueryBuilder favoriteMovieQueryBuilder = new SQLiteQueryBuilder();
        favoriteMovieQueryBuilder.setTables(MoviesContract.MoviesEntry.TABLE_NAME);
        String selectionMovie = MoviesContract.MoviesEntry.ID + " = ?";
        String _id = uri.getLastPathSegment();
        String[] selectionArgs = new String[]{_id};

        return favoriteMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(), null,
                selectionMovie, selectionArgs, null, null, null);
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MoviesContract.PATH_MOVIES, MOVIE);
        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_ID:
                return MoviesContract.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                returnCursor = getFavoriteMovies(uri);
                break;
            }
            case MOVIE_ID: {
                returnCursor = getFavoriteMovie(uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIE: {
                long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MoviesContract.buildMovieUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String idMovie = uri.getLastPathSegment();
        String[] whereArgs = new String[]{idMovie};
        int rowsDeleted;
        switch (match) {
            case MOVIE_ID: {
                rowsDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME,
                        MoviesContract.MoviesEntry.ID + "= ?", whereArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
