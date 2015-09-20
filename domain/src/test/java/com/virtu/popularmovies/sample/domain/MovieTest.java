package com.virtu.popularmovies.sample.domain;

import com.virtu.popularmovies.domain.entities.MovieD;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by unai on 20/08/15.
 */
public class MovieTest {

    private static final long FAKE_MOVIDE_ID = 123456;

    private MovieD movie;

    @Before
    public void setUp(){
        movie = new MovieD(FAKE_MOVIDE_ID);
    }

    @Test
    public void testMovieContructorCase(){
        long movieID = movie.getId();
        assertThat(movieID,is(FAKE_MOVIDE_ID));

    }
}
