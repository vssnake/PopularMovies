package com.virtu.popularmovies.data.repository;

import com.virtu.popularmovies.data.ApplicationTestCase;
import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;
import com.virtu.popularmovies.data.entity.mapper.MovieEntityDataMapper;
import com.virtu.popularmovies.data.repository.datasource.MovieDataStore;
import com.virtu.popularmovies.data.repository.datasource.MovieDataStoreFactory;
import com.virtu.popularmovies.domain.entities.MovieD;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func3;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by unai on 20/08/15.
 */
public class MovieDataRepositoryTest extends ApplicationTestCase {

    private static final long FAKE_MOVIE_ID = 12345678;

    private MoviesDataRepository moviesDataRepository;

    @Mock
    private MovieDataStoreFactory mockMovieDataStoreFactory;
    @Mock
    private MovieEntityDataMapper mockEntityDataMapper;
    @Mock
    private MovieDataStore mockMovieDataStore;
    @Mock
    private MovieEntity mockMovieEntity;

    @Rule
    public ExpectedException exceptedException = ExpectedException.none();

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        given(mockMovieDataStoreFactory.createCloudDataStore()).willReturn(mockMovieDataStore);
        moviesDataRepository = new MoviesDataRepository(mockMovieDataStoreFactory,
                mockEntityDataMapper);


        verify(mockMovieDataStoreFactory).createCloudDataStore();
    }

    @Test
    public void testGetHighRatedMoviesHappyCase(){
        List<MovieEntity> movieEntityList = new ArrayList<>();
        movieEntityList.add(new MovieEntity());

        given(mockMovieDataStore.getHighRatedMoviesDesc()).willReturn(Observable.just(movieEntityList));

        moviesDataRepository.getHighestRatedMoviesDesc();


        verify(mockMovieDataStore).getHighRatedMoviesDesc();

    }
    @Test
    public void testGetPopularMoviesHappyCase(){
        List<MovieEntity> movieEntityList = new ArrayList<>();
        movieEntityList.add(new MovieEntity());

        given(mockMovieDataStore.getPopularMoviesDesc()).willReturn(Observable.just(movieEntityList));

        moviesDataRepository.getPopularMoviesDesc();


        verify(mockMovieDataStore).getPopularMoviesDesc();
    }
    @Test
    public void testGetMovieDetailsHappyCase(){
        MovieEntity movieEntity = new MovieEntity();
        List<ReviewMovieEntity> reviewEntity = new ArrayList<ReviewMovieEntity>();
        List<VideoMovieEntity> videosEntity = new ArrayList<VideoMovieEntity>();
        final Func3<MovieEntity, List<ReviewMovieEntity>, List<VideoMovieEntity>, Void> callbackMovie =
                new Func3<MovieEntity, List<ReviewMovieEntity>, List<VideoMovieEntity>, Void>(){

                    @Override
                    public Void call(MovieEntity movieEntity,
                                     List<ReviewMovieEntity> reviewMovieEntities,
                                     List<VideoMovieEntity> videoMovieEntities) {
                        return null;
                    }
                };
       // given(mockMovieDataStore.getMovie(FAKE_MOVIE_ID)).willReturn(Observable.just(movieEntity));
        given(mockMovieDataStore.getReview(FAKE_MOVIE_ID)).willReturn(Observable.just(reviewEntity));
        given(mockMovieDataStore.getVideo(FAKE_MOVIE_ID)).willReturn(Observable.just(videosEntity));
        moviesDataRepository.getMovie(FAKE_MOVIE_ID).subscribe(new Action1<MovieD>() {
            @Override
            public void call(MovieD movie) {
                verify(mockMovieDataStore).getMovie(FAKE_MOVIE_ID,callbackMovie);
                verify(mockMovieDataStore).getReview(FAKE_MOVIE_ID);
                verify(mockMovieDataStore).getVideo(FAKE_MOVIE_ID);
            }
        });



    }


}
