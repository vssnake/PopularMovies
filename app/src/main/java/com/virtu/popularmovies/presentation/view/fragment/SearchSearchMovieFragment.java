package com.virtu.popularmovies.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.virtu.spotifystreamer.R;
import com.virtu.popularmovies.presentation.injection.components.MovieComponent;
import com.virtu.popularmovies.presentation.model.MovieModelPresenter;
import com.virtu.popularmovies.presentation.presenter.SearchMoviePresenter;
import com.virtu.popularmovies.presentation.view.activity.SearchMovieActivity;
import com.virtu.popularmovies.presentation.view.adapter.MoviesAdapter;
import com.virtu.popularmovies.presentation.view.adapter.MoviesLayoutManager;
import com.virtu.popularmovies.presentation.view.connectors.SearchMovieView;
import com.virtu.popularmovies.presentation.view.adapter.NoSpacingItemDecoration;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A placeholder fragment containing a simple view.
 */
public class SearchSearchMovieFragment extends BaseFragment<SearchMovieActivity> implements SearchMovieView {

    public SearchSearchMovieFragment() {
    }



    @Inject
    SearchMoviePresenter presenter;

    @InjectView(R.id.fragment_search_movie_recycle_view)
    RecyclerView recyclerViewMovies;

    @InjectView(R.id.rl_progress)
    RelativeLayout rl_progress;
    @InjectView(R.id.rl_retry)
    RelativeLayout rl_retry;
    @InjectView(R.id.bt_retry)
    Button bt_retry;

    public final static String  TYPE_LIST_KEY = "type_list";

    private MoviesAdapter moviesAdapter;
    private MoviesLayoutManager moviesLayoutManager;

    private SearchMoviePresenter.TYPE_MOVIES_LIST mTypeList;

    private MovieListListener mMoviesListListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        String typeMovieList = arguments.getString(TYPE_LIST_KEY,
                        SearchMoviePresenter.TYPE_MOVIES_LIST.POPULAR.toString());

        mTypeList  = SearchMoviePresenter.TYPE_MOVIES_LIST.valueOf((typeMovieList));

        View fragmentView = inflater.inflate(R.layout.fragment_search_movie,
                container,false);
        ButterKnife.inject(this,fragmentView);
        initUI();
        return fragmentView;
    }



    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MovieListListener) {
            this.mMoviesListListener = (MovieListListener) activity;
        }
    }

    @Override public void onResume() {
        super.onResume();
        this.presenter.resume();
        initGrid();
    }

    @Override public void onPause() {
        super.onPause();
        this.presenter.pause();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        this.presenter.destroy();
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.init();
    }
    public void initGrid(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String numberColumns = "2";
        switch (getResources().getConfiguration().orientation){
            case  Configuration.ORIENTATION_LANDSCAPE:
                numberColumns = sharedPreferences.getString(
                        getActivity().getString(R.string.pref_title_number_columns_landscape_key),
                        "3");
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                numberColumns = sharedPreferences.getString(
                        getActivity().getString(R.string.pref_title_number_columns_portrait_key),
                        "2");
                break;
        }
        this.moviesLayoutManager = new MoviesLayoutManager(getActivity(),Integer.parseInt(numberColumns));
        this.recyclerViewMovies.setLayoutManager(moviesLayoutManager);
        this.recyclerViewMovies.addItemDecoration(new NoSpacingItemDecoration());

        String typeSearch = sharedPreferences.getString(
                getActivity().getString(R.string.pref_title_type_search_key),"0");
        switch (Integer.parseInt(typeSearch)){
            case 0:
                this.presenter.initialize(SearchMoviePresenter.TYPE_MOVIES_LIST.HIGH_RATED);
                mMoviesListListener.onTitleChanged(getActivity()
                        .getString(R.string.search_movie_tab_high_score));
                break;
            case 1:
                this.presenter.initialize(SearchMoviePresenter.TYPE_MOVIES_LIST.POPULAR);
                mMoviesListListener.onTitleChanged(getActivity()
                        .getString(R.string.search_movie_tab_popular));
                break;
        }

    }

    public void initUI(){


        bt_retry.setOnClickListener(onClickListenerRetry);
    }


    private void init() {
        this.getComponent(MovieComponent.class).inject(this);
        this.presenter.setView(this);
    }


    @Override
    public void renderMoviesList(Collection<MovieModelPresenter> moviesModelCollection) {
        if (moviesModelCollection != null){
            if (this.moviesAdapter == null){
                this.moviesAdapter = new MoviesAdapter(getActivity(),moviesModelCollection);
            }else{
                this.moviesAdapter.setMoviesCollection(moviesModelCollection);
            }
            this.moviesAdapter.setOnItemClickListener(onItemClickListener);
            this.recyclerViewMovies.setAdapter(moviesAdapter);
        }
    }

    @Override
    public void viewMovie(MovieModelPresenter movieModel) {
        if (mMoviesListListener != null){
            this.mMoviesListListener.onMovieClicked(movieModel);
        }
    }

    @Override
    public void showLoading() {
        this.rl_progress.setVisibility(View.VISIBLE);
        this.getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void hideLoading() {
        this.rl_progress.setVisibility(View.GONE);
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showRetry() {
        this.rl_retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetry() {
        this.rl_retry.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }



    public interface MovieListListener{
        void onMovieClicked(final MovieModelPresenter movieModelPresenter);
        void onTitleChanged(String title);
    }

    private View.OnClickListener onClickListenerRetry =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchSearchMovieFragment.this.presenter.initialize(mTypeList);
                }
            };

    private MoviesAdapter.OnItemClickListener onItemClickListener =
            new MoviesAdapter.OnItemClickListener() {
                @Override public void onUserItemClicked(MovieModelPresenter userModel) {
                    if (SearchSearchMovieFragment.this.presenter != null && userModel != null) {
                        SearchSearchMovieFragment.this.presenter.onMovieClicked(userModel);
                    }
                }
            };



}
