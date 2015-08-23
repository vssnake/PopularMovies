package com.virtu.popularmovies.data.repository.provider;

import android.net.Uri;

import com.virtu.popularmovies.data.ApplicationTestCase;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by virtu on 22/08/2015.
 */
public class TestMovieContract extends ApplicationTestCase {

    private static final long TEST_MOVIE_ID = 12L;

    @Test
    public void testBuildMovies(){
        Uri movieUri = MoviesContract.MovieEntry.buildMovieUri(TEST_MOVIE_ID);
        assertThat(movieUri,is(notNullValue()));
        assertThat(Long.toString(TEST_MOVIE_ID),is(movieUri.getLastPathSegment()));
        assertThat(movieUri.toString(),is(MoviesContract.BASE_CONTENT_URI + "/movies/" + TEST_MOVIE_ID));
    }
    @Test
    public void testBuildReview(){
        Uri movieUri = MoviesContract.ReviewEntry.buildReviewUri(TEST_MOVIE_ID);
        assertThat(movieUri,is(notNullValue()));
        assertThat(MoviesContract.PATH_REVIEWS,is(movieUri.getLastPathSegment()));
        assertThat(movieUri.toString(),is(MoviesContract.BASE_CONTENT_URI + "/movies/" + TEST_MOVIE_ID + "/" +
                MoviesContract.PATH_REVIEWS));
    }
    @Test
    public void testBuildVideos(){
        Uri movieUri = MoviesContract.VideoEntry.buildVideoUri(TEST_MOVIE_ID);
        assertThat(movieUri,is(notNullValue()));
        assertThat(MoviesContract.PATH_VIDEOS,is(movieUri.getLastPathSegment()));
        assertThat(movieUri.toString(),is(MoviesContract.BASE_CONTENT_URI + "/movies/" + TEST_MOVIE_ID + "/" +
                MoviesContract.PATH_VIDEOS));
    }
}
