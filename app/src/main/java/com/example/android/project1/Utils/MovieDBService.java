package com.example.android.project1.Utils;

import com.example.android.project1.BuildConfig;
import com.example.android.project1.Model.SortType;
import com.example.android.project1.MovieInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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
}


