package com.example.android.project1.Database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rgarcia on 05/03/2017.
 */

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.popularmovies.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
            CONTENT_AUTHORITY + "/" + PATH_MOVIES;
    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

    public static Uri buildMovieUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static abstract class MoviesEntry implements BaseColumns {


        public static final String TABLE_NAME ="movies";

        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String IMAGEURL = "imageurl";
        public static final String DESCRIPTION = "description";
        public static final String USERRATING = "userrating";
        public static final String RELEASEDATE = "releasedate";
        public static final String BACKDROPURL = "backdropurl";

    }
}
