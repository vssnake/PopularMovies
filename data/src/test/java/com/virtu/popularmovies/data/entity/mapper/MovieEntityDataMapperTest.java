package com.virtu.popularmovies.data.entity.mapper;

import com.virtu.popularmovies.data.ApplicationTestCase;
import com.virtu.popularmovies.data.EntityTestUtilities;
import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;
import com.virtu.popularmovies.domain.entities.MovieD;

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



    private MovieEntityDataMapper movieEntityDataMapper;

    @Before
    public void setUp() throws Exception {
        movieEntityDataMapper = new MovieEntityDataMapper();
    }

    @Test
    public void testTransformMovieEntity(){
        MovieEntity movieEntity = EntityTestUtilities.createFakeMovie();
        MovieD movie = movieEntityDataMapper.transform(movieEntity);

        assertThat(movie,is(instanceOf(MovieD.class)));
        assertThat(movie.getId(),is(EntityTestUtilities.FAKE_MOVIE_ID));
        assertThat(movie.getTitle(),is(EntityTestUtilities.FAKE_TITLE));
        assertThat(movie.getOverView(),is(EntityTestUtilities.FAKE_OVERVIEW));
        assertThat(movie.getPosterUrl(),is(
                MovieD.API_IMAGE_BASE_URL +
                        MovieD.API_W300_ +
                        EntityTestUtilities.FAKE_POSTER_URL));
        assertThat(movie.getReleaseDate(), is(EntityTestUtilities.FAKE_RELEASE_DATE));
        assertThat(movie.getVote_average(), is(EntityTestUtilities.FAKE_VOTE_AVERAGE));
    }

    @Test
    public void testTransformMovieEntityCollection(){
        MovieEntity mockMovieOne = mock(MovieEntity.class);
        MovieEntity mockMovieTwo = mock(MovieEntity.class);

        List<MovieEntity> movieList = new ArrayList<MovieEntity>();
        movieList.add(mockMovieOne);
        movieList.add(mockMovieTwo);

        Collection<MovieD> movieModelList = movieEntityDataMapper.transform(movieList);
        assertThat(movieModelList.toArray()[0],is(instanceOf(MovieD.class)));
        assertThat(movieModelList.toArray()[1],is(instanceOf(MovieD.class)));

        assertThat(movieModelList.size(), is(2));
    }

    @Test
    public void testTransformFullMovieEntity(){
        MovieEntity movieEntity = EntityTestUtilities.createFakeMovie();
        ReviewMovieEntity reviewMovieEntity = EntityTestUtilities.createFakeReview();
        VideoMovieEntity videoMovieEntity = EntityTestUtilities.createFakeVideo();

        List<ReviewMovieEntity> reviewCollection = new ArrayList<ReviewMovieEntity>();
        reviewCollection.add(reviewMovieEntity);

        List<VideoMovieEntity> videoCollection = new ArrayList<VideoMovieEntity>();
        videoCollection.add(videoMovieEntity);

        MovieD movie = movieEntityDataMapper.transform(movieEntity,videoCollection,reviewCollection);

        assertThat(movie,is(instanceOf(MovieD.class)));
        assertThat(movie.getId(),is(EntityTestUtilities.FAKE_MOVIE_ID));
        assertThat(movie.getTitle(),is(EntityTestUtilities.FAKE_TITLE));
        assertThat(movie.getOverView(), is(EntityTestUtilities.FAKE_OVERVIEW));
        assertThat(movie.getPosterUrl(), is(
                MovieD.API_IMAGE_BASE_URL +
                        MovieD.API_W300_ +
                        EntityTestUtilities.FAKE_POSTER_URL));
        assertThat(movie.getReleaseDate(), is(EntityTestUtilities.FAKE_RELEASE_DATE));
        assertThat(movie.getVote_average(), is(EntityTestUtilities.FAKE_VOTE_AVERAGE));

        assertThat(movie.gemVideos().size(), is(1));
        assertThat(movie.gemVideos().get(0).getName(),is(EntityTestUtilities.FAKE_VIDEO_NAME));
        assertThat(movie.gemVideos().get(0).getKeyVideo(),is(EntityTestUtilities.FAKE_VIDEO_KEY));
        assertThat(movie.gemVideos().get(0).getSite(),is(EntityTestUtilities.FAKE_VIDEO_SITE));
        assertThat(movie.gemVideos().get(0).getType(),is(EntityTestUtilities.FAKE_VIDEO_TYPE));

        assertThat(movie.getReviews().size(), is(1));
        assertThat(movie.getReviews().get(0).getAuthor(),is(EntityTestUtilities.FAKE_REVIEW_AUTHOR));
        assertThat(movie.getReviews().get(0).getContent(),is(EntityTestUtilities.FAKE_REVIEW_CONTENT));
    }






}
