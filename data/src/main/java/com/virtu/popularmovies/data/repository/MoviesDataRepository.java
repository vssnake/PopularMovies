package com.virtu.popularmovies.data.repository;

import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.mapper.MovieEntityDataMapper;
import com.virtu.popularmovies.data.repository.datasource.MovieDataStore;
import com.virtu.popularmovies.data.repository.datasource.MovieDataStoreFactory;
import com.virtu.popularmovies.domain.entities.Movie;
import com.virtu.popularmovies.domain.repository.MoviesRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by virtu on 08/07/2015.
 */
@Singleton
public class MoviesDataRepository implements MoviesRepository {


    private final MovieEntityDataMapper mMovieEntityDataMapper;
    private final MovieDataStoreFactory mMovieDataStoreFactory;
    private final MovieDataStore mMovieDataStore;

    private final Func1<List<MovieEntity>, List<Movie>> moviesListEntityMapper =
            new Func1<List<MovieEntity>, List<Movie>>() {
                @Override public List<Movie> call(List<MovieEntity> movieEntities) {
                    return MoviesDataRepository.this.mMovieEntityDataMapper.transform(movieEntities);
                }
            };

    private final Func1<MovieEntity, Movie>
            movieEntityMapper = new Func1<MovieEntity, Movie>() {
        @Override public Movie call(MovieEntity movieEntity) {
            return MoviesDataRepository.this.mMovieEntityDataMapper.transform(movieEntity);
        }
    };

    /**
     * Contructor with {@link MoviesRepository}
     * @param movieDataStoreFactory
     * @param movieEntityDataMapper
     */
    @Inject
    public MoviesDataRepository(MovieDataStoreFactory movieDataStoreFactory,
                                MovieEntityDataMapper movieEntityDataMapper){
        this.mMovieEntityDataMapper = movieEntityDataMapper;
        this.mMovieDataStore = movieDataStoreFactory.createCloudDataStore();
        this.mMovieDataStoreFactory = movieDataStoreFactory;


    }

    @Override
    public Observable<List<Movie>> getPopularMoviesDesc() {
        return mMovieDataStore.getPopularMoviesDesc().map(moviesListEntityMapper);
    }

    @Override
    public Observable<Movie> getMovie(Long id) {
        return mMovieDataStore.getMovie(id).map(movieEntityMapper);
    }

    @Override
    public Observable<List<Movie>> getHighestRatedMoviesDesc() {
        return mMovieDataStore.getHighRatedMoviesDesc().map(moviesListEntityMapper);
    }
}
