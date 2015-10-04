package com.virtu.popularmovies.domain.repository;

import com.virtu.popularmovies.domain.entities.MovieD;

import java.util.List;

import rx.Observable;

/**
 * Created by virtu on 25/06/2015.
 */
public interface MoviesRepository {

    Observable<List<MovieD>> getPopularMoviesDesc();

    Observable<MovieD> getMovie(Long id,Boolean favourite);

    Observable<List<MovieD>> getFavouriteMovies(List<Long> ids);

    Observable<List<MovieD>> getHighestRatedMoviesDesc();

    Observable<Boolean> markStarred(long movieId,boolean starred);
}
