package com.virtu.popularmovies.data.repository.datasource;

import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;
import com.virtu.popularmovies.data.repository.provider.MoviesContract;

import java.util.List;

import rx.Observable;

/**
 * Created by virtu on 09/07/2015.
 */
public interface MovieDataStore {

    Observable<List<MovieEntity>> getPopularMoviesDesc();

    Observable<List<MovieEntity>> getHighRatedMoviesDesc();

    Observable<MovieEntity> getMovie(Long id);

    Observable<List<ReviewMovieEntity>> getReview(Long movieID);

    Observable<List<VideoMovieEntity>> getVideo(Long movieID);

    Observable<MovieEntity> markStarred (Long movieID);


}
