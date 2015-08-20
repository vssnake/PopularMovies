package com.virtu.popularmovies.test.app.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;

import com.example.virtu.spotifystreamer.R;
import com.virtu.popularmovies.presentation.view.activity.SearchMovieActivity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by unai on 20/08/15.
 */
public class SearchMovieActivityTest extends ActivityInstrumentationTestCase2<SearchMovieActivity> {


    private SearchMovieActivity mSearchMovieActivity;


    public SearchMovieActivityTest(Class<SearchMovieActivity> activityClass) {
        super(activityClass);
    }


    @Override protected void setUp() throws Exception{
        super.setUp();
        this.setActivityIntent(createTargetIntent());
        mSearchMovieActivity = getActivity();
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testContainerSearchMovieFragment(){
        Fragment searchMovieFragment =
                mSearchMovieActivity.getSupportFragmentManager().findFragmentById(R.id.pager);
        assertThat(searchMovieFragment,is(notNullValue()));
    }
    private Intent createTargetIntent() {
        Intent intentLaunchActivity =
                SearchMovieActivity.getCallingIntent(getInstrumentation().getTargetContext());

        return intentLaunchActivity;
    }
}
