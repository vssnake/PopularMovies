package com.virtu.popularmovies.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.virtu.spotifystreamer.R;
import com.virtu.popularmovies.presentation.injection.components.MovieComponent;
import com.virtu.popularmovies.presentation.model.MovieModelPresenter;
import com.virtu.popularmovies.presentation.presenter.MovieDetailsPresenter;
import com.virtu.popularmovies.presentation.view.activity.MovieDetailsActivity;
import com.virtu.popularmovies.presentation.view.connectors.MovieDetailsView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    @Bind(R.id.details_title)
    TextView mTitle;
    @Bind(R.id.details_summary)
    TextView mSummary;
    @Bind(R.id.details_relase_date)
    TextView mReleaseDate;
    @Bind(R.id.details_user_rating)
    TextView mUserRating;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView =  inflater.inflate(R.layout.fragment_movie_details, container, false);

        ButterKnife.bind(this, fragmentView);

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
        this.presenter.initialize(this.mMovieID);
    }

    @Override
    public void renderMovieDetail(MovieModelPresenter movie) {
        mSummary.setText(movie.getOverView());
        mReleaseDate.setText(movie.getReleaseDate());
        mUserRating.setText(movie.getVote_average() +"");
        mTitle.setText(movie.getTitle());
        getAttachActivity().changeBackgroundImage(movie);
    }
}
