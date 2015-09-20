package com.virtu.popularmovies.data.repository;

import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;
import com.virtu.popularmovies.data.entity.mapper.MovieEntityDataMapper;
import com.virtu.popularmovies.data.exception.MovieNotFoundException;
import com.virtu.popularmovies.data.repository.datasource.MovieDataStore;
import com.virtu.popularmovies.data.repository.datasource.MovieDataStoreFactory;
import com.virtu.popularmovies.domain.entities.MovieD;
import com.virtu.popularmovies.domain.repository.MoviesRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func3;

/**
 * Created by virtu on 08/07/2015.
 */
@Singleton
public class MoviesDataRepository implements MoviesRepository {


    private final MovieEntityDataMapper mMovieEntityDataMapper;
    private final MovieDataStoreFactory mMovieDataStoreFactory;
    private final MovieDataStore mMovieDataStore;

    private final Func1<List<MovieEntity>, List<MovieD>> moviesListEntityMapper =
            new Func1<List<MovieEntity>, List<MovieD>>() {
                @Override public List<MovieD> call(List<MovieEntity> movieEntities) {
                    return MoviesDataRepository.this.mMovieEntityDataMapper.transform(movieEntities);
                }
            };

    private final Func1<MovieEntity, MovieD>
            movieEntityMapper = new Func1<MovieEntity, MovieD>() {
        @Override public MovieD call(MovieEntity movieEntity) {
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
    public Observable<List<MovieD>> getPopularMoviesDesc() {
        return mMovieDataStore.getPopularMoviesDesc().map(moviesListEntityMapper);
    }

    @Override
    public Observable<MovieD> getMovie(final Long id) {
        return Observable.create(new Observable.OnSubscribe<MovieD>() {
            @Override
            public void call(final Subscriber<? super MovieD> subscriber) {
                mMovieDataStore.getMovie(id, new Func3<MovieEntity, List<ReviewMovieEntity>,
                        List<VideoMovieEntity>, Void>() {
                    @Override
                    public Void call(MovieEntity movieEntity,
                                     List<ReviewMovieEntity> reviewMovieEntities,
                                     List<VideoMovieEntity> videoMovieEntities) {
                        if (movieEntity == null ||
                                reviewMovieEntities == null ||
                                videoMovieEntities == null) {
                            subscriber.onError(new MovieNotFoundException("Error getting a movie"));
                        }else{
                            MovieD movie = MoviesDataRepository.this.mMovieEntityDataMapper.transform(
                                    movieEntity,
                                    videoMovieEntities,
                                    reviewMovieEntities);
                            subscriber.onNext(movie);
                        }

                        return null;
                    }
                });
            }
        });
        //return mMovieDataStore.getMovie(id).map(movieEntityMapper);
    }

    @Override
    public Observable<List<MovieD>> getFavouriteMovies(List<Long> ids) {
        return mMovieDataStore.getFavourites(ids).map(moviesListEntityMapper);
    }


    @Override
    public Observable<List<MovieD>> getHighestRatedMoviesDesc() {
        return mMovieDataStore.getHighRatedMoviesDesc().map(moviesListEntityMapper);
    }

    @Override
    public Observable<Boolean> markStarred(long movieId,boolean starred) {
        return mMovieDataStore.markStarred(movieId,starred);
    }
}
