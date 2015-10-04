package com.virtu.popularmovies.test.app.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.virtu.popularmovies.presentation.R;
import com.virtu.popularmovies.presentation.view.activity.MovieDetailsActivity;
import com.virtu.popularmovies.presentation.view.activity.SearchMovieActivity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by unai on 20/08/15.
 */
public class MovieDetailsActivityTest extends ActivityInstrumentationTestCase2<MovieDetailsActivity> {


    private MovieDetailsActivity mMovieDetailsActivity;

    public MovieDetailsActivityTest(){
        super(MovieDetailsActivity.class);
    }

    public MovieDetailsActivityTest(Class<MovieDetailsActivity> activityClass) {
        super(activityClass);
    }


    @Override protected void setUp() throws Exception{
        super.setUp();
        this.setActivityIntent(createTargetIntent());
        mMovieDetailsActivity = getActivity();
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInterfaceInialized(){
        View mainContent = mMovieDetailsActivity.findViewById(R.id.main_content);
        assertThat(mainContent,is(notNullValue()));
    }
    private Intent createTargetIntent() {
        Intent intentLaunchActivity =
                SearchMovieActivity.getCallingIntent(getInstrumentation().getTargetContext());

        return intentLaunchActivity;
    }

}
