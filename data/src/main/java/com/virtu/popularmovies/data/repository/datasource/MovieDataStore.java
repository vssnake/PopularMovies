package com.virtu.popularmovies.data.repository.datasource;

import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;
import com.virtu.popularmovies.data.exception.MovieNotFoundException;
import com.virtu.popularmovies.data.repository.provider.MoviesContract;

import java.util.List;

import rx.Observable;
import rx.functions.Func3;
import rx.functions.Func4;

/**
 * Created by virtu on 09/07/2015.
 */
public interface MovieDataStore {

    Observable<List<MovieEntity>> getPopularMoviesDesc();

    Observable<List<MovieEntity>> getHighRatedMoviesDesc();

    Observable<List<MovieEntity>> getFavourites(List<Long> idMovies);

    void getMovie(Long id,Boolean favourite,Func3<MovieEntity, List<ReviewMovieEntity>,
                List<VideoMovieEntity>, Void> callback);

    Observable<List<ReviewMovieEntity>> getReview(Long movieID);

    Observable<List<VideoMovieEntity>> getVideo(Long movieID);

    Observable<Boolean> markStarred (Long movieID,boolean starred);


}
