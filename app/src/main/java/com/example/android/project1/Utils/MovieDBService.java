package com.example.android.project1.Utils;

import com.example.android.project1.Model.ReviewInfo;
import com.example.android.project1.Model.SortType;
import com.example.android.project1.Model.MovieInfo;
import com.example.android.project1.Model.TrailerInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by rgarcia on 12/02/2017.
 */
public interface MovieDBService {

    @GET("/3/movie/{sort_by}") Call<MovieInfo.MovieResult> getMovies(
            @Path("sort_by") SortType sort,
            @Query("page") Integer page,
            @Query("api_key") String api_key);

    @GET("/3/movie/{id}/videos") Call<TrailerInfo.TrailerResult> getVideos(
            @Path("id") int id,
            @Query("api_key") String api_key);

    @GET("/3/movie/{id}/reviews") Call<ReviewInfo.ReviewResult> getReviews(
            @Path("id") int id,
            @Query("api_key") String api_key);

}


