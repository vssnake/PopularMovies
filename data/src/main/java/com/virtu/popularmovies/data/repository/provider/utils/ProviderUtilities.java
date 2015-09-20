package com.virtu.popularmovies.data.repository.provider.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.Time;

import com.virtu.popularmovies.data.cache.serializer.MoviesSerializer;
import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;
import com.virtu.popularmovies.data.repository.provider.MoviesContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by virtu on 02/09/2015.
 */
public class ProviderUtilities {

    public static ContentValues createMovies(MovieEntity movieEntity,
                                             int favourite,
                                             MoviesSerializer serializer){
        ContentValues movieValues = new ContentValues();

        movieValues.put(MoviesContract.MovieEntry._ID,movieEntity.getId());
        movieValues.put(MoviesContract.MovieEntry.COLUMN_FAVOURITE,favourite);

        String movieResult = serializer.serialize(movieEntity);
        movieValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_DATA, movieResult);

        Time now = new Time();
        movieValues.put(MoviesContract.MovieEntry.COLUMN_EXPIRATION_CACHE_DATE,now.toMillis(true));

        return movieValues;
    }

    public static ContentValues createReview(ReviewMovieEntity reviewMovieEntity,
                                             long idMovie,
                                             MoviesSerializer serializer){

        ContentValues reviewValues = new ContentValues();

        reviewValues.put(MoviesContract.ReviewEntry.COLUMN_MOVIE_KEY,idMovie);

        String reviewResult = serializer.serialize(reviewMovieEntity);
        reviewValues.put(MoviesContract.ReviewEntry.COLUMN_REVIEW_DATA,reviewResult);

        return reviewValues;
    }

    public static ContentValues createVideo(VideoMovieEntity videoMovieEntity,
                                            long idMovie,
                                            MoviesSerializer serializer){
        ContentValues videoValues = new ContentValues();

        videoValues.put(MoviesContract.VideoEntry.COLUMN_MOVIE_KEY,idMovie);

        String videoResult = serializer.serialize(videoMovieEntity);
        videoValues.put(MoviesContract.VideoEntry.COLUMN_VIDEO_DATA,videoResult);

        return videoValues;
    }

    public static List<ReviewMovieEntity> getListReviewsFromCursor(Cursor reviewMovieCursor,
            MoviesSerializer serializer){

        if (!reviewMovieCursor.moveToFirst()) return null;

        int reviewDataColumn = reviewMovieCursor.getColumnIndex(MoviesContract.ReviewEntry.COLUMN_REVIEW_DATA);

        List<ReviewMovieEntity> listReviews = new ArrayList<ReviewMovieEntity>();
        do {
            String jsonData = reviewMovieCursor.getString(reviewDataColumn);
            listReviews.add(serializer.deserializeReview(jsonData));

        }while(reviewMovieCursor.moveToNext());

        return listReviews;
    }

    public static List<VideoMovieEntity> getListVideosFromCursor(Cursor videosMovieCursor,
                                                         MoviesSerializer serializer){

        if (!videosMovieCursor.moveToFirst()) return null;

        int videoDataColumn = videosMovieCursor.getColumnIndex(MoviesContract.VideoEntry.COLUMN_VIDEO_DATA);

        List<VideoMovieEntity> listVideos = new ArrayList<VideoMovieEntity>();
        do {
            String jsonData = videosMovieCursor.getString(videoDataColumn);
            listVideos.add(serializer.deserializeVideo(jsonData));

        }while(videosMovieCursor.moveToNext());

        return listVideos;
    }

    public static MovieEntity getMovieFromCursor(Cursor movieCursor,
                                                         MoviesSerializer serializer){

        if (!movieCursor.moveToFirst() || movieCursor.getCount() > 1) return null;

        int movieDataColumn = movieCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_DATA);

        String jsonData = movieCursor.getString(movieDataColumn);
        return serializer.deserializeMovie(jsonData);

    }
}
