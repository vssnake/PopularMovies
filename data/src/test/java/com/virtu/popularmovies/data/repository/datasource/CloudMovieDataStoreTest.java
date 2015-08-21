package com.virtu.popularmovies.data.repository.datasource;

import com.virtu.popularmovies.data.ApplicationTestCase;
import com.virtu.popularmovies.data.net.RestApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Created by unai on 21/08/15.
 */
public class CloudMovieDataStoreTest extends ApplicationTestCase {

    private static final long FAKE_MOVIE_ID = 12345678;

    private CloudMovieDataStore cloudMovieDataStore;

    @Mock
    private RestApi mockRestApi;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        cloudMovieDataStore = new CloudMovieDataStore(mockRestApi);
    }

    @Test
    public void testGetHightRatedMoviesFromApi(){
        cloudMovieDataStore.getHighRatedMoviesDesc();
        verify(mockRestApi).getMovieEntityListVoteAverageDesc();
    }


    @Test
    public void testGetPopularMoviesFromApi(){
        cloudMovieDataStore.getPopularMoviesDesc();
        verify(mockRestApi).getMovieEntityListPopularityDESC();

    }

    @Test
    public void testGetMovieFromApi(){
        cloudMovieDataStore.getMovie(FAKE_MOVIE_ID);
        verify(cloudMovieDataStore).getMovie(FAKE_MOVIE_ID);
    }
}
