package com.virtu.popularmovies.test.app.presenter;

import android.content.Context;
import android.test.AndroidTestCase;

import com.virtu.popularmovies.domain.interactor.usesCases.GetMovieUseCase;
import com.virtu.popularmovies.presentation.model.mapper.MovieModelDataMapper;
import com.virtu.popularmovies.presentation.presenter.MovieDetailsPresenter;
import com.virtu.popularmovies.presentation.view.connectors.MovieDetailsView;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Subscriber;

import static org.mockito.Matchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Created by unai on 20/08/15.
 */
public class MovieDetailsPresenterTest extends AndroidTestCase {

    private static final long FAKE_MOVIE_ID=1234;

    private MovieDetailsPresenter mMovieDetailsPresenter;

    @Mock
    private Context mockContext;
    @Mock
    MovieDetailsView mockMovieDetailsView;
    @Mock
    private GetMovieUseCase mMovieUseCase;
    @Mock
    private MovieModelDataMapper mockMovideModelDataMapper;

    @Override protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mMovieDetailsPresenter = new MovieDetailsPresenter(mMovieUseCase,
                mockMovideModelDataMapper);
        mMovieDetailsPresenter.setView(mockMovieDetailsView);
    }

    public void testMovieDetailsPresenterInitialize(){
        given(mockMovieDetailsView.getContext()).willReturn(mockContext);

        mMovieDetailsPresenter.initialize(FAKE_MOVIE_ID);

        verify(mockMovieDetailsView).hideRetry();
        verify(mockMovieDetailsView).showLoading();
        verify(mMovieUseCase).execute(any(Subscriber.class));
    }

}
