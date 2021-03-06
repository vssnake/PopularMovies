package com.virtu.popularmovies.data.repository.datasource;

import android.content.Context;

import com.virtu.popularmovies.data.cache.MovieCache;
import com.virtu.popularmovies.data.net.RestApi;
import com.virtu.popularmovies.data.net.RestApiImpl;
import com.virtu.popularmovies.data.entity.mapper.MovieEntityJsonMapper;
import com.virtu.popularmovies.data.net.ApiBridge;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by virtu on 09/07/2015.
 */
@Singleton
public class MovieDataStoreFactory {

    private final Context mContext;

    private final MovieCache mMovieCache;

    @Inject
    public MovieDataStoreFactory(Context context,MovieCache movieCache ){
        if (context == null){
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        this.mContext = context.getApplicationContext();
        this.mMovieCache = movieCache;
    }


    public MovieDataStore createCloudDataStore(){
        MovieEntityJsonMapper movieEntityJsonMapper = new MovieEntityJsonMapper();
        RestApi restApi = new RestApiImpl(this.mContext,new ApiBridge(),movieEntityJsonMapper);
        return new CloudMovieDataStore(restApi,mMovieCache);
    }

}
