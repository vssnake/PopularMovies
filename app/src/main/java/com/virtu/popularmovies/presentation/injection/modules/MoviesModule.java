package com.virtu.popularmovies.presentation.injection.modules;

import com.virtu.popularmovies.domain.executor.PostExecutionThread;
import com.virtu.popularmovies.domain.executor.ThreadExecutor;
import com.virtu.popularmovies.domain.interactor.UseCase;
import com.virtu.popularmovies.domain.interactor.usesCases.GetHighestRatedMoviesUseCase;
import com.virtu.popularmovies.domain.interactor.usesCases.GetMovieUseCase;
import com.virtu.popularmovies.presentation.injection.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by virtu on 25/06/2015.
 * Dagger module that provided artist related collaborators (Uses Cases)
 */
@Module
public class MoviesModule {
    public static final String POPULAR_MOVIES = "popularMovies";
    public static final String HIGH_RATED_MOVIES = "highRatedMovies";
    public static final String MOVIE = "movie";

    private Long mMovieID = -1l;

    public MoviesModule(){}

    public MoviesModule(Long moveID){ this.mMovieID = moveID;}

    @Provides
    @PerActivity
    @Named(HIGH_RATED_MOVIES)
    UseCase provideGetHighRatedMoviesUseCase(
            GetHighestRatedMoviesUseCase getHighestRatedMoviesUseCase) {
        return getHighestRatedMoviesUseCase;
    }

    @Provides
    @PerActivity
    @Named(POPULAR_MOVIES)
    UseCase provideGetPopularMoviesUseCase(
            com.virtu.popularmovies.domain.interactor.usesCases.GetPopularMoviesUseCase getPopularMoviesUseCase){
        return getPopularMoviesUseCase;
    }

    @Provides
    @PerActivity
    @Named(MOVIE)
    UseCase provideGetMovieUserCase(
            com.virtu.popularmovies.domain.repository.MoviesRepository moviesRepository,
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread){
        return new GetMovieUseCase(mMovieID, moviesRepository,threadExecutor,postExecutionThread);
    }


}
