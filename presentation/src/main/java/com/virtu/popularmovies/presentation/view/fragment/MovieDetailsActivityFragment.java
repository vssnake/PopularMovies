package com.virtu.popularmovies.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.virtu.popularmovies.domain.entities.MovieD;
import com.virtu.popularmovies.presentation.R;

import com.virtu.popularmovies.presentation.injection.components.DaggerMovieComponent;
import com.virtu.popularmovies.presentation.injection.components.MovieComponent;
import com.virtu.popularmovies.presentation.injection.modules.MoviesModule;
import com.virtu.popularmovies.presentation.model.MovieModelPresenter;
import com.virtu.popularmovies.presentation.presenter.MovieDetailsPresenter;
import com.virtu.popularmovies.presentation.utils.CustomLinearLayoutManager;
import com.virtu.popularmovies.presentation.view.activity.MovieDetailsActivity;
import com.virtu.popularmovies.presentation.view.adapter.ReviewAdapter;
import com.virtu.popularmovies.presentation.view.adapter.TrailerAdapter;
import com.virtu.popularmovies.presentation.view.connectors.MovieDetailsView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends BaseFragment<MovieDetailsActivity>
        implements MovieDetailsView {

    public MovieDetailsActivityFragment() {
    }

    public static MovieDetailsActivityFragment newInstance(Long movieID) {
        MovieDetailsActivityFragment movieDetailsFragment = new MovieDetailsActivityFragment();

        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putLong(ARGUMENT_KEY_MOVIE_ID, movieID);
        movieDetailsFragment.setArguments(argumentsBundle);

        return movieDetailsFragment;
    }
    private Long mMovieID;

    public static final String ARGUMENT_KEY_MOVIE_ID = "mMovieID";

    @Inject
    MovieDetailsPresenter presenter;

    MovieModelPresenter movie;

    @Bind(R.id.details_title)
    TextView mTitle;
    @Bind(R.id.details_summary)
    TextView mSummary;
    @Bind(R.id.details_relase_date)
    TextView mReleaseDate;
    @Bind(R.id.details_user_rating)
    TextView mUserRating;
    @Bind(R.id.details_trailers_recycler)
    RecyclerView mRecycleTrailers;
    @Bind(R.id.details_trailer_reviews_recycler)
    RecyclerView mRecycleReviews;
    @Bind(R.id.details_favourite)
    Button mDetailFavouriteButton;
    @Bind(R.id.fragment_movie_details_main_container)
    ScrollView mainContainer;

    TrailerAdapter mTrailerAdapter;
    CustomLinearLayoutManager mTrailerLinearLayoutManager;

    ReviewAdapter mReviewAdapter;
    CustomLinearLayoutManager mReviewLinearLayoutManager;

    private ShareActionProvider mShareActionProvider;

    @OnClick(R.id.details_favourite)
    public void favourite(){
        if(!presenter.changeFavourite()){
            mDetailFavouriteButton.setBackgroundResource(R.drawable.abc_btn_default_mtrl_shape);
        }else{
            mDetailFavouriteButton.setBackgroundResource(R.drawable.shrared_shape);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView =  inflater.inflate(R.layout.fragment_movie_details, container, false);

        ButterKnife.bind(this, fragmentView);
        mTrailerLinearLayoutManager = new CustomLinearLayoutManager(getContext());
        mReviewLinearLayoutManager = new CustomLinearLayoutManager(getContext());
        mRecycleTrailers.setLayoutManager(mTrailerLinearLayoutManager);
        mRecycleReviews.setLayoutManager(mReviewLinearLayoutManager);

        setHasOptionsMenu(true);

        mainContainer.setVisibility(View.INVISIBLE);
        return fragmentView;
    }
    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
    }

    @Override public void onResume() {
        super.onResume();
        this.presenter.resume();
    }

    @Override public void onPause() {
        super.onPause();
        this.presenter.pause();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        this.presenter.destroy();
    }



    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }


    private void initialize() {

        this.getComponent(MovieComponent.class).inject(this);
        this.presenter.setView(this);

            this.mMovieID = getArguments().getLong(ARGUMENT_KEY_MOVIE_ID);
        if (mMovieID != -1){
            this.presenter.initialize(this.mMovieID);
        }



    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);



    }


    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        Uri youtubeUri = Uri.parse("http://www.youtube.com/watch?v=" +
                "" + movie.getVideos().get(0).getKeyVideo());
        shareIntent.putExtra(Intent.EXTRA_TEXT, youtubeUri.toString());
        return shareIntent;
    }

    @Override
    public void renderMovieDetail(MovieModelPresenter movie) {
        this.movie = movie;

        mSummary.setText(movie.getOverView());
        mReleaseDate.setText(movie.getReleaseDate());
        mUserRating.setText(movie.getVote_average() +"");
        mTitle.setText(movie.getTitle());
        if (getActivity().getClass().getSimpleName().contains(MovieDetailsActivity.class.getSimpleName())){
            getAttachActivity().changeBackgroundImage(movie);
        }


        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if ( this.movie != null && this.movie.getVideos().size() != 0) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

       if(!movie.getFavourite()){
            mDetailFavouriteButton.setBackgroundResource(R.drawable.abc_btn_default_mtrl_shape);
        }else{
            mDetailFavouriteButton.setBackgroundResource(R.drawable.shrared_shape);
        }

        if (movie.getVideos() != null){
            mTrailerAdapter = new TrailerAdapter(movie,getContext());
            mRecycleTrailers.setAdapter(mTrailerAdapter);
        }


        if (movie.getReviews() != null){
            mReviewAdapter = new ReviewAdapter(movie, getContext());
            mRecycleReviews.setAdapter(mReviewAdapter);
        }

        mainContainer.setVisibility(View.VISIBLE);

    }
}
