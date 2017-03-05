package com.example.android.project1;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.project1.Database.MoviesContract;
import com.example.android.project1.Database.MoviesDBHelper;
import com.example.android.project1.Model.MovieInfo;
import com.example.android.project1.Model.SortType;
import com.example.android.project1.Utils.MovieDBService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
    private MoviesDBHelper dbHelper;

    private class MoviesLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return dbHelper.getAllFavoriteMovies();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            List<MovieInfo> movieInfoResults = new ArrayList<>();
            if (cursor != null && cursor.getCount() > 0) {
                while(cursor.moveToNext()){
                    int id = cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.ID));
                    String title = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.DESCRIPTION));
                    String imageUrl = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.IMAGEURL));
                    String backdropUrl = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.BACKDROPURL));
                    long releaseDate = cursor.getLong(cursor.getColumnIndex(MoviesContract.MoviesEntry.RELEASEDATE));
                    double userRating = cursor.getDouble(cursor.getColumnIndex(MoviesContract.MoviesEntry.USERRATING));
                    // Acciones...
                    MovieInfo movieInfo=new MovieInfo(id,title,imageUrl,description,backdropUrl,userRating,new Date(releaseDate));
                    movieInfoResults.add(movieInfo);
                }

            } else {
                // Mostrar empty state
            }
            myAdapter.setMovieList(movieInfoResults);
        }
    }

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
        dbHelper = new MoviesDBHelper(this);
        getMovies();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("sortType", sortType);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sortType=(SortType) savedInstanceState.get("sortType");
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
            case R.id.action_order_favorites:
                item.setChecked(true);
                getFavoriteMovies();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private  void getFavoriteMovies() {
        new MoviesLoadTask().execute();
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
                    myAdapter.setMovieList(response.body().getResults());
                }
            }
            @Override
            public void onFailure(Call<MovieInfo.MovieResult> call, Throwable t) {

            }
        });
    }

}
