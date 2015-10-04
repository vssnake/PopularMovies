package com.virtu.popularmovies.presentation.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virtu.popularmovies.domain.entities.Review;
import com.virtu.popularmovies.domain.entities.Video;
import com.virtu.popularmovies.presentation.R;
import com.virtu.popularmovies.presentation.model.MovieModelPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by virtu on 03/10/2015.
 */
public class ReviewAdapter  extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    List<Review> mMovieReviews;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public ReviewAdapter(MovieModelPresenter movieDetails,Context context){
        this.mMovieReviews = movieDetails.getReviews();
        this.mLayoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.mLayoutInflater.inflate(R.layout.review_item,parent,false);
        ReviewViewHolder trailerViewHolder = new ReviewViewHolder(view);
        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.author.setText(mMovieReviews.get(position).getAuthor());
        holder.content.setText(mMovieReviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mMovieReviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.review_item_author)
        public TextView author;
        @Bind(R.id.review_item_content)
        public TextView content;



        public ReviewViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}

