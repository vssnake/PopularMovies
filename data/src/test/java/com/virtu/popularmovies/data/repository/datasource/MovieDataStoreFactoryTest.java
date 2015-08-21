package com.virtu.popularmovies.data.repository.datasource;

import com.virtu.popularmovies.data.ApplicationTestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by unai on 21/08/15.
 */
public class MovieDataStoreFactoryTest extends ApplicationTestCase {

    private static final long FAKE_MOVIE_ID = 12345678;

    private MovieDataStoreFactory movieDataStoreFactory;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        movieDataStoreFactory =
                new MovieDataStoreFactory(RuntimeEnvironment.application);
    }


    @Test
    public void testCreateCloudDataStore(){
        MovieDataStore movieDataStore = movieDataStoreFactory.createCloudDataStore();

        assertThat(movieDataStore, is(notNullValue()));
        assertThat(movieDataStore, is(instanceOf(CloudMovieDataStore.class)));

    }
}
