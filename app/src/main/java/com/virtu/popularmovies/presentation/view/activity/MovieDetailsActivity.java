package com.virtu.popularmovies.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.virtu.spotifystreamer.R;
import com.squareup.picasso.Picasso;
import com.virtu.popularmovies.presentation.injection.components.DaggerMovieComponent;
import com.virtu.popularmovies.presentation.injection.components.MovieComponent;
import com.virtu.popularmovies.presentation.model.MovieModelPresenter;
import com.virtu.popularmovies.presentation.view.fragment.MovieDetailsActivityFragment;
import com.virtu.popularmovies.presentation.injection.modules.MoviesModule;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MovieDetailsActivity extends ComponentActivity<MovieComponent> {

    private static final String MOVIE_ID_KEY = "movieID";

    private Long movieID;

    @Bind(R.id.activity_details_movie_image)
    ImageView mTitleImageView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mToolbarCollapsing;

    /**
     * Launch this activity
     * @param context
     * @return
     */
    public static Intent getCallingIntent(Context context,Long movie){
        Intent callingIntent = new Intent(context,MovieDetailsActivity.class);
        callingIntent.putExtra(MOVIE_ID_KEY,movie);
        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

        }

        ButterKnife.bind(this);
        toolbar.setTitle(R.string.app_name);



        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.initializeActivity(savedInstanceState);
        this.initializeInjector();


    }


    @Override protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putLong(MOVIE_ID_KEY, this.movieID);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_movie_details, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeInjector() {
        this.component = DaggerMovieComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .moviesModule(new MoviesModule(this.movieID))
                .build();
    }

    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            this.movieID = getIntent().getLongExtra(MOVIE_ID_KEY, -1);
            addFragment(R.id.movie_details_fragment, MovieDetailsActivityFragment.newInstance(this.movieID));
        } else {
            this.movieID = savedInstanceState.getLong(MOVIE_ID_KEY);
        }
    }

    public void changeBackgroundImage(MovieModelPresenter movie){
        Picasso.with(this).load(movie.getPosterUrl()).into(mTitleImageView);
        mToolbarCollapsing.setTitle(movie.getTitle());
    }
}
