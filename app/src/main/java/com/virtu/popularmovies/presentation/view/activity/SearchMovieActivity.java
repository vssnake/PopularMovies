package com.virtu.popularmovies.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.virtu.spotifystreamer.R;
import com.virtu.popularmovies.presentation.injection.components.DaggerMovieComponent;
import com.virtu.popularmovies.presentation.model.MovieModelPresenter;
import com.virtu.popularmovies.presentation.injection.components.MovieComponent;
import com.virtu.popularmovies.presentation.presenter.SearchMoviePresenter;
import com.virtu.popularmovies.presentation.view.fragment.SearchSearchMovieFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SearchMovieActivity extends ComponentActivity<MovieComponent> implements
        SearchSearchMovieFragment.MovieListListener{

    @InjectView(R.id.pager)
    ViewPager mViewPager;
    //@InjectView(R.id.sliding_tabs)
    //TabLayout mTabLayout;
    Toolbar mToolbar;

    private PagerAdapter mPagerAdapter;

    /**
     * Launch this activity
     * @param context
     * @return
     */
    public static Intent getCallingIntent(Context context){
        return new Intent(context,SearchMovieActivity.class);
    }

    private static final int NUM_PAGES = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.app_name);
            setSupportActionBar(mToolbar);
        }


        this.initializeInjector();
        ButterKnife.inject(this);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        //mTabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            navigator.navigateToSettingsMovie(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeInjector() {
        this.component = DaggerMovieComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public void onMovieClicked(MovieModelPresenter movieModelPresenter) {
        this.navigator.navigateToDetailsMovie(this,movieModelPresenter.getId());
    }

    @Override
    public void onTitleChanged(String title) {
        mToolbar.setTitle(title);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment =  new SearchSearchMovieFragment();
            Bundle bundle = new Bundle();
            switch (position){
                case 0:
                    bundle.putString(SearchSearchMovieFragment.TYPE_LIST_KEY,
                            SearchMoviePresenter.TYPE_MOVIES_LIST.POPULAR.toString());
                    break;
                case 1:
                    bundle.putString(SearchSearchMovieFragment.TYPE_LIST_KEY,
                            SearchMoviePresenter.TYPE_MOVIES_LIST.HIGH_RATED.toString());
                    break;
            }

            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


        @Override
        public String getPageTitle(int position){
            String title = "";
            switch (position){
                case 0:
                    title =  SearchMovieActivity.this.getResources()
                            .getString(R.string.search_movie_tab_popular);
                    break;
                case 1:
                    title =  SearchMovieActivity.this.getResources()
                            .getString(R.string.search_movie_tab_high_score);
                    break;
            }
             return title;
         }
    }
}
