package com.example.android.project1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rgarcia on 12/02/2017.
 */

public class MyRecyclerViewAdapter  extends RecyclerView.Adapter<MyRecyclerViewAdapter.MovieViewHolder> {

    private List<MovieInfo> movieList;

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_info_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        final MovieViewHolder viewHolder = new MovieViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movieList.get(position));
                context.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieInfo movie = movieList.get(position);
        String posterURL = "http://image.tmdb.org/t/p/w500" + movie.imageURL;
        ImageView imageView = holder.mainImage;
        Picasso.with(imageView.getContext()).load(posterURL).into(imageView);
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public void setMovieList(MovieInfo.MovieResult movieList) {
        this.movieList = new ArrayList<>();
        this.movieList.addAll(movieList.getResults());
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
		
		@Bind(R.id.item_image)
        private ImageView mainImage;

        public MovieViewHolder(View itemView) {
            super(itemView);
		Butterknife.bind(this);
        }
    }
}
