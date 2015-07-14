package com.virtu.popularmovies.domain.interactor.usesCases;

import com.virtu.popularmovies.domain.executor.PostExecutionThread;
import com.virtu.popularmovies.domain.executor.ThreadExecutor;
import com.virtu.popularmovies.domain.interactor.UseCase;
import com.virtu.popularmovies.domain.repository.MoviesRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by virtu on 08/07/2015.
 */
public class GetPopularMoviesUseCase extends UseCase {

    private final MoviesRepository mMoviesRepository;

    @Inject
    public GetPopularMoviesUseCase(MoviesRepository moviesRepository, ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread){
        super(threadExecutor,postExecutionThread);

        this.mMoviesRepository = moviesRepository;

    }

    @Override
    protected Observable buildUserCaseObservable() {
        return this.mMoviesRepository.getPopularMoviesDesc();
    }
}
