package com.virtu.popularmovies.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;
import com.virtu.popularmovies.data.exception.NetworkConnectionException;
import com.virtu.popularmovies.data.entity.mapper.MovieEntityJsonMapper;

import org.json.JSONException;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by virtu on 09/07/2015.
 */
public class RestApiImpl implements  RestApi {

    private final Context mContext;
    private final MovieEntityJsonMapper mMovieEntityJsonMapper;
    private final ApiBridge mApiBridge;

    /**
     *  Constructor of the class
     * @param context
     * @param userEntityJsonMapper
     */
    public RestApiImpl(Context context,
                       ApiBridge apiBridge,
                       MovieEntityJsonMapper userEntityJsonMapper) {
        if (context == null || userEntityJsonMapper == null) {
            throw new IllegalArgumentException("The constructor parameters cannot be null!!!");
        }
        this.mContext = context.getApplicationContext();
        this.mMovieEntityJsonMapper = userEntityJsonMapper;
        this.mApiBridge = apiBridge;
    }

    @Override
    public Observable<List<MovieEntity>> getMovieEntityListPopularityDESC() {
        return Observable.create(new Observable.OnSubscribe<List<MovieEntity>>() {
            @Override
            public void call(final Subscriber<? super List<MovieEntity>> subscriber) {
                if (isInternetConnection()){
                        getHighPopularityMoviesFromApi(new ApiBridge.ResponseStringCallback() {
                            @Override
                            public void onStringResponse(String response) {
                                if (response != null){
                                    try {
                                        subscriber.onNext(mMovieEntityJsonMapper
                                                .transformMovieEntityCollection(response));
                                        subscriber.onCompleted();
                                    } catch (JSONException e) {
                                        subscriber.onError(e);

                                    } finally {
                                        subscriber.onCompleted();
                                    }
                                }else{
                                    subscriber.onError(new NetworkConnectionException());
                                }
                            }
                        });
                }else{
                    subscriber.onError(new NetworkConnectionException());
                }

            }
        });
    }

    @Override
    public Observable<List<MovieEntity>> getMovieEntityListVoteAverageDesc() {
        return Observable.create(new Observable.OnSubscribe<List<MovieEntity>>() {
            @Override
            public void call(final Subscriber<? super List<MovieEntity>> subscriber) {
                if (isInternetConnection()){
                    getHighRatedMoviesFromApi(new ApiBridge.ResponseStringCallback() {
                        @Override
                        public void onStringResponse(String response) {
                            if (response != null){
                                try {
                                    subscriber.onNext(mMovieEntityJsonMapper
                                            .transformMovieEntityCollection(response));
                                    subscriber.onCompleted();
                                } catch (JSONException e) {
                                    subscriber.onError(e);
                                }
                            }else{
                                subscriber.onError(new NetworkConnectionException());
                            }
                        }
                    });
                }else{
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @Override
    public Observable<List<ReviewMovieEntity>> getReviewMovie(final Long idMovie) {
        return Observable.create(new Observable.OnSubscribe<List<ReviewMovieEntity>>() {
            @Override
            public void call(final Subscriber<? super List<ReviewMovieEntity>> subscriber) {
                if (isInternetConnection()){
                    getReviewsFromMovieFromApi(idMovie, new ApiBridge.ResponseStringCallback() {
                        @Override
                        public void onStringResponse(String response) {
                            if (response != null){
                                try {
                                    subscriber.onNext(mMovieEntityJsonMapper
                                            .transformReviewEntityCollection(response));
                                    subscriber.onCompleted();
                                } catch (JSONException e) {
                                    subscriber.onError(e);
                                }
                            }else{
                                subscriber.onError(new NetworkConnectionException());
                            }
                        }
                    });
                }else{
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @Override
    public Observable<List<VideoMovieEntity>> getVideosMovie(final Long idMovie) {
        return Observable.create(new Observable.OnSubscribe<List<VideoMovieEntity>>() {
            @Override
            public void call(final Subscriber<? super List<VideoMovieEntity>> subscriber) {
                if (isInternetConnection()){
                    getVideosFromMovieFromApi(idMovie, new ApiBridge.ResponseStringCallback() {
                        @Override
                        public void onStringResponse(String response) {
                            if (response != null){
                                try {
                                    subscriber.onNext(mMovieEntityJsonMapper
                                            .transformVideosEntityCollection(response));
                                    subscriber.onCompleted();
                                } catch (JSONException e) {
                                    subscriber.onError(e);
                                }
                            }else{
                                subscriber.onError(new NetworkConnectionException());
                            }
                        }
                    });



                }else{
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    private void getHighRatedMoviesFromApi(ApiBridge.ResponseStringCallback callback){
        mApiBridge.setURL(API_BASE_URL +
                API_URL_GET_MOVIE_LIST +
                API_QUERY_KEY +
                API_QUERY_SORT_BY +
                API_QUERY_SORT_BY_VOTE_AVERAGE_DESC_VALUE +
                API_CONCAT_QUERY +
                API_QUERY_SORT_BY +
                API_QUERY_SORT_BY_VOTE_COUNT_DESC_VALUE +
                API_CONCAT_QUERY +
                API_KEY_NAME +
                API_KEY_VALUE);

        mApiBridge.requestAsyncCall(callback);
       // return mApiBridge.requestSyncCall();
    }

    private void getHighPopularityMoviesFromApi(ApiBridge.ResponseStringCallback callback){
        mApiBridge.setURL(API_BASE_URL +
                API_URL_GET_MOVIE_LIST +
                API_QUERY_KEY +
                API_QUERY_SORT_BY_POPULARITY_DESC_VALUE +
                API_CONCAT_QUERY +
                API_KEY_NAME +
                API_KEY_VALUE);

        mApiBridge.requestAsyncCall(callback);
    }

    private void getReviewsFromMovieFromApi(Long idMovie,ApiBridge.ResponseStringCallback callback){
        mApiBridge.setURL(API_BASE_URL +
                API_MOVIE_ +
                "/" + idMovie +
                API_REVIEW_ +
                API_QUERY_KEY +
                API_KEY_NAME +
                API_KEY_VALUE);

        mApiBridge.requestAsyncCall(callback);
    }

    private void getVideosFromMovieFromApi(Long idMovie,ApiBridge.ResponseStringCallback callback){
        mApiBridge.setURL(API_BASE_URL +
                API_MOVIE_ +
                "/" + idMovie +
                API_VIDEO_ +
                API_QUERY_KEY +
                API_KEY_NAME +
                API_KEY_VALUE);

        mApiBridge.requestAsyncCall(callback);
    }



    /**
     * Checks if the device has any active internet connection.
     *
     * @return true device with internet connection, otherwise false.
     */
    private boolean isInternetConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }
}
