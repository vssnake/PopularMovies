package com.virtu.popularmovies.sample.domain.exception;

import com.virtu.popularmovies.domain.exception.DefaultErrorBundle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Created by unai on 20/08/15.
 */
public class DefaultErrorBundleTest {

    private DefaultErrorBundle defaultErrorBundle;


    @Mock
    private Exception mockException;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        defaultErrorBundle = new DefaultErrorBundle(mockException);
    }

    @Test
    public void testGetErrorMessageInteraction(){
        defaultErrorBundle.getErrorMessage();

        verify(mockException).getMessage();
    }
}
