package com.virtu.popularmovies.data.entity.mapper;

import com.virtu.popularmovies.data.ApplicationTestCase;
import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;
import com.virtu.popularmovies.data.repository.provider.MoviesContract;
import com.virtu.popularmovies.domain.entities.Movie;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

/**
 * Created by unai on 21/08/15.
 */
public class MovieEntityDataMapperTest extends ApplicationTestCase {

    private static final long FAKE_MOVIE_ID = 12345678;
    private static final String FAKE_TITLE = "El señor de los grumillos";
    private static final String FAKE_RELEASE_DATE = "1212";
    private static final String FAKE_POSTER_URL = "FAKEURL";
    private static final float FAKE_VOTE_AVERAGE = 4.99f;
    private static final String FAKE_OVERVIEW = "Fake Overview";

    private static final String FAKE_REVIEW_AUTHOR = "Pepito Grillo";
    private static final String FAKE_REVIEW_CONTENT = "CUSTOM";
    private static final String FAKE_REVIEW_URL = "FAKEURL";
    private static final String FAKE_REVIEW_ID = "fdfdsf12515";

    private static final String FAKE_VIDEO_ID = "511gfgfg";
    private static final String FAKE_VIDEO_LANG ="en";
    private static final String FAKE_VIDEO_KEY = "1fdf_3454";
    private static final String FAKE_VIDEO_NAME =  "Test sample";
    private static final String FAKE_VIDEO_SITE = "Youtube";
    private static final int FAKE_VIDEO_SIZE = 1080;
    private static final String FAKE_VIDEO_TYPE = "trailer";

    private MovieEntityDataMapper movieEntityDataMapper;

    @Before
    public void setUp() throws Exception {
        movieEntityDataMapper = new MovieEntityDataMapper();
    }

    @Test
    public void testTransformMovieEntity(){
        MovieEntity movieEntity = createFakeMovie();
        Movie movie = movieEntityDataMapper.transform(movieEntity);

        assertThat(movie,is(instanceOf(Movie.class)));
        assertThat(movie.getId(),is(FAKE_MOVIE_ID));
        assertThat(movie.getTitle(),is(FAKE_TITLE));
        assertThat(movie.getOverView(),is(FAKE_OVERVIEW));
        assertThat(movie.getPosterUrl(),is(
                Movie.API_IMAGE_BASE_URL +
                        Movie.API_W300_ +
                        FAKE_POSTER_URL));
        assertThat(movie.getReleaseDate(), is(FAKE_RELEASE_DATE));
        assertThat(movie.getVote_average(), is(FAKE_VOTE_AVERAGE));
    }

    @Test
    public void testTransformMovieEntityCollection(){
        MovieEntity mockMovieOne = mock(MovieEntity.class);
        MovieEntity mockMovieTwo = mock(MovieEntity.class);

        List<MovieEntity> movieList = new ArrayList<MovieEntity>();
        movieList.add(mockMovieOne);
        movieList.add(mockMovieTwo);

        Collection<Movie> movieModelList = movieEntityDataMapper.transform(movieList);
        assertThat(movieModelList.toArray()[0],is(instanceOf(Movie.class)));
        assertThat(movieModelList.toArray()[1],is(instanceOf(Movie.class)));

        assertThat(movieModelList.size(), is(2));
    }

    @Test
    public void testTransformFullMovieEntity(){
        MovieEntity movieEntity = createFakeMovie();
        ReviewMovieEntity reviewMovieEntity = createFakeReview();
        VideoMovieEntity videoMovieEntity = createFakeVideo();

        List<ReviewMovieEntity> reviewCollection = new ArrayList<ReviewMovieEntity>();
        reviewCollection.add(reviewMovieEntity);

        List<VideoMovieEntity> videoCollection = new ArrayList<VideoMovieEntity>();
        videoCollection.add(videoMovieEntity);

        Movie movie = movieEntityDataMapper.transform(movieEntity,videoCollection,reviewCollection);

        assertThat(movie,is(instanceOf(Movie.class)));
        assertThat(movie.getId(),is(FAKE_MOVIE_ID));
        assertThat(movie.getTitle(),is(FAKE_TITLE));
        assertThat(movie.getOverView(), is(FAKE_OVERVIEW));
        assertThat(movie.getPosterUrl(), is(
                Movie.API_IMAGE_BASE_URL +
                        Movie.API_W300_ +
                        FAKE_POSTER_URL));
        assertThat(movie.getReleaseDate(), is(FAKE_RELEASE_DATE));
        assertThat(movie.getVote_average(), is(FAKE_VOTE_AVERAGE));

        assertThat(movie.gemVideos().size(), is(1));
        assertThat(movie.gemVideos().get(0).getName(),is(FAKE_VIDEO_NAME));
        assertThat(movie.gemVideos().get(0).getKeyVideo(),is(FAKE_VIDEO_KEY));
        assertThat(movie.gemVideos().get(0).getSite(),is(FAKE_VIDEO_SITE));
        assertThat(movie.gemVideos().get(0).getType(),is(FAKE_VIDEO_TYPE));

        assertThat(movie.getReviews().size(), is(1));
        assertThat(movie.getReviews().get(0).getAuthor(),is(FAKE_REVIEW_AUTHOR));
        assertThat(movie.getReviews().get(0).getContent(),is(FAKE_REVIEW_CONTENT));
    }





    private MovieEntity createFakeMovie(){
        MovieEntity movie = new MovieEntity();
        movie.setId(FAKE_MOVIE_ID);
        movie.setTitle(FAKE_TITLE);
        movie.setOverView(FAKE_OVERVIEW);
        movie.setPosterUrl(FAKE_POSTER_URL);
        movie.setReleaseDate(FAKE_RELEASE_DATE);
        movie.setVote_average(FAKE_VOTE_AVERAGE);
        return movie;
    }

    private ReviewMovieEntity createFakeReview(){
        ReviewMovieEntity reviewMovieEntity = new ReviewMovieEntity();
        reviewMovieEntity.setId(FAKE_REVIEW_ID);
        reviewMovieEntity.setAuthor(FAKE_REVIEW_AUTHOR);
        reviewMovieEntity.setContent(FAKE_REVIEW_CONTENT);
        reviewMovieEntity.setUrl(FAKE_REVIEW_URL);
        return reviewMovieEntity;
    }

    private VideoMovieEntity createFakeVideo(){
        VideoMovieEntity videoMovieEntity = new VideoMovieEntity();
        videoMovieEntity.setId(FAKE_VIDEO_ID);
        videoMovieEntity.setSite(FAKE_VIDEO_SITE);
        videoMovieEntity.setKey(FAKE_VIDEO_KEY);
        videoMovieEntity.setLanguage(FAKE_VIDEO_LANG);
        videoMovieEntity.setName(FAKE_VIDEO_NAME);
        videoMovieEntity.setResolution(FAKE_VIDEO_SIZE);
        videoMovieEntity.setTypeVideo(FAKE_VIDEO_TYPE);

        return videoMovieEntity;
    }
}
