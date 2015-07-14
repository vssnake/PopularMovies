package com.virtu.popularmovies.presentation.model.mapper;

import com.virtu.popularmovies.presentation.model.MovieModelPresenter;
import com.virtu.popularmovies.presentation.injection.PerActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by virtu on 11/07/2015.
 */

@PerActivity
public class MovieModelDataMapper {

    @Inject
    public MovieModelDataMapper(){}


    public MovieModelPresenter transform(com.virtu.popularmovies.domain.entities.Movie movie){
        if (movie == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        MovieModelPresenter movieModelPresenter = new MovieModelPresenter(movie.getId());

        movieModelPresenter.setTitle(movie.getTitle());
        movieModelPresenter.setOverView(movie.getOverView());
        movieModelPresenter.setPosterUrl(movie.getPosterUrl());
        movieModelPresenter.setReleaseDate(movie.getReleaseDate());
        movieModelPresenter.setVote_average(movie.getVote_average());
        return movieModelPresenter;
    }

    /**
     * Transform a Collection of {@link com.virtu.popularmovies.domain.entities.Movie} into a Collection of {@link MovieModelPresenter}.
     *
     * @param moviesCollection Objects to be transformed.
     * @return List of {@link MovieModelPresenter}.
     */
    public Collection<MovieModelPresenter> transform(Collection<com.virtu.popularmovies.domain.entities.Movie> moviesCollection) {
        Collection<MovieModelPresenter> movieModelsCollection;

        if (moviesCollection != null && !moviesCollection.isEmpty()) {
            movieModelsCollection = new ArrayList<MovieModelPresenter>();
            for (com.virtu.popularmovies.domain.entities.Movie movie : moviesCollection) {
                movieModelsCollection.add(transform(movie));
            }
        } else {
            movieModelsCollection = Collections.emptyList();
        }
        return movieModelsCollection;
    }
}
