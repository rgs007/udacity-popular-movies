package com.example.android.project1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by rgarc on 12/02/2017.
 */
public class MovieInfo implements Parcelable {
    String title;
    @SerializedName("poster_path")
    String imageURL;
    @SerializedName("overview")
    String description;
    @SerializedName("backdrop_path")
    String backDropUrl;
    @SerializedName("vote_average")
    double userRating;
    @SerializedName("release_date")
    Date releaseDate;

    public MovieInfo(String title, String imageURL, String description, String backDropUrl, double userRating, Date releaseDate) {
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
        dest.writeString(this.title);
        dest.writeString(this.imageURL);
        dest.writeString(this.description);
        dest.writeDouble(this.userRating);
        dest.writeString(this.backDropUrl);
        dest.writeLong(this.releaseDate != null ? this.releaseDate.getTime() : -1);
    }

    protected MovieInfo(Parcel in) {
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