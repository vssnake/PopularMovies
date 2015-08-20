package com.virtu.popularmovies.sample.domain.interactor;

import com.virtu.popularmovies.domain.executor.PostExecutionThread;
import com.virtu.popularmovies.domain.executor.ThreadExecutor;
import com.virtu.popularmovies.domain.interactor.usesCases.GetHighestRatedMoviesUseCase;
import com.virtu.popularmovies.domain.interactor.usesCases.GetPopularMoviesUseCase;
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
public class GetPopularMoviesUseCaseTest {

    private GetPopularMoviesUseCase getPopularMoviesUseCase;

    @Mock
    private ThreadExecutor mockThreadExecutor;
    @Mock
    private PostExecutionThread mockPostExecutionThread;
    @Mock
    private MoviesRepository mockMoviesRepository;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        getPopularMoviesUseCase = new GetPopularMoviesUseCase(
                mockMoviesRepository,
                mockThreadExecutor,
                mockPostExecutionThread);
    }


    @Test
    public void testGetHighRatedMoviesUseCaseObservableHappyCase(){
        getPopularMoviesUseCase.buildUseCaseObservable();

        verify(mockMoviesRepository).getPopularMoviesDesc();
        verifyNoMoreInteractions(mockMoviesRepository);
        verifyZeroInteractions(mockThreadExecutor);
        verifyZeroInteractions(mockPostExecutionThread);
    }
}
