package com.virtu.popularmovies.presentation.injection.components;

import android.content.Context;

import com.virtu.popularmovies.presentation.injection.modules.ApplicationModule;
import com.virtu.popularmovies.presentation.view.activity.BaseActivity;
import com.virtu.popularmovies.domain.executor.PostExecutionThread;
import com.virtu.popularmovies.domain.executor.ThreadExecutor;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by virtu on 25/06/2015.
 * A component with lifetime of the app
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseActivity baseActivity);

    //Exposed to sub-graphs
    Context context();
    ThreadExecutor threadExecutor();
    PostExecutionThread postExecutionThread();
    com.virtu.popularmovies.domain.repository.MoviesRepository moviesRepository();
}
