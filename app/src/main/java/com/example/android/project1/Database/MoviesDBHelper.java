package com.example.android.project1.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.project1.model.MovieInfo;

/**
 * Created by rgarcia on 05/03/2017.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movies.db";

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " ("
                + MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MoviesContract.MoviesEntry.ID + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.TITLE + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.IMAGEURL + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.DESCRIPTION + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.USERRATING + " TEXT,"
                + MoviesContract.MoviesEntry.RELEASEDATE + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.BACKDROPURL + " TEXT,"
                + "UNIQUE (" + MoviesContract.MoviesEntry.ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //The database is only on one version so there no need to update
    }
    public Cursor getAllFavoriteMovies() {
        return getReadableDatabase()
                .query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }
    public long InsertFavoriteMovie(MovieInfo movieInfo) {
        return getWritableDatabase()
                .insert(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                null,
                        movieInfo.toContentValues());
    }
    public long DeleteFavoriteMovie(MovieInfo movieInfo)
    {
        return getWritableDatabase()
                .delete(MoviesContract.MoviesEntry.TABLE_NAME,
                        MoviesContract.MoviesEntry.ID + " LIKE ?",
                        new String[]{String.valueOf(movieInfo.id) });
    }
    public boolean IsFavorite(int movieId)
    {
        Cursor c = getReadableDatabase().query(
                MoviesContract.MoviesEntry.TABLE_NAME,
                null,
                MoviesContract.MoviesEntry.ID + " LIKE ?",
                new String[]{String.valueOf(movieId)},
                null,
                null,
                null);
        return c.getCount()>0;

    }
}
