package com.virtu.popularmovies.data.repository.datasource;

import com.google.gson.Gson;
import com.virtu.popularmovies.data.cache.MovieCache;
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
import rx.functions.Func2;
import rx.functions.Func3;

/**
 * Created by virtu on 09/07/2015.
 */
public class CloudMovieDataStore implements MovieDataStore {

    private final RestApi mRestApi;

    //Primitive cache
    private List<MovieEntity> mCachePopularMovies;
    private List<MovieEntity> mCacheHighRatedMovies;

    private final MovieCache mMovieCache;

    public CloudMovieDataStore(RestApi restApi,MovieCache movieCache){
        this.mRestApi = restApi;
        mCacheHighRatedMovies = new ArrayList<>();
        mCachePopularMovies = new ArrayList<>();
        this.mMovieCache = movieCache;
    }

    private final Action1<List<MovieEntity>> saveMoviesToCache = new Action1<List<MovieEntity>>() {
        @Override
        public void call(List<MovieEntity> movieEntities) {
            for (MovieEntity movieEntity : movieEntities){
                if(!mMovieCache.isCached(movieEntity.getId())){
                    mMovieCache.put(movieEntity,movieEntity.getId());
                }
            }

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
        /*if (mCachePopularMovies.size()> 0){
            return Observable.create(new Observable.OnSubscribe<List<MovieEntity>>() {
                @Override
                public void call(Subscriber<? super List<MovieEntity>> subscriber) {
                    subscriber.onNext(mCachePopularMovies);
                    subscriber.onCompleted();
                }
            });
        }*/
        return this.mRestApi.getMovieEntityListPopularityDESC().doOnNext(saveMoviesToCache);
    }

    @Override
    public Observable<List<MovieEntity>> getHighRatedMoviesDesc() {
        //Primitive Imp of cache
        /*if (mCacheHighRatedMovies.size()> 0){
            return Observable.create(new Observable.OnSubscribe<List<MovieEntity>>() {
                @Override
                public void call(Subscriber<? super List<MovieEntity>> subscriber) {
                    subscriber.onNext(mCacheHighRatedMovies);
                    subscriber.onCompleted();
                }
            });
        }*/
        return this.mRestApi.getMovieEntityListVoteAverageDesc().doOnNext(saveMoviesToCache);
    }

    @Override
    public Observable<List<MovieEntity>> getFavourites(final List<Long> idMovies) {
        return Observable.create(new Observable.OnSubscribe<List<MovieEntity>>() {
            @Override
            public void call(Subscriber<? super List<MovieEntity>> subscriber) {
                List<MovieEntity> movieEntities = new ArrayList<MovieEntity>();
                for (Long idMovie: idMovies){
                    if (mMovieCache.isCached(idMovie)){
                        movieEntities.add(mMovieCache.get(idMovie));
                    }

                }
                subscriber.onNext(movieEntities);
                subscriber.onCompleted();
            }
        });

    }

    @Override
    public void getMovie(final Long id,final Func3<MovieEntity, List<ReviewMovieEntity>,
            List<VideoMovieEntity>, Void> callback){

        if (mMovieCache.isCached(id)){
            mMovieCache.get(id, new Func3<MovieEntity, List<ReviewMovieEntity>, List<VideoMovieEntity>, Void>() {
                @Override
                public Void call(final MovieEntity movieEntity,
                                 List<ReviewMovieEntity> reviewMovieEntities,
                                 List<VideoMovieEntity> videoMovieEntities) {
                    if (reviewMovieEntities == null || videoMovieEntities == null){
                        MovieObservable movieObservable = new MovieObservable();

                        movieObservable.returned = new Func2<List<ReviewMovieEntity>,
                                List<VideoMovieEntity>, Void>() {
                            @Override
                            public Void call(List<ReviewMovieEntity> reviewMovieEntities,
                                             List<VideoMovieEntity> videoMovieEntities) {
                                    for (ReviewMovieEntity review:reviewMovieEntities) {
                                        mMovieCache.put(id,review);
                                    }
                                    for (VideoMovieEntity video:videoMovieEntities) {
                                        mMovieCache.put(id,video);
                                    }
                                callback.call(movieEntity,reviewMovieEntities,videoMovieEntities);

                                return null;
                            }
                        };
                        getReview(id).subscribe(movieObservable.actionReview);
                        getVideo(id).subscribe(movieObservable.actionVideo);
                    }else{
                        callback.call(movieEntity,reviewMovieEntities,videoMovieEntities);
                    }
                    return null;
                }
            });
        }else{
            callback.call(null,null,null);
        }

       /* return Observable.create(new Observable.OnSubscribe<MovieEntity>() {
            @Override
            public void call(Subscriber<? super MovieEntity> subscriber) {
                MovieEntity movieEntity = searchMovieInCache(id);
                if (movieEntity == null) {
                    subscriber.onError(new MovieNotFoundException("Not found movie in cache"));
                } else {
                    subscriber.onNext(movieEntity);
                }
                subscriber.onCompleted();
            }
        });*/

    }

    @Override
    public Observable<List<ReviewMovieEntity>> getReview(Long movieID) {

        return this.mRestApi.getReviewMovie(movieID);
    }

    @Override
    public Observable<List<VideoMovieEntity>> getVideo(Long movieID) {

        return this.mRestApi.getVideosMovie(movieID);
    }

    @Override
    public Observable<Boolean> markStarred(final Long movieID,final boolean starred){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (mMovieCache.isCached(movieID)){
                    mMovieCache.markStarred(movieID, starred);
                    subscriber.onNext(starred);

                }else{
                    subscriber.onError(new MovieNotFoundException("MovieD not found in Cachew"));
                }
            }
        });

    }


    class MovieObservable{
        List<ReviewMovieEntity> m2 = null;
        List<VideoMovieEntity> m3 = null;
        public Func2<List<ReviewMovieEntity>, List<VideoMovieEntity>,Void> returned = null;


        public Action1<List<ReviewMovieEntity>> actionReview =  new Action1<List<ReviewMovieEntity>>() {
            @Override
            public void call(List<ReviewMovieEntity> reviewMovieEntities) {
                m2 = reviewMovieEntities;
                check();
            }
        };
        public Action1<List<VideoMovieEntity>> actionVideo = new Action1<List<VideoMovieEntity>>() {
            @Override
            public void call(List<VideoMovieEntity> videoMovieEntities) {
                m3 = videoMovieEntities;
                check();
            }
        };

        private void check(){
            if (m2!= null && m3!= null && returned != null){
                returned.call(m2,m3);
            }
        }

    }

    /**
     * Primitive implementation of cache
     * @param id
     * @return
     */
    @Deprecated
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
