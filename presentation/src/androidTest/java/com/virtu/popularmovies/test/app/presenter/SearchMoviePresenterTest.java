package com.virtu.popularmovies.test.app.presenter;

import android.content.Context;
import android.test.AndroidTestCase;

import com.virtu.popularmovies.domain.interactor.usesCases.GetFavouriteMoviesUseCase;
import com.virtu.popularmovies.domain.interactor.usesCases.GetHighestRatedMoviesUseCase;
import com.virtu.popularmovies.domain.interactor.usesCases.GetPopularMoviesUseCase;
import com.virtu.popularmovies.presentation.model.mapper.MovieModelDataMapper;
import com.virtu.popularmovies.presentation.presenter.SearchMoviePresenter;
import com.virtu.popularmovies.presentation.view.connectors.SearchMovieView;
import com.virtu.popularmovies.presentation.view.fragment.SearchSearchMovieFragment;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Subscriber;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by unai on 20/08/15.
 */
public class SearchMoviePresenterTest extends AndroidTestCase{

    private SearchMoviePresenter mSearchMoviePresenter;

    @Mock
    private Context mockContext;
    @Mock
    private SearchMovieView mockSearchMovieView;

    @Mock
    private GetPopularMoviesUseCase mockGetPopularMoviesUseCase;

    @Mock
    private GetHighestRatedMoviesUseCase mockGetHighestRatedMoviesUseCase;

    @Mock
    private GetFavouriteMoviesUseCase mockGetFavouriteMoviesUseCase;

    @Mock
    private MovieModelDataMapper mockMovieDataMapper;





    @Override protected void setUp() throws Exception{
        super.setUp();
        // This workaround allows the test to work with Dexmaker 1.2. Commenting it results in error:
        // java.lang.IllegalArgumentException: dexcache == null (and no default could be found; consider setting the 'dexmaker.dexcache' system property)
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath());
        MockitoAnnotations.initMocks(this);
        mSearchMoviePresenter = new SearchMoviePresenter(
                mockGetHighestRatedMoviesUseCase,
                mockGetPopularMoviesUseCase,
                mockGetFavouriteMoviesUseCase,
                mockMovieDataMapper,
                mockContext);
        mSearchMoviePresenter.setView(mockSearchMovieView);
    }

    public void testMovielistPresenterHighRatedInitialize() {
        given(mockSearchMovieView.getContext()).willReturn(mockContext);


        mSearchMoviePresenter.initialize(SearchMoviePresenter.TYPE_MOVIES_LIST.HIGH_RATED);

        verify(mockSearchMovieView).hideRetry();
        verify(mockSearchMovieView).showLoading();
        verify(mockGetHighestRatedMoviesUseCase).execute(any(Subscriber.class));
    }

    public void testMovielistPresenterPopularInitialize() {
        given(mockSearchMovieView.getContext()).willReturn(mockContext);


        mSearchMoviePresenter.initialize(SearchMoviePresenter.TYPE_MOVIES_LIST.POPULAR);

        verify(mockSearchMovieView).hideRetry();
        verify(mockSearchMovieView).showLoading();
        verify(mockGetPopularMoviesUseCase).execute(any(Subscriber.class));
    }
}
