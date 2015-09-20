package com.virtu.popularmovies.presentation.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.virtu.popularmovies.domain.entities.MovieD;
import com.virtu.popularmovies.domain.interactor.usesCases.GetFavouriteMoviesUseCase;
import com.virtu.popularmovies.presentation.model.MovieModelPresenter;
import com.virtu.popularmovies.presentation.model.mapper.MovieModelDataMapper;
import com.virtu.popularmovies.domain.interactor.DefaultSubcriber;
import com.virtu.popularmovies.domain.interactor.UseCase;
import com.virtu.popularmovies.presentation.exception.ErrorMessageFactory;
import com.virtu.popularmovies.presentation.injection.modules.MoviesModule;
import com.virtu.popularmovies.presentation.view.connectors.SearchMovieView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by virtu on 25/06/2015.
 */
public class SearchMoviePresenter implements Presenter {

    private SearchMovieView mMovieViewGridView;

    private final UseCase mGetMoviesPopularUseCase;
    private final UseCase mGetMoviesHighRatingUseCase;
    private final GetFavouriteMoviesUseCase mGetFavouriteMoviesUseCase;

    private final MovieModelDataMapper mMovieModelDataMapper;

    private final Context mContext;

    static final String KEY_SHARED = "movieFavourite";

    public enum TYPE_MOVIES_LIST{
        HIGH_RATED,
        POPULAR,
        FAVOURITE;
    }

    private TYPE_MOVIES_LIST mTypeMoviesList = TYPE_MOVIES_LIST.HIGH_RATED;

    @Inject
    public SearchMoviePresenter(
            @Named(MoviesModule.HIGH_RATED_MOVIES) UseCase getMoviesHighRatingUseCase,
            @Named(MoviesModule.POPULAR_MOVIES) UseCase getMoviesPopularUseCase,
            @Named(MoviesModule.FAVOURITES) GetFavouriteMoviesUseCase getFavouriteMoviesUseCase,
            MovieModelDataMapper movieModelDataMapper,
            Context context){
        this.mGetMoviesHighRatingUseCase = getMoviesHighRatingUseCase;
        this.mGetMoviesPopularUseCase = getMoviesPopularUseCase;
        this.mGetFavouriteMoviesUseCase = getFavouriteMoviesUseCase;
        this.mMovieModelDataMapper = movieModelDataMapper;

        this.mContext = context;
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
        this.mGetFavouriteMoviesUseCase.unsubscribe();
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
            case FAVOURITE:
                this.mGetFavouriteMoviesUseCase.execute(getFavouriteMovies(),
                        new MovieListSubscriber());
                break;
        }
    }

    private List<Long> getFavouriteMovies(){

        SharedPreferences sharedPreferences = mContext.
                getSharedPreferences(KEY_SHARED, Context.MODE_PRIVATE);
        Map<String,?> sharedData = sharedPreferences.getAll();
        Set<? extends Map.Entry<String, ?>> entrySet =  sharedData.entrySet();
        Iterator<? extends Map.Entry<String, ?>> iterator = entrySet.iterator();

        List<Long> favouriteMovies = new ArrayList<Long>();
        while(iterator.hasNext()){
           Map.Entry<String,Boolean> entry = (Map.Entry<String, Boolean>) iterator.next();
            if(entry.getValue()){
                favouriteMovies.add(Long.parseLong(entry.getKey()));
            }

        }
        return favouriteMovies;
    }

    private void showMoviesCollectionInView(Collection<MovieD> usersCollection) {
        final Collection<MovieModelPresenter> userModelsCollection =
                this.mMovieModelDataMapper.transform(usersCollection);
        this.mMovieViewGridView.renderMoviesList(userModelsCollection);
    }

    private final class MovieListSubscriber extends DefaultSubcriber<List<MovieD>> {

        @Override public void onCompleted() {
            SearchMoviePresenter.this.hideViewLoading();
        }

        @Override public void onError(Throwable e) {
            SearchMoviePresenter.this.hideViewLoading();
            SearchMoviePresenter.this.showErrorMessage(new com.virtu.popularmovies.domain.exception.DefaultErrorBundle((Exception) e));
            SearchMoviePresenter.this.showViewRetry();
        }

        @Override public void onNext(List<MovieD> movies) {
            SearchMoviePresenter.this.showMoviesCollectionInView(movies);
        }
    }


}
