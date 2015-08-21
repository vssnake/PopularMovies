package com.virtu.popularmovies.test.app.mapper;

import android.test.InstrumentationTestCase;

import com.example.virtu.spotifystreamer.BuildConfig;
import com.virtu.popularmovies.domain.entities.Movie;
import com.virtu.popularmovies.presentation.model.MovieModelPresenter;
import com.virtu.popularmovies.presentation.model.mapper.MovieModelDataMapper;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by unai on 20/08/15.
 */
public class MovieModelDataMapperTest extends InstrumentationTestCase {

    private static final long FAKE_MOVIE_ID = 12345678;
    private static final String FAKE_TITLE = "El señor de los grumillos";
    private static final String FAKE_RELEASE_DATE = "1212";
    private static final String FAKE_POSTER_URL = "FAKEURL";
    private static final float FAKE_VOTE_AVERAGE = 4.99f;
    private static final String FAKE_OVERVIEW = "Fake Overview";


    private MovieModelDataMapper mMovieModelDataMapper;


    @Override
    protected void setUp() throws Exception{
        super.setUp();
        mMovieModelDataMapper = new MovieModelDataMapper();
        // This workaround allows the test to work with Dexmaker 1.2. Commenting it results in error:
        // java.lang.IllegalArgumentException: dexcache == null (and no default could be found; consider setting the 'dexmaker.dexcache' system property)
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
    }

    public void testTransformMovie(){
        Movie movie = createFakeMovie();
        MovieModelPresenter movieModel = mMovieModelDataMapper.transform(movie);

        assertThat(movieModel,is(instanceOf(MovieModelPresenter.class)));
        assertThat(movieModel.getId(),is(FAKE_MOVIE_ID));
        assertThat(movieModel.getTitle(),is(FAKE_TITLE));
        assertThat(movieModel.getOverView(),is(FAKE_OVERVIEW));
        assertThat(movieModel.getPosterUrl(),is(
                Movie.API_IMAGE_BASE_URL +
                        Movie.API_W300_ +
                        FAKE_POSTER_URL));
        assertThat(movieModel.getReleaseDate(), is(FAKE_RELEASE_DATE));
        assertThat(movieModel.getVote_average(), is(FAKE_VOTE_AVERAGE));
    }

    public void testTransformMovieCollection(){
        Movie mockMovieOne = mock(Movie.class);
        Movie mockMovieTwo = mock(Movie.class);

        List<Movie> movieList = new ArrayList<Movie>();
        movieList.add(mockMovieOne);
        movieList.add(mockMovieTwo);

        Collection<MovieModelPresenter> movieModelList = mMovieModelDataMapper.transform(movieList);
        assertThat(movieModelList.toArray()[0],is(instanceOf(MovieModelPresenter.class)));
        assertThat(movieModelList.toArray()[1],is(instanceOf(MovieModelPresenter.class)));

        assertThat(movieModelList.size(), is(2));

    }





    private Movie createFakeMovie(){
        Movie movie = new Movie(FAKE_MOVIE_ID);
        movie.setTitle(FAKE_TITLE);
        movie.setOverView(FAKE_OVERVIEW);
        movie.setPosterUrl(FAKE_POSTER_URL);
        movie.setReleaseDate(FAKE_RELEASE_DATE);
        movie.setVote_average(FAKE_VOTE_AVERAGE);
        return movie;
    }
}
