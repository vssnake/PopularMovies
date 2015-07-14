package com.virtu.popularmovies.presentation.injection.components;

import android.app.Activity;

import com.virtu.popularmovies.presentation.injection.modules.ActivityModule;
import com.virtu.popularmovies.presentation.injection.PerActivity;

import dagger.Component;

/**
 * Created by virtu on 25/06/2015.
 * A component whose lifetime depends of the lifetime of activity
 * {@link PerActivity}
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    //Exposed to sub-graphs
    Activity activity();
}
