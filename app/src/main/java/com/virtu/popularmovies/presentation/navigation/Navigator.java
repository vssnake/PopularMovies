package com.virtu.popularmovies.presentation.navigation;

import android.content.Context;
import android.content.Intent;

import com.virtu.popularmovies.presentation.view.activity.MovieDetailsActivity;
import com.virtu.popularmovies.presentation.view.activity.SearchMovieActivity;

import javax.inject.Inject;

/**
 * Class used to navigate through the app
 */
public class Navigator {


    @Inject
    public void Navigator(){

    }

    /**
     * Open the Search MovieModelPresenter Screen
     * @param context context of the app needed to open it.
     */
    public void navigateToSearchMovie(Context context){
        if (context != null){
            Intent intentToLaunch = SearchMovieActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToDetailsMovie(Context context,Long movieID){
        if (context != null && movieID != null){
            Intent intentToLaunch = MovieDetailsActivity.getCallingIntent(context,movieID);
            context.startActivity(intentToLaunch);
        }
    }
}
