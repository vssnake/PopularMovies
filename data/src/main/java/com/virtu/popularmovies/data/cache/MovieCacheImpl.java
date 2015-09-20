package com.virtu.popularmovies.data.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.format.Time;
import android.util.Log;

import com.virtu.popularmovies.data.cache.serializer.MoviesSerializer;
import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;
import com.virtu.popularmovies.data.repository.provider.MoviesContract;
import com.virtu.popularmovies.data.repository.provider.utils.ProviderUtilities;
import com.virtu.popularmovies.domain.executor.ThreadExecutor;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.functions.Func3;

/**
 * Created by virtu on 02/09/2015.
 */
@Singleton
public class MovieCacheImpl implements MovieCache {

    private static String LOG_TAG = MovieCacheImpl.class.getSimpleName();

    private final Context mContext;
    private final MoviesSerializer mMoviesSerializer;
    private final ThreadExecutor mThreadExecutor;

    @Inject
    public MovieCacheImpl(Context context,
                          MoviesSerializer moviesSerializer,
                          ThreadExecutor threadExecutor){
        mContext = context;
        mMoviesSerializer = moviesSerializer;
        mThreadExecutor = threadExecutor;
    }

    @Override
    public boolean isCached(long movieID) {

        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.MovieEntry.buildMovieUri(movieID),
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()){
            return true;
        }
        return false;
    }



    @Override
    //Only use if the cache is empty
    public void put(MovieEntity movieEntity,
                    long checkingID) {

        if (!checkMovieCacheTime(checkingID)){
            ContentValues movieValues =ProviderUtilities.createMovies(movieEntity, 0, mMoviesSerializer);

            mContext.getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI,movieValues);
        }

    }

    @Override
    //Only use if the cache is empty
    public void put(long movieID, ReviewMovieEntity reviewMovieEntity) {

        ContentValues reviewValues =ProviderUtilities.createReview(reviewMovieEntity,
                movieID, mMoviesSerializer);

        mContext.getContentResolver().insert(MoviesContract.ReviewEntry.buildReviewUri(movieID), reviewValues);
    }

    @Override
    //Only use if the cache is empty
    public void put(long movieID, VideoMovieEntity videoMovieEntity) {

        ContentValues videoValues =ProviderUtilities.createVideo(videoMovieEntity,
                movieID, mMoviesSerializer);

        mContext.getContentResolver().insert(MoviesContract.VideoEntry.buildVideoUri(movieID), videoValues);
    }

    @Override
    public void get(final long movieId,final Func3<MovieEntity, List<ReviewMovieEntity>, List<VideoMovieEntity>, Void> callback) {

        runAsyncTask(new Runnable() {
            @Override
            public void run() {


                Cursor movieCursor = mContext.getContentResolver().query(
                        MoviesContract.MovieEntry.buildMovieUri(movieId),
                        null,
                        null,
                        null,
                        null);

                /*if (checkMovieCacheTime(movieCursor, true)) {
                    callback.call(null, null, null);
                    return;
                }*/

                MovieEntity movieEntity =
                        ProviderUtilities.getMovieFromCursor(movieCursor, mMoviesSerializer);

                Cursor reviewCursor = mContext.getContentResolver().query(
                        MoviesContract.ReviewEntry.buildReviewUri(movieId),
                        null,
                        null,
                        null,
                        null);
                List<ReviewMovieEntity> reviewEntityList =
                        ProviderUtilities.getListReviewsFromCursor(reviewCursor, mMoviesSerializer);

                Cursor videoCursor = mContext.getContentResolver().query(
                        MoviesContract.VideoEntry.buildVideoUri(movieId),
                        null,
                        null,
                        null,
                        null);
                List<VideoMovieEntity> videosEntityList =
                        ProviderUtilities.getListVideosFromCursor(videoCursor, mMoviesSerializer);

                callback.call(movieEntity, reviewEntityList, videosEntityList);
            }
        });




    }

    @Override
    public MovieEntity get(long movieId) {
        Cursor movieCursor = mContext.getContentResolver().query(
                MoviesContract.MovieEntry.buildMovieUri(movieId),
                null,
                null,
                null,
                null);

                /*if (checkMovieCacheTime(movieCursor, true)) {
                    callback.call(null, null, null);
                    return;
                }*/

        MovieEntity movieEntity =
                ProviderUtilities.getMovieFromCursor(movieCursor, mMoviesSerializer);
        return movieEntity;
    }

    @Override
    public void markStarred(long movieId, boolean starred) {
        ContentValues updateValues = new ContentValues();
        //Update data
        updateValues.put(MoviesContract.MovieEntry._ID,movieId);
        updateValues.put(MoviesContract.MovieEntry.COLUMN_FAVOURITE, 0);

        mContext.getContentResolver().update(MoviesContract.MovieEntry.buildMovieUri(movieId),
                updateValues,
                null,
                null);
    }

    private boolean checkMovieCacheTime(long movieId){
        Cursor movieCursor = mContext.getContentResolver().query(
                MoviesContract.MovieEntry.buildMovieUri(movieId),
                null,
                null,
                null,
                null);

        return checkMovieCacheTime(movieCursor,true);

    }


    private boolean checkMovieCacheTime(Cursor movieCursor,boolean eraseIfExpired){


        if (!movieCursor.moveToFirst()){
            Log.d(LOG_TAG,"In CheckMovieCacheTime -- Cursor haven´t got any rows");
            return false;
        }

        int creationTimeDateColumn = movieCursor.getColumnIndex(MoviesContract.
                MovieEntry.COLUMN_EXPIRATION_CACHE_DATE);
        int movieKeyColumn = movieCursor.getColumnIndex(MoviesContract.MovieEntry._ID);
        long movieId = movieCursor.getLong(movieKeyColumn);
        long creationTimeDate = movieCursor.getLong(creationTimeDateColumn);
        Time now = new Time();
        long timeCurrent =  now.toMillis(true);
        long expirationInMinutes = (timeCurrent - creationTimeDate)/1000/60;

        return true;
        //Remove erased need a better implementation with server

       /* if (expirationInMinutes < 30){
            return true;
        }

        if (eraseIfExpired){

            Log.d(LOG_TAG, "Cache Expired ");
            //TODO
            mContext.getContentResolver().delete(MoviesContract.MovieEntry.buildMovieUri(movieId),
                    null,
                    null);
        }
        return false;*/

    }

    private void runAsyncTask(Runnable runnable){
        mThreadExecutor.execute(runnable);
    }
}
