package com.virtu.popularmovies.test.app.exception;

import android.test.AndroidTestCase;

import com.example.virtu.spotifystreamer.R;
import com.virtu.popularmovies.data.exception.MovieNotFoundException;
import com.virtu.popularmovies.data.exception.NetworkConnectionException;
import com.virtu.popularmovies.presentation.exception.ErrorMessageFactory;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by unai on 20/08/15.
 */
public class ErrorMessageFactoryTest extends AndroidTestCase {


    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testNetworkConnectionErrorMessage() {
        String expectedMessage = getContext().getString(R.string.exception_message_no_connection);
        String actualMessage = ErrorMessageFactory.create(getContext(),
                new NetworkConnectionException());

        assertThat(actualMessage, is(equalTo(expectedMessage)));
    }

    public void testMovieNotFoundErrorMessage(){
        String exceptedMessage = getContext().getString(R.string.exception_message_movie_not_found);
        String actualMessage = ErrorMessageFactory.create(getContext(),new MovieNotFoundException());

        assertThat(actualMessage,is(equalTo(exceptedMessage)));
    }
}
