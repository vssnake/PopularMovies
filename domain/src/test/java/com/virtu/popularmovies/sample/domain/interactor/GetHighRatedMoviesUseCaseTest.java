package com.virtu.popularmovies.sample.domain.interactor;

import com.virtu.popularmovies.domain.executor.PostExecutionThread;
import com.virtu.popularmovies.domain.executor.ThreadExecutor;
import com.virtu.popularmovies.domain.interactor.usesCases.GetHighestRatedMoviesUseCase;
import com.virtu.popularmovies.domain.repository.MoviesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by unai on 20/08/15.
 */
public class GetHighRatedMoviesUseCaseTest {

    private GetHighestRatedMoviesUseCase getHighestRatedMoviesUseCase;

    @Mock
    private ThreadExecutor mockThreadExecutor;
    @Mock
    private PostExecutionThread mockPostExecutionThread;
    @Mock
    private MoviesRepository mockMoviesRepository;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        getHighestRatedMoviesUseCase = new GetHighestRatedMoviesUseCase(
                mockMoviesRepository,
                mockThreadExecutor,
                mockPostExecutionThread);
    }


    @Test
    public void testGetHighRatedMoviesUseCaseObservableHappyCase(){
        getHighestRatedMoviesUseCase.buildUseCaseObservable();

        verify(mockMoviesRepository).getHighestRatedMoviesDesc();
        verifyNoMoreInteractions(mockMoviesRepository);
        verifyZeroInteractions(mockThreadExecutor);
        verifyZeroInteractions(mockPostExecutionThread);
    }
}
