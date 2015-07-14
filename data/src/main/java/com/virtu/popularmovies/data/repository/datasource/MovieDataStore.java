package com.virtu.popularmovies.data.repository.datasource;

import com.virtu.popularmovies.data.entity.MovieEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by virtu on 09/07/2015.
 */
public interface MovieDataStore {

    Observable<List<MovieEntity>> getPopularMoviesDesc();

    Observable<List<MovieEntity>> getHighRatedMoviesDesc();

    Observable<MovieEntity> getMovie(Long id);


}
