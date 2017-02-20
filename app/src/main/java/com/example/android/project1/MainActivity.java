package com.example.android.project1;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.project1.Model.SortType;
import com.example.android.project1.Utils.MovieDBService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL_THEMOVIEDB = "http://api.themoviedb.org/3/";
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private MyRecyclerViewAdapter myAdapter;
    private SortType sortType = SortType.POPULARITY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView= (RecyclerView) findViewById(R.id.rv_main);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new MyRecyclerViewAdapter();
        recyclerView.setAdapter(myAdapter);
        getMovies();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_order_popularity:
                item.setChecked(true);
                sortType = SortType.POPULARITY;
                getMovies();
                return true;
            case R.id.action_order_top_rated:
                item.setChecked(true);
                sortType = SortType.TOP_RATED;
                getMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getMovies() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_THEMOVIEDB)
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                .build();

        MovieDBService movieDBService = retrofit.create(MovieDBService.class);

        final Call<MovieInfo.MovieResult> call;

        call = movieDBService.getMovies(sortType, 1, BuildConfig.API_KEY);

        call.enqueue(new Callback<MovieInfo.MovieResult>() {
            @Override
            public void onResponse(Call<MovieInfo.MovieResult> call, Response<MovieInfo.MovieResult> response) {
                if (response.isSuccessful()) {
                    myAdapter.setMovieList(response.body());

                }
            }
            @Override
            public void onFailure(Call<MovieInfo.MovieResult> call, Throwable t) {

            }
        });
    }

}
