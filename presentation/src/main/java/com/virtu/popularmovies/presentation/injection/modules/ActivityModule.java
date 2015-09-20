package com.virtu.popularmovies.presentation.injection.modules;

import android.app.Activity;

import com.virtu.popularmovies.presentation.injection.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by virtu on 25/06/2015.
 */
@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity){
        this.mActivity = activity;
    }
    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides
    @PerActivity
    Activity activity() {
        return this.mActivity;
    }
}
