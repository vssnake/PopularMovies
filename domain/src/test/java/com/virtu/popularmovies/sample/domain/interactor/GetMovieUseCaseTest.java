package com.virtu.popularmovies.sample.domain.interactor;

import com.virtu.popularmovies.domain.executor.PostExecutionThread;
import com.virtu.popularmovies.domain.executor.ThreadExecutor;
import com.virtu.popularmovies.domain.interactor.usesCases.GetMovieUseCase;
import com.virtu.popularmovies.domain.repository.MoviesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by unai on 20/08/15.
 */
public class GetMovieUseCaseTest {

    private static final long FAKE_MOVIE_ID = 12345678;

    private GetMovieUseCase getMovieUseCase;

    @Mock
    private ThreadExecutor mockThreadExecutor;
    @Mock
    private PostExecutionThread mockPostExecutionThread;
    @Mock
    private MoviesRepository mockMoviesRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        getMovieUseCase = new GetMovieUseCase(FAKE_MOVIE_ID,
                mockMoviesRepository,
                mockThreadExecutor,
                mockPostExecutionThread);



    }

    @Test
    public void testGetMovieDetailsUseCaseObservableHappyCase(){
        getMovieUseCase.buildUseCaseObservable();

        verify(mockMoviesRepository).getMovie(FAKE_MOVIE_ID,false);
        verifyZeroInteractions(mockMoviesRepository);
        verifyZeroInteractions(mockThreadExecutor);
        verifyZeroInteractions(mockPostExecutionThread);
    }
}
