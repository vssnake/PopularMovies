package com.virtu.popularmovies.domain.interactor.usesCases;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by virtu on 08/07/2015.
 */
/**
 * Created by virtu on 08/07/2015.
 */
public class GetHighestRatedMoviesUseCase extends com.virtu.popularmovies.domain.interactor.UseCase {

    private final com.virtu.popularmovies.domain.repository.MoviesRepository mMoviesRepository;

    @Inject
    public GetHighestRatedMoviesUseCase(com.virtu.popularmovies.domain.repository.MoviesRepository moviesRepository, com.virtu.popularmovies.domain.executor.ThreadExecutor threadExecutor,
                           com.virtu.popularmovies.domain.executor.PostExecutionThread postExecutionThread){
        super(threadExecutor,postExecutionThread);

        this.mMoviesRepository = moviesRepository;

    }

    @Override
    public Observable buildUseCaseObservable() {
        return this.mMoviesRepository.getHighestRatedMoviesDesc();
    }
}
