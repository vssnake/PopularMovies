package com.virtu.popularmovies.data.cache.serializer;

import com.virtu.popularmovies.data.ApplicationTestCase;
import com.virtu.popularmovies.data.EntityTestUtilities;
import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by virtu on 24/08/2015.
 */
public class JsonSerializerTest extends ApplicationTestCase{
    private MoviesSerializer jsonSerializer;

    private static final String MOVIE_ENTITY_SERIALIZED = "{\"id\":12345678,\"title\":\"El señor de los grumillos\",\"release_date\":\"1212\",\"poster_path\":\"FAKEURL\",\"vote_average\":4.99,\"overview\":\"Fake Overview\"}";
    private static final String REVIEW_MOVIE_SERIALIZED = "{\"id\":\"fdfdsf12515\",\"author\":\"Pepito Grillo\",\"content\":\"CUSTOM\",\"url\":\"FAKEURL\"}";
    private static final String VIDEO_MOVIE_SERIALIZED = "{\"id\":\"511gfgfg\",\"iso_639_1\":\"en\",\"key\":\"1fdf_3454\",\"name\":\"Test sample\",\"site\":\"Youtube\",\"size\":1080,\"type\":\"trailer\"}";
    @Before
    public void setUp(){
        jsonSerializer = new MoviesSerializer();
    }

    @Test
    public void testSerializeHappyCase(){
        MovieEntity movieEntity = EntityTestUtilities.createFakeMovie();
        ReviewMovieEntity reviewEntity = EntityTestUtilities.createFakeReview();
        VideoMovieEntity videoMovieEntity = EntityTestUtilities.createFakeVideo();

        String movieEntitySerialized = jsonSerializer.serialize(movieEntity);
        assertThat(movieEntitySerialized,is(MOVIE_ENTITY_SERIALIZED));
        String reviewMovieEntitySerialized = jsonSerializer.serialize(reviewEntity);
        assertThat(reviewMovieEntitySerialized,is(REVIEW_MOVIE_SERIALIZED));
        String videoMovieEntitySerialized = jsonSerializer.serialize(videoMovieEntity);
        assertThat(videoMovieEntitySerialized,is(VIDEO_MOVIE_SERIALIZED));

    }

    @Test
    public void testDeSerializeHappyCase(){
        MovieEntity movieEntity =jsonSerializer.deserializeMovie(MOVIE_ENTITY_SERIALIZED);

        assertThat(movieEntity.getId(),is(EntityTestUtilities.FAKE_MOVIE_ID));
        assertThat(movieEntity.getOverView(),is(EntityTestUtilities.FAKE_OVERVIEW));
        assertThat(movieEntity.getPosterUrl(),is(EntityTestUtilities.FAKE_POSTER_URL));
        assertThat(movieEntity.getReleaseDate(),is(EntityTestUtilities.FAKE_RELEASE_DATE));
        assertThat(movieEntity.getTitle(),is(EntityTestUtilities.FAKE_TITLE));
        assertThat(movieEntity.getVote_average(),is(EntityTestUtilities.FAKE_VOTE_AVERAGE));

        ReviewMovieEntity reviewMovieEntity =  jsonSerializer.deserializeReview(REVIEW_MOVIE_SERIALIZED);

        assertThat(reviewMovieEntity.getId(),is(EntityTestUtilities.FAKE_REVIEW_ID));
        assertThat(reviewMovieEntity.getContent(),is(EntityTestUtilities.FAKE_REVIEW_CONTENT));
        assertThat(reviewMovieEntity.getAuthor(),is(EntityTestUtilities.FAKE_REVIEW_AUTHOR));
        assertThat(reviewMovieEntity.getUrl(),is(EntityTestUtilities.FAKE_REVIEW_URL));

        VideoMovieEntity videoMovieEntity = jsonSerializer.deserializeVideo(VIDEO_MOVIE_SERIALIZED);

        assertThat(videoMovieEntity.getId(),is(EntityTestUtilities.FAKE_VIDEO_ID));
        assertThat(videoMovieEntity.getName(),is(EntityTestUtilities.FAKE_VIDEO_NAME));
        assertThat(videoMovieEntity.getKey(),is(EntityTestUtilities.FAKE_VIDEO_KEY));
        assertThat(videoMovieEntity.getLanguage(),is(EntityTestUtilities.FAKE_VIDEO_LANG));
        assertThat(videoMovieEntity.getResolution(),is(EntityTestUtilities.FAKE_VIDEO_SIZE));
        assertThat(videoMovieEntity.getSite(),is(EntityTestUtilities.FAKE_VIDEO_SITE));
        assertThat(videoMovieEntity.getTypeVideo(),is(EntityTestUtilities.FAKE_VIDEO_TYPE));
    }
}
