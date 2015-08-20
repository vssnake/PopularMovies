package com.virtu.popularmovies.domain.interactor.usesCases;

/**
 * Created by virtu on 08/07/2015.
 */

import com.virtu.popularmovies.domain.executor.PostExecutionThread;
import com.virtu.popularmovies.domain.executor.ThreadExecutor;
import com.virtu.popularmovies.domain.interactor.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by virtu on 08/07/2015.
 */
public class GetMovieUseCase extends UseCase {

    private final Long mIdMovie;
    private final com.virtu.popularmovies.domain.repository.MoviesRepository mMoviesRepository;

    @Inject
    public GetMovieUseCase(Long idMovie, com.virtu.popularmovies.domain.repository.MoviesRepository moviesRepository, ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread){
        super(threadExecutor,postExecutionThread);

        this.mIdMovie = idMovie;
        this.mMoviesRepository = moviesRepository;

    }

    @Override
    public Observable buildUseCaseObservable() {
        return this.mMoviesRepository.getMovie(this.mIdMovie);
    }
}