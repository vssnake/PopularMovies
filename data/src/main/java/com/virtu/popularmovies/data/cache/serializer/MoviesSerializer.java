package com.virtu.popularmovies.data.cache.serializer;

import com.google.gson.Gson;
import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by virtu on 24/08/2015.
 * Class to serialize and Deserializer MovieD,Review and Video Entities
 */
@Singleton
public class MoviesSerializer {

    private final Gson gson = new Gson();

    @Inject
    public MoviesSerializer(){};

    /**
     *
     * @param movieEntity {@link MovieEntity} to serialize
     * @return
     */
    public String serialize(MovieEntity movieEntity){
        String jsonString = gson.toJson(movieEntity, MovieEntity.class);
        return jsonString;
    }

    /**
     *
     * @param videoMovieEntity {@link VideoMovieEntity} to serialize
     * @return
     */
    public String serialize(VideoMovieEntity videoMovieEntity){
        String jsonString = gson.toJson(videoMovieEntity, VideoMovieEntity.class);
        return jsonString;
    }

    /**
     *
     * @param reviewMovieEntity {@link ReviewMovieEntity} to serialize
     * @return
     */
    public String serialize(ReviewMovieEntity reviewMovieEntity){
        String jsonString = gson.toJson(reviewMovieEntity, ReviewMovieEntity.class);
        return jsonString;
    }

    /**
     * Deserialize a json to Object
     * @param jsonString A json to deserialize
     * @param typeClass The  class you want to  deserialize
     * @param <T> The type of class you want deserialize
     * @return Deserialize class
     */
    public MovieEntity deserializeMovie(String jsonString){
        MovieEntity entity = gson.fromJson(jsonString, MovieEntity.class);
        return entity;
    }

    public ReviewMovieEntity deserializeReview(String jsonString){
        ReviewMovieEntity entity = gson.fromJson(jsonString, ReviewMovieEntity.class);
        return entity;
    }

    public VideoMovieEntity deserializeVideo(String jsonString){
        VideoMovieEntity entity = gson.fromJson(jsonString, VideoMovieEntity.class);
        return entity;
    }

}
