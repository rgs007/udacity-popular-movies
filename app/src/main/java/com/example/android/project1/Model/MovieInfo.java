package com.example.android.project1.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.project1.database.MoviesContract;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by rgarc on 12/02/2017.
 */
public class MovieInfo implements Parcelable {
    public int id;
    public String title;
    @SerializedName("poster_path")
    public
    String imageURL;
    @SerializedName("overview")
    public
    String description;
    @SerializedName("backdrop_path")
    public
    String backDropUrl;
    @SerializedName("vote_average")
    public
    double userRating;
    @SerializedName("release_date")
    public
    Date releaseDate;

    public MovieInfo(int id, String title, String imageURL, String description, String backDropUrl, double userRating, Date releaseDate) {
        this.id = id;
        this.title = title;
        this.imageURL = imageURL;
        this.description = description;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.backDropUrl = backDropUrl;
    }

    public static class MovieResult {
        private List<MovieInfo> results;

        public List<MovieInfo> getResults() {
            return results;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.imageURL);
        dest.writeString(this.description);
        dest.writeDouble(this.userRating);
        dest.writeString(this.backDropUrl);
        dest.writeLong(this.releaseDate != null ? this.releaseDate.getTime() : -1);
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MoviesEntry.ID, String.valueOf(this.id));
        values.put(MoviesContract.MoviesEntry.TITLE, this.title);
        values.put(MoviesContract.MoviesEntry.DESCRIPTION, this.description);
        values.put(MoviesContract.MoviesEntry.IMAGEURL, this.imageURL);
        values.put(MoviesContract.MoviesEntry.RELEASEDATE, String.valueOf(this.releaseDate.getTime()));
        values.put(MoviesContract.MoviesEntry.USERRATING, String.valueOf(this.userRating));
        values.put(MoviesContract.MoviesEntry.BACKDROPURL, this.backDropUrl);
        return values;
    }

    protected MovieInfo(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.imageURL = in.readString();
        this.description = in.readString();
        this.userRating = in.readDouble();
        this.backDropUrl = in.readString();
        long tmpReleaseDate = in.readLong();
        this.releaseDate = tmpReleaseDate == -1 ? null : new Date(tmpReleaseDate);
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel source) {
            return new MovieInfo(source);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}