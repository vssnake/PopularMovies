package com.virtu.popularmovies.presentation.presenter;

import android.support.annotation.NonNull;

import com.virtu.popularmovies.presentation.model.MovieModelPresenter;
import com.virtu.popularmovies.presentation.model.mapper.MovieModelDataMapper;
import com.virtu.popularmovies.domain.interactor.DefaultSubcriber;
import com.virtu.popularmovies.domain.interactor.UseCase;
import com.virtu.popularmovies.presentation.exception.ErrorMessageFactory;
import com.virtu.popularmovies.presentation.injection.modules.MoviesModule;
import com.virtu.popularmovies.presentation.view.connectors.SearchMovieView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by virtu on 25/06/2015.
 */
public class SearchMoviePresenter implements Presenter {

    private SearchMovieView mMovieViewGridView;

    private final UseCase mGetMoviesPopularUseCase;
    private final UseCase mGetMoviesHighRatingUseCase;

    private final MovieModelDataMapper mMovieModelDataMapper;

    public enum TYPE_MOVIES_LIST{
        HIGH_RATED,
        POPULAR;
    }

    private TYPE_MOVIES_LIST mTypeMoviesList = TYPE_MOVIES_LIST.HIGH_RATED;

    @Inject
    public SearchMoviePresenter(
            @Named(MoviesModule.HIGH_RATED_MOVIES) UseCase getMoviesHighRatingUseCase,
            @Named(MoviesModule.POPULAR_MOVIES) UseCase getMoviesPopularUseCase,
            MovieModelDataMapper movieModelDataMapper){
        this.mGetMoviesHighRatingUseCase = getMoviesHighRatingUseCase;
        this.mGetMoviesPopularUseCase = getMoviesPopularUseCase;
        this.mMovieModelDataMapper = movieModelDataMapper;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.mGetMoviesPopularUseCase.unsubscribe();
        this.mGetMoviesHighRatingUseCase.unsubscribe();
    }
    


    public void initialize(TYPE_MOVIES_LIST type_movies_list){
        this.mTypeMoviesList = type_movies_list;
        this.loadMoviesList();
    }
    public void setView(@NonNull SearchMovieView view) {
        this.mMovieViewGridView = view;
    }

    public void onMovieClicked(MovieModelPresenter movie) {
        this.mMovieViewGridView.viewMovie(movie);
    }

    /**
     * Loads all users.
     */
    private void loadMoviesList() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getMoviesList();
    }

    private void showViewLoading() {
        this.mMovieViewGridView.showLoading();
    }

    private void hideViewLoading() {
        this.mMovieViewGridView.hideLoading();
    }

    private void showErrorMessage(com.virtu.popularmovies.domain.exception.ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.mMovieViewGridView.getContext(),
                errorBundle.getException());
        this.mMovieViewGridView.showError(errorMessage);
    }

    private void showViewRetry() {
        this.mMovieViewGridView.showRetry();
    }

    private void hideViewRetry() {
        this.mMovieViewGridView.hideRetry();
    }

    private void getMoviesList() {
        switch (mTypeMoviesList){
            case HIGH_RATED:
                this.mGetMoviesHighRatingUseCase.execute(new MovieListSubscriber ());
                break;
            case POPULAR:
                this.mGetMoviesPopularUseCase.execute(new MovieListSubscriber());
                break;
        }
    }

    private void showMoviesCollectionInView(Collection<com.virtu.popularmovies.domain.entities.Movie> usersCollection) {
        final Collection<MovieModelPresenter> userModelsCollection =
                this.mMovieModelDataMapper.transform(usersCollection);
        this.mMovieViewGridView.renderMoviesList(userModelsCollection);
    }

    private final class MovieListSubscriber extends DefaultSubcriber<List<com.virtu.popularmovies.domain.entities.Movie>> {

        @Override public void onCompleted() {
            SearchMoviePresenter.this.hideViewLoading();
        }

        @Override public void onError(Throwable e) {
            SearchMoviePresenter.this.hideViewLoading();
            SearchMoviePresenter.this.showErrorMessage(new com.virtu.popularmovies.domain.exception.DefaultErrorBundle((Exception) e));
            SearchMoviePresenter.this.showViewRetry();
        }

        @Override public void onNext(List<com.virtu.popularmovies.domain.entities.Movie> movies) {
            SearchMoviePresenter.this.showMoviesCollectionInView(movies);
        }
    }


}
