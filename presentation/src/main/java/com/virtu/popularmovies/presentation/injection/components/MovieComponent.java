package com.virtu.popularmovies.presentation.injection.components;

import com.virtu.popularmovies.presentation.injection.modules.ActivityModule;
import com.virtu.popularmovies.presentation.view.fragment.MovieDetailsActivityFragment;
import com.virtu.popularmovies.presentation.injection.PerActivity;
import com.virtu.popularmovies.presentation.injection.modules.MoviesModule;
import com.virtu.popularmovies.presentation.view.fragment.SearchSearchMovieFragment;

import dagger.Component;

/**
 * Created by virtu on 25/06/2015.
 * Inject components in the specifics Fragments
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, MoviesModule.class})
public interface MovieComponent extends ActivityComponent{

    void inject(SearchSearchMovieFragment searchMovieFragment);
    void inject(MovieDetailsActivityFragment movieDetailsFragment);
}
