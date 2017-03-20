package com.example.android.project1;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.project1.Database.MoviesContract;
import com.example.android.project1.Database.MoviesDBHelper;
import com.example.android.project1.Model.MovieInfo;
import com.example.android.project1.Model.ReviewInfo;
import com.example.android.project1.Model.TrailerInfo;
import com.example.android.project1.Utils.MovieDBService;
import com.example.android.project1.Utils.YoutubeUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailActivity extends AppCompatActivity {
	
    public static final String EXTRA_MOVIE = "movie";
    private static final String BASE_URL_IMAGES = "http://image.tmdb.org/t/p/w500";
    private static final String BASE_URL_THEMOVIEDB = "http://api.themoviedb.org/3/";
    private MovieInfo mMovie;
    ContentResolver contentResolver;
	
	@Bind(R.id.backdrop)
    ImageView backdrop;
	@Bind(R.id.movie_poster)
    ImageView poster;
	@Bind(R.id.movie_title)
    TextView title;
	@Bind(R.id.movie_description)
    TextView description;
	@Bind(R.id.movie_user_rating)
    TextView userRating;
	@Bind(R.id.movie_release_date)
    TextView releaseDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        } else {
            throw new IllegalArgumentException(getResources().getString(R.string.error_detail_activity_parceable));
        }
        contentResolver=getContentResolver();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(mMovie.title);
		ButterKnife.bind(this);

        title.setText(mMovie.title);
        description.setText(mMovie.description);
        userRating.setText(String.valueOf(mMovie.userRating));
        releaseDate.setText(new SimpleDateFormat("y").format(mMovie.releaseDate));
        Picasso.with(this)
                .load(BASE_URL_IMAGES + mMovie.imageURL)
                .into(poster, new Callback() {
            @Override
            public void onSuccess() {    }

            @Override
            public void onError() {
                poster.setVisibility(View.GONE);
            }
        });
        Picasso.with(this)
                .load(BASE_URL_IMAGES + mMovie.backDropUrl)
                .into(backdrop, new Callback() {
            @Override
            public void onSuccess() {      }
            @Override
            public void onError() {
                poster.setVisibility(View.GONE);
            }
        });
        GetTrailerData(mMovie.id);
        GetReviewData(mMovie.id);

        ImageButton favoriteButton = (ImageButton)findViewById(R.id.movie_favorite);
        favoriteButton.setVisibility(View.VISIBLE);
        if (isFavorite()) {
            setButtonMarkAsFavorite(favoriteButton);
        } else {
            setButtonMarkAsNotFavorite(favoriteButton);
        }
        favoriteButton.setTag(isFavorite());
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavorite = (Boolean) view.getTag();
                if (isFavorite) {
                    setButtonMarkAsNotFavorite((ImageButton) view);
                    RemoveFavorite();

                } else {
                    setButtonMarkAsFavorite((ImageButton) view);
                    AddFavorite();

                }
                view.setTag(isFavorite());
            }
        });
    }

    private void setButtonMarkAsNotFavorite(ImageButton favoriteButton) {
        favoriteButton.setImageResource(R.drawable.ic_favorite_off);
    }

    private void setButtonMarkAsFavorite(ImageButton favoriteButton) {
        favoriteButton.setImageResource(R.drawable.ic_favorite_on);
    }

    private void AddFavorite() {
       contentResolver.insert(MoviesContract.CONTENT_URI, mMovie.toContentValues());
    }

    private void RemoveFavorite() {
        contentResolver.delete(MoviesContract.buildMovieUri(mMovie.id), null, null);
    }
    public boolean isFavorite() {
        Cursor c = contentResolver.query(MoviesContract.buildMovieUri(mMovie.id),null,null,null,null);
        return c != null && c.getCount() > 0;
    }

    private void GetReviewData(int movieId) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_THEMOVIEDB)
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                .build();

        MovieDBService movieDBService = retrofit.create(MovieDBService.class);

        final Call<ReviewInfo.ReviewResult> call;

        call = movieDBService.getReviews(movieId, BuildConfig.API_KEY);

        call.enqueue(new retrofit2.Callback<ReviewInfo.ReviewResult>() {
            @Override
            public void onResponse(Call<ReviewInfo.ReviewResult> call, Response<ReviewInfo.ReviewResult> response) {
                if (response.isSuccessful()) {
                    setReviews(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<ReviewInfo.ReviewResult> call, Throwable t) {
            }
        });
    }

    private void GetTrailerData(int movieId)
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_THEMOVIEDB)
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                .build();

        MovieDBService movieDBService = retrofit.create(MovieDBService.class);

        final Call<TrailerInfo.TrailerResult> call;

        call = movieDBService.getVideos(movieId, BuildConfig.API_KEY);

        call.enqueue(new retrofit2.Callback<TrailerInfo.TrailerResult>() {
            @Override
            public void onResponse(Call<TrailerInfo.TrailerResult> call, Response<TrailerInfo.TrailerResult> response) {
                if (response.isSuccessful()) {
                   setTrailers(response.body().getResults());
                }
            }
            @Override
            public void onFailure(Call<TrailerInfo.TrailerResult> call, Throwable t) {

            }
        });
    }
    private void setTrailers(List<TrailerInfo> movieTrailers) throws NullPointerException {
        LinearLayout blockTrailers = (LinearLayout) findViewById(R.id.movie_trailers);
        blockTrailers.setVisibility(View.VISIBLE);
        LinearLayout trailers = (LinearLayout)blockTrailers.findViewById(R.id.trailer_list);
        for (TrailerInfo trailer : movieTrailers) {
            LinearLayout trailerItem = (LinearLayout) LayoutInflater.from(this)
                    .inflate(R.layout.trailer_item, trailers, false);
            TextView trailerName = (TextView)trailerItem.findViewById(R.id.name_trailer);
            trailerName.setText(trailer.getName());
            trailerName.setTag(trailer.getKey());
            trailerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View trailer) {
                    String keyTrailer = trailer.getTag().toString();
                    YoutubeUtil.playVideo(trailer.getContext(), keyTrailer);
                }
            });
            trailers.addView(trailerItem);
        }
    }

    private void setReviews(List<ReviewInfo> movieReviews) throws NullPointerException {
        LinearLayout blockReviews = (LinearLayout) findViewById(R.id.movie_reviews);
        blockReviews.setVisibility(View.VISIBLE);
        LinearLayout reviews = (LinearLayout)blockReviews.findViewById(R.id.review_list);
        for (ReviewInfo review : movieReviews) {
            LinearLayout reviewItem = (LinearLayout)LayoutInflater.from(this)
                    .inflate(R.layout.review_item, reviews, false);
            TextView reviewAuthor = (TextView)reviewItem.findViewById(R.id.review_author);
            reviewAuthor.setText(getString(R.string.head_review) + review.getAuthor());
            TextView reviewText = (TextView)reviewItem.findViewById(R.id.review_text);
            reviewText.setText(review.getContent());
            reviewText.setTag(review.getId());
            if (review.isVisible()) {
                setTextViewReviewVisible(reviewAuthor, reviewText);
            } else {
                setTextViewReviewInvisible(reviewAuthor, reviewText);
            }
            reviewAuthor.setTag(reviewText);
            reviews.addView(reviewItem);
            reviewAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView reviewAuthor = (TextView) view;
                    TextView reviewText = (TextView) reviewAuthor.getTag();
                    if (reviewText.getVisibility() == View.VISIBLE) {
                        setTextViewReviewInvisible(reviewAuthor, reviewText);
                    } else {
                        setTextViewReviewVisible(reviewAuthor, reviewText);
                    }
                }
            });
        }
    }private void setTextViewReviewInvisible(TextView reviewAuthor, TextView reviewText) {

    }

    private void setTextViewReviewVisible(TextView reviewAuthor, TextView reviewText) {

    }
}
