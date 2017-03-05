package com.example.android.project1.Database;

import android.provider.BaseColumns;

/**
 * Created by rgarcia on 05/03/2017.
 */

public class MoviesContract {
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
