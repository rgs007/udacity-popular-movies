package com.example.android.project1;

import android.content.ContentResolver;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.project1.database.MoviesContract;
import com.example.android.project1.database.MoviesDBHelper;
import com.example.android.project1.model.MovieInfo;
import com.example.android.project1.model.SortType;
import com.example.android.project1.utils.MovieDBService;

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

import static java.sql.Types.NULL;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL_THEMOVIEDB = "http://api.themoviedb.org/3/";
    private static final String LOG_TAG = "MainActivity";
    private static final String SAVED_LAYOUT_MANAGER = "layout-manager-state";

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private MyRecyclerViewAdapter myAdapter;
    private SortType sortType = SortType.POPULARITY;
    private MoviesDBHelper dbHelper;
    private Parcelable mLayoutManagerSavedState = null;

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

        int galleryColumns = getResources().getInteger(R.integer.gallery_columns);
        layoutManager = new GridLayoutManager(this, galleryColumns);

        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new MyRecyclerViewAdapter();
        recyclerView.setAdapter(myAdapter);

        dbHelper = new MoviesDBHelper(this);
        getMovies();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putSerializable("sortType", sortType);
        outState.putParcelable(SAVED_LAYOUT_MANAGER, layoutManager.onSaveInstanceState());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sortType=(SortType) savedInstanceState.get("sortType");
        getMovies();
        mLayoutManagerSavedState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
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
                sortType = SortType.POPULARITY;
                break;
            case R.id.action_order_top_rated:
                sortType = SortType.TOP_RATED;
                break;
            case R.id.action_order_favorites:
                sortType = SortType.FAVORITES;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        item.setChecked(true);
        getMovies();
        return true;
    }
    private  void getFavoriteMovies() {
        new GetFavoritesMoviesTask().execute();
    }

    public class GetFavoritesMoviesTask extends AsyncTask<Void, Void, ArrayList<MovieInfo>> {

        @Override
        protected ArrayList<MovieInfo> doInBackground(Void... params) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(MoviesContract.CONTENT_URI,
                    null, null, null, null);
            ArrayList<MovieInfo> favoritesMovies = new ArrayList<MovieInfo>();
            if(cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.ID));
                    String title = cursor.getString(
                            cursor.getColumnIndex(MoviesContract.MoviesEntry.TITLE));
                    String imageUrl = cursor.getString(
                            cursor.getColumnIndex(MoviesContract.MoviesEntry.IMAGEURL));
                    String description = cursor.getString(
                            cursor.getColumnIndex(MoviesContract.MoviesEntry.DESCRIPTION));
                    String backdropUrl = cursor.getString(
                            cursor.getColumnIndex(MoviesContract.MoviesEntry.BACKDROPURL));
                    double userRating = cursor.getDouble(
                            cursor.getColumnIndex(MoviesContract.MoviesEntry.USERRATING));
                    long releaseDate = cursor.getLong(
                            cursor.getColumnIndex(MoviesContract.MoviesEntry.RELEASEDATE));
                    MovieInfo movie = new MovieInfo(id, title, imageUrl, description, backdropUrl, userRating, new Date(releaseDate));
                    favoritesMovies.add(movie);
                } while (cursor.moveToNext());
                restorePosition();
            }

            if(cursor != null) {
                cursor.close();
            }
            return favoritesMovies;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieInfo> movies) {
            myAdapter.setMovieList(movies);

        }
    }
    /**
     * Restores scroll position after configuration change.
     * <p>
     * <b>NOTE:</b> Must be called after adapter has been set.
     */
    private void restorePosition() {
        if (mLayoutManagerSavedState != null) {
            layoutManager.onRestoreInstanceState(mLayoutManagerSavedState);
            mLayoutManagerSavedState = null;
        }
    }

    private void getMovies()
    {
        switch(sortType)
        {
            case FAVORITES:
                getFavoriteMovies();
            default:
                getPopularMovies();
        }

    }

    private void getPopularMovies() {

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
                    restorePosition();
                }

            }
            @Override
            public void onFailure(Call<MovieInfo.MovieResult> call, Throwable t) {
                Log.e(LOG_TAG,"Error: ", t);
            }
        });
    }

}
