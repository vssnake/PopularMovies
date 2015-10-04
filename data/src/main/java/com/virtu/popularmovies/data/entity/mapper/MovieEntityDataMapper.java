package com.virtu.popularmovies.data.entity.mapper;

import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;
import com.virtu.popularmovies.domain.entities.MovieD;
import com.virtu.popularmovies.domain.entities.Review;
import com.virtu.popularmovies.domain.entities.Video;

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
     * Transform a {@link MovieEntity} into a {@link MovieD}
     * @param movieEntity
     * @return
     */
    public MovieD transform(MovieEntity movieEntity){
        MovieD movie = null;
        movie = new MovieD(movieEntity.getId());
        movie.setTitle(movieEntity.getTitle());
        movie.setOverView(movieEntity.getOverView());
        movie.setPosterUrl(movieEntity.getPosterUrl());
        movie.setReleaseDate(movieEntity.getReleaseDate());
        movie.setVote_average(movieEntity.getVote_average());
        return movie;
    }
    /**
     * Transform a {@link MovieEntity} into a {@link MovieD}
     * @param movieEntity
     * @return
     */
    public MovieD transform(
            MovieEntity movieEntity,
            List<VideoMovieEntity> videosEntities,
            List<ReviewMovieEntity> reviewMovieEntities){
        MovieD movie = null;
        movie = new MovieD(movieEntity.getId());
        movie.setTitle(movieEntity.getTitle());
        movie.setOverView(movieEntity.getOverView());
        movie.setPosterUrl(movieEntity.getPosterUrl());
        movie.setReleaseDate(movieEntity.getReleaseDate());
        movie.setVote_average(movieEntity.getVote_average());

        if (videosEntities != null){
            List<Video> videos = new ArrayList<Video>();
            for (VideoMovieEntity videoMovieEntity: videosEntities){
                Video video = new Video();
                video.setName(videoMovieEntity.getName());
                video.setKeyVideo(videoMovieEntity.getKey());
                video.setSite(videoMovieEntity.getSite());
                video.setType(videoMovieEntity.getTypeVideo());
                videos.add(video);
            }
            movie.setVideos(videos);
        }
        if (reviewMovieEntities != null){
            List<Review> reviews = new ArrayList<Review>();
            for (ReviewMovieEntity reviewMovieEntity: reviewMovieEntities){
                Review review = new Review();
                review.setAuthor(reviewMovieEntity.getAuthor());
                review.setContent(reviewMovieEntity.getContent());
                reviews.add(review);
            }
            movie.setReviews(reviews);
        }




        return movie;
    }

    /**
     * Transform a list of {@link MovieEntity} into a Collection of {@link MovieD}
     * @param movieEntityCollection Collection of {@link MovieEntity} to be converted
     * @return List of {@link MovieD} converted
     */
    public List<MovieD> transform(Collection<MovieEntity> movieEntityCollection){
        List<MovieD> movieList = new ArrayList<>();
        MovieD movie;
        for (MovieEntity movieEntity: movieEntityCollection){
            movie = transform(movieEntity);
            if (movie != null) movieList.add(movie);
        }
        return movieList;
    }
}
