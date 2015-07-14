package com.virtu.popularmovies.presentation.presenter;

import android.support.annotation.NonNull;

import com.virtu.popularmovies.presentation.model.MovieModelPresenter;
import com.virtu.popularmovies.domain.entities.Movie;
import com.virtu.popularmovies.domain.exception.DefaultErrorBundle;
import com.virtu.popularmovies.domain.exception.ErrorBundle;
import com.virtu.popularmovies.domain.interactor.DefaultSubcriber;
import com.virtu.popularmovies.domain.interactor.UseCase;
import com.virtu.popularmovies.presentation.exception.ErrorMessageFactory;
import com.virtu.popularmovies.presentation.injection.modules.MoviesModule;
import com.virtu.popularmovies.presentation.model.mapper.MovieModelDataMapper;
import com.virtu.popularmovies.presentation.view.connectors.MovieDetailsView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by virtu on 11/07/2015.
 */
public class MovieDetailsPresenter implements Presenter  {

    private MovieDetailsView mMovieDetails;

    private Long mMovieID;

    private UseCase mGetMovieUseCase;

    private MovieModelDataMapper mMovieModelDataMapper;

    @Inject
    public MovieDetailsPresenter(
            @Named(MoviesModule.MOVIE) UseCase getMovieUseCase,
            MovieModelDataMapper movieModelDataMapper){
        this.mGetMovieUseCase = getMovieUseCase;
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

    }

    public void setView(@NonNull MovieDetailsView view) {
        this.mMovieDetails = view;
    }

    public void initialize(Long movieID){
        this.mMovieID = movieID;
        this.loadMovie();
    }

    public void loadMovie(){

        this.hideViewRetry();
        this.showViewLoading();
        getDetailMovie();

    }

    private void showViewLoading() {
        this.mMovieDetails.showLoading();
    }

    private void hideViewLoading() {
        this.mMovieDetails.hideLoading();
    }

    private void showViewRetry() {
        this.mMovieDetails.showRetry();
    }

    private void hideViewRetry() {
        this.mMovieDetails.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.mMovieDetails.getContext(),
                errorBundle.getException());
        this.mMovieDetails.showError(errorMessage);
    }

    private void showMovieDetails(Movie movie){
        final MovieModelPresenter movieModel =
                this.mMovieModelDataMapper.transform(movie);
        this.mMovieDetails.renderMovieDetail(movieModel);
    }

    private void getDetailMovie(){
        this.mGetMovieUseCase.execute(new MovieListSubscriber());
    }

    private final class MovieListSubscriber extends DefaultSubcriber<Movie> {

        @Override public void onCompleted() {
            MovieDetailsPresenter.this.hideViewLoading();
        }

        @Override public void onError(Throwable e) {
            MovieDetailsPresenter.this.hideViewLoading();
            MovieDetailsPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            MovieDetailsPresenter.this.showViewRetry();
        }

        @Override public void onNext(Movie movie) {
            MovieDetailsPresenter.this.showMovieDetails(movie);
        }
    }
}
