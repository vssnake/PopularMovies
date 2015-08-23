package com.virtu.popularmovies.data.repository.datasource;

import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;
import com.virtu.popularmovies.data.exception.MovieNotFoundException;
import com.virtu.popularmovies.data.net.RestApi;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by virtu on 09/07/2015.
 */
public class CloudMovieDataStore implements MovieDataStore {

    private final RestApi mRestApi;

    //Primitive cache
    private List<MovieEntity> mCachePopularMovies;
    private List<MovieEntity> mCacheHighRatedMovies;

    public CloudMovieDataStore(RestApi restApi){
        this.mRestApi = restApi;
        mCacheHighRatedMovies = new ArrayList<>();
        mCachePopularMovies = new ArrayList<>();
    }

    private final Action1<List<MovieEntity>> saveToCachePopularMovies = new Action1<List<MovieEntity>>() {
        @Override
        public void call(List<MovieEntity> movieEntities) {
            CloudMovieDataStore.this.mCachePopularMovies = movieEntities;
        }
    };

    private final Action1<List<MovieEntity>> saveToCacheHighRatedMovies = new Action1<List<MovieEntity>>() {
        @Override
        public void call(List<MovieEntity> movieEntities) {
            CloudMovieDataStore.this.mCacheHighRatedMovies = movieEntities;
        }
    };

    @Override
    public Observable<List<MovieEntity>> getPopularMoviesDesc() {
        //Primitive Imp of cache
        if (mCachePopularMovies.size()> 0){
            return Observable.create(new Observable.OnSubscribe<List<MovieEntity>>() {
                @Override
                public void call(Subscriber<? super List<MovieEntity>> subscriber) {
                    subscriber.onNext(mCachePopularMovies);
                    subscriber.onCompleted();
                }
            });
        }
        return this.mRestApi.getMovieEntityListPopularityDESC().doOnNext(saveToCachePopularMovies);
    }

    @Override
    public Observable<List<MovieEntity>> getHighRatedMoviesDesc() {
        //Primitive Imp of cache
        if (mCacheHighRatedMovies.size()> 0){
            return Observable.create(new Observable.OnSubscribe<List<MovieEntity>>() {
                @Override
                public void call(Subscriber<? super List<MovieEntity>> subscriber) {
                    subscriber.onNext(mCacheHighRatedMovies);
                    subscriber.onCompleted();
                }
            });
        }
        return this.mRestApi.getMovieEntityListVoteAverageDesc().doOnNext(saveToCacheHighRatedMovies);
    }

    @Override
    public Observable<MovieEntity> getMovie(final Long id) {
        return Observable.create(new Observable.OnSubscribe<MovieEntity>() {
            @Override
            public void call(Subscriber<? super MovieEntity> subscriber) {
                MovieEntity movieEntity = searchMovieInCache(id);
                if (movieEntity == null){
                    subscriber.onError(new MovieNotFoundException("Not found movie in cache"));
                }else{
                    subscriber.onNext(movieEntity);
                }
                subscriber.onCompleted();
            }
        });

    }

    @Override
    public Observable<List<ReviewMovieEntity>> getReview(Long movieID) {

        //TODO Cache Module
        return this.mRestApi.getReviewMovie(movieID);
    }

    @Override
    public Observable<List<VideoMovieEntity>> getVideo(Long movieID) {

        //TODO Cache Module
        return this.mRestApi.getVideosMovie(movieID);
    }

    @Override
    public Observable<MovieEntity> markStarred(Long movieID) {
        return null;
    }

    ;

    /**
     * Primitive implementation of cache
     * @param id
     * @return
     */
    public MovieEntity searchMovieInCache(Long id){
        MovieEntity movieEntity = null;
        for (int x = 0; x<mCacheHighRatedMovies.size();x++){
            if(mCacheHighRatedMovies.get(x).getId().equals(id)){
                movieEntity = mCacheHighRatedMovies.get(x);
                break;
            }
        }
        if (movieEntity == null){
            for (int x = 0; x<mCachePopularMovies.size();x++){
                if(mCachePopularMovies.get(x).getId().equals(id)){
                    movieEntity = mCachePopularMovies.get(x);
                    break;
                }
            }
        }
        return movieEntity;

    }
}
