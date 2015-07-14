package com.virtu.popularmovies.data.entity.mapper;

import com.virtu.popularmovies.data.entity.MovieEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by virtu on 09/07/2015.
 */
public class MovieEntityDataMapper {

    @Inject
    public MovieEntityDataMapper(){}

    /**
     * Transform a {@link MovieEntity} into a {@link com.virtu.popularmovies.domain.entities.Movie}
     * @param movieEntity
     * @return
     */
    public com.virtu.popularmovies.domain.entities.Movie transform(MovieEntity movieEntity){
        com.virtu.popularmovies.domain.entities.Movie movie = null;
        movie = new com.virtu.popularmovies.domain.entities.Movie(movieEntity.getId());
        movie.setTitle(movieEntity.getTitle());
        movie.setOverView(movieEntity.getOverView());
        movie.setPosterUrl(movieEntity.getPosterUrl());
        movie.setReleaseDate(movieEntity.getReleaseDate());
        movie.setVote_average(movieEntity.getVote_average());
        return movie;
    }

    /**
     * Transform a list of {@link MovieEntity} into a Collection of {@link com.virtu.popularmovies.domain.entities.Movie}
     * @param movieEntityCollection Collection of {@link MovieEntity} to be converted
     * @return List of {@link com.virtu.popularmovies.domain.entities.Movie} converted
     */
    public List<com.virtu.popularmovies.domain.entities.Movie> transform(Collection<MovieEntity> movieEntityCollection){
        List<com.virtu.popularmovies.domain.entities.Movie> movieList = new ArrayList<>();
        com.virtu.popularmovies.domain.entities.Movie movie;
        for (MovieEntity movieEntity: movieEntityCollection){
            movie = transform(movieEntity);
            if (movie != null) movieList.add(movie);
        }
        return movieList;
    }
}
