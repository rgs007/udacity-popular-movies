package com.example.android.project1.model;

import java.io.Serializable;

/**
 * Created by rgarc on 15/02/2017.
 */

public enum SortType implements Serializable {

        FAVORITES("favorites"),
        POPULARITY("popular"),
        TOP_RATED("top_rated");

        private final String value;

        SortType    (String value) {
            this.value = value;
        }

        @Override public String toString() {
            return value;
        }
    }

