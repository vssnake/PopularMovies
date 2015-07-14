package com.virtu.popularmovies.domain.repository;

import java.util.List;

import rx.Observable;

/**
 * Created by virtu on 25/06/2015.
 */
public interface MoviesRepository {

    Observable<List<com.virtu.popularmovies.domain.entities.Movie>> getPopularMoviesDesc();

    Observable<com.virtu.popularmovies.domain.entities.Movie> getMovie(Long id);

    Observable<List<com.virtu.popularmovies.domain.entities.Movie>> getHighestRatedMoviesDesc();
}
