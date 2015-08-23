package com.virtu.popularmovies.data.repository.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.virtu.popularmovies.data.ApplicationTestCase;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by virtu on 22/08/2015.
 */
public class TestUriMatcher extends ApplicationTestCase {

    private static final long TEST_MOVIE_ID = 12L;

    private static final Uri TEST_MOVIE_DIR = MoviesContract.MovieEntry.buildMovieUri(TEST_MOVIE_ID);
    private static final Uri TEST_MOVIES = MoviesContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_REVIEWS = MoviesContract.ReviewEntry.buildReviewUri(TEST_MOVIE_ID);
    private static final Uri TEST_VIDEOS = MoviesContract.VideoEntry.buildVideoUri(TEST_MOVIE_ID);


    @Test
    public void testUriMatcher(){

        UriMatcher testMacher = MoviesProvider.buildURIMatcher();
        assertThat("Error: The Movie URI was matched incorrectly",
                testMacher.match(TEST_MOVIE_DIR), is(MoviesProvider.MOVIE_ID));
        assertThat("Error: The Movies URI was matched incorrectly",
                testMacher.match(TEST_MOVIES), is(MoviesProvider.MOVIES));
        assertThat("Error: The Movie URI was matched incorrectly",
                testMacher.match(TEST_REVIEWS), is(MoviesProvider.REVIEW));
        assertThat("Error: The Movies URI was matched incorrectly",
                testMacher.match(TEST_VIDEOS), is(MoviesProvider.VIDEO));

                //assertEquals("Error: The Movie URI was matched incorrectly",
                //        testMacher.match(TEST_MOVIE_DIR), MoviesProvider.MOVIE_ID);
    }
}
