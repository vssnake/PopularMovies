package com.virtu.popularmovies.presentation.injection.modules;

import android.content.Context;

import com.virtu.popularmovies.data.executor.JobExecutor;
import com.virtu.popularmovies.data.repository.MoviesDataRepository;
import com.virtu.popularmovies.presentation.navigation.Navigator;
import com.virtu.popularmovies.domain.executor.PostExecutionThread;
import com.virtu.popularmovies.domain.executor.ThreadExecutor;
import com.virtu.popularmovies.domain.repository.MoviesRepository;
import com.virtu.popularmovies.presentation.AndroidApp;
import com.virtu.popularmovies.presentation.UIThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by virtu on 25/06/2015.
 * Dagger module. Provides object which will interact in the app lifetime
 */
@Module
public class ApplicationModule {
    private final AndroidApp mApp;

    public ApplicationModule(AndroidApp app){
        this.mApp = app;
    }
    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.mApp;
    }

    @Provides
    @Singleton
    Navigator provideNavigator() {
        return new Navigator();
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }


    @Provides
    @Singleton
    MoviesRepository provideMoviesRepository(MoviesDataRepository moviesRepository) {
        return moviesRepository;
    }
}
