package com.virtu.popularmovies.domain.interactor.usesCases;

import com.virtu.popularmovies.domain.executor.PostExecutionThread;
import com.virtu.popularmovies.domain.executor.ThreadExecutor;
import com.virtu.popularmovies.domain.interactor.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by virtu on 20/09/2015.
 */
public class GetFavouriteMoviesUseCase extends UseCase {

    private List<Long> mIdMovies;
    private final com.virtu.popularmovies.domain.repository.MoviesRepository mMoviesRepository;

    @Inject
    public GetFavouriteMoviesUseCase(com.virtu.popularmovies.domain.repository.MoviesRepository moviesRepository, ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread){
        super(threadExecutor,postExecutionThread);

        this.mMoviesRepository = moviesRepository;

    }

    @Override
    public Observable buildUseCaseObservable() {
        return this.mMoviesRepository.getFavouriteMovies(this.mIdMovies);
    }

    public void execute(List<Long> idMovies,Subscriber UserCaseSubscriber){
        mIdMovies = idMovies;
        super.execute(UserCaseSubscriber);


    }
}
