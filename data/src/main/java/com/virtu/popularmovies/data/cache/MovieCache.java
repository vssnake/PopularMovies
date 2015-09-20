package com.virtu.popularmovies.data.cache;

import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;

import java.util.List;

import rx.functions.Func3;

/**
 * Created by virtu on 24/08/2015.
 */
public interface MovieCache {

    boolean isCached(final long movieID);

    void put(MovieEntity movieEntity,
             long checkingID);

    void put(long movieID,ReviewMovieEntity reviewMovieEntity);

    void put(long movieID,VideoMovieEntity movieEntity);

    void get(final long movieId,Func3<MovieEntity,
            List<ReviewMovieEntity>,List<VideoMovieEntity>,Void> callback);

    MovieEntity get (final long movieId);

    void markStarred(final long movieId, boolean starred);
}
