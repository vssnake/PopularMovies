package com.virtu.popularmovies.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.virtu.popularmovies.data.entity.MovieEntity;
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
            public void call(Subscriber<? super List<MovieEntity>> subscriber) {
                if (isInternetConnection()){
                        String response = getHighPopularityMoviesFromApi();

                    if (response != null){
                        try {
                            subscriber.onNext(mMovieEntityJsonMapper
                                    .transformMovieEntityCollection(response));
                        } catch (JSONException e) {
                            subscriber.onError(e);
                        }

                    }else{
                        subscriber.onError(new NetworkConnectionException());
                    }

                }else{
                    subscriber.onError(new NetworkConnectionException());
                }
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<MovieEntity>> getMovieEntityListVoteAverageDesc() {
        return Observable.create(new Observable.OnSubscribe<List<MovieEntity>>() {
            @Override
            public void call(Subscriber<? super List<MovieEntity>> subscriber) {
                if (isInternetConnection()){
                    String response = getHighRatedMoviesFromApi();

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

                }else{
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    private String getHighRatedMoviesFromApi(){
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

        return mApiBridge.requestSyncCall();
    }

    private String getHighPopularityMoviesFromApi(){
        mApiBridge.setURL(API_BASE_URL +
                API_URL_GET_MOVIE_LIST +
                API_QUERY_KEY +
                API_QUERY_SORT_BY_POPULARITY_DESC_VALUE +
                API_CONCAT_QUERY +
                API_KEY_NAME +
                API_KEY_VALUE);

        return mApiBridge.requestSyncCall();
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
