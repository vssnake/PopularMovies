package com.virtu.popularmovies.data.repository.datasource;

import com.virtu.popularmovies.data.ApplicationTestCase;
import com.virtu.popularmovies.data.cache.MovieCache;
import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.net.RestApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Created by unai on 21/08/15.
 */
public class CloudMovieDataStoreTest extends ApplicationTestCase {

    private static final long FAKE_MOVIE_ID = 12345678;

    private CloudMovieDataStore cloudMovieDataStore;

    @Mock
    private RestApi mockRestApi;

    @Mock
    private MovieCache mockMovieCache;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        cloudMovieDataStore = new CloudMovieDataStore(mockRestApi,mockMovieCache);
    }

    @Test
    public void testGetHighRatedMoviesFromApi(){
        List<MovieEntity> movieEntityList = new ArrayList<>();
        movieEntityList.add(new MovieEntity());
        given(mockRestApi.getMovieEntityListVoteAverageDesc()).willReturn(Observable.just(movieEntityList));
                cloudMovieDataStore.getHighRatedMoviesDesc();
        verify(mockRestApi).getMovieEntityListVoteAverageDesc();
    }


    @Test
    public void testGetPopularMoviesFromApi(){
        List<MovieEntity> movieEntityList = new ArrayList<>();
        movieEntityList.add(new MovieEntity());
        given(mockRestApi.getMovieEntityListPopularityDESC()).willReturn(Observable.just(movieEntityList));
        cloudMovieDataStore.getPopularMoviesDesc();
        verify(mockRestApi).getMovieEntityListPopularityDESC();

    }

    @Test
    public void testGetMovieFromApi(){
        //cloudMovieDataStore.getMovie(FAKE_MOVIE_ID);
        //verify(cloudMovieDataStore).getMovie(FAKE_MOVIE_ID);
    }
}
