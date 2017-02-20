package com.example.android.project1;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {
	
    public static final String EXTRA_MOVIE = "movie";
    private static final String BASE_URL_IMAGES = "http://image.tmdb.org/t/p/w500";
    private MovieInfo mMovie;
	
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(mMovie.title);
		Butterknife.bind(this);
        
        title.setText(mMovie.title);
        description.setText(mMovie.description);
        userRating.setText(String.valueOf(mMovie.userRating));
        releaseDate.setText(new SimpleDateFormat("y").format(mMovie.releaseDate));
        Picasso.with(this)
                .load(BASE_URL_IMAGES + mMovie.imageURL)
                .into(poster);
        Picasso.with(this)
                .load(BASE_URL_IMAGES + mMovie.backDropUrl)
                .into(backdrop);
    }
}
