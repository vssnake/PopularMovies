package com.virtu.popularmovies.data;

import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;

/**
 * Created by virtu on 24/08/2015.
 */
public class EntityTestUtilities {

    public static final long FAKE_MOVIE_ID = 12345678;
    public static final String FAKE_TITLE = "El señor de los grumillos";
    public static final String FAKE_RELEASE_DATE = "1212";
    public static final String FAKE_POSTER_URL = "FAKEURL";
    public static final float FAKE_VOTE_AVERAGE = 4.99f;
    public static final String FAKE_OVERVIEW = "Fake Overview";

    public static final String FAKE_REVIEW_AUTHOR = "Pepito Grillo";
    public static final String FAKE_REVIEW_CONTENT = "CUSTOM";
    public static final String FAKE_REVIEW_URL = "FAKEURL";
    public static final String FAKE_REVIEW_ID = "fdfdsf12515";

    public static final String FAKE_VIDEO_ID = "511gfgfg";
    public static final String FAKE_VIDEO_LANG ="en";
    public static final String FAKE_VIDEO_KEY = "1fdf_3454";
    public static final String FAKE_VIDEO_NAME =  "Test sample";
    public static final String FAKE_VIDEO_SITE = "Youtube";
    public static final int FAKE_VIDEO_SIZE = 1080;
    public static final String FAKE_VIDEO_TYPE = "trailer";

    public static MovieEntity createFakeMovie(){
        MovieEntity movie = new MovieEntity();
        movie.setId(FAKE_MOVIE_ID);
        movie.setTitle(FAKE_TITLE);
        movie.setOverView(FAKE_OVERVIEW);
        movie.setPosterUrl(FAKE_POSTER_URL);
        movie.setReleaseDate(FAKE_RELEASE_DATE);
        movie.setVote_average(FAKE_VOTE_AVERAGE);
        return movie;
    }

    public static ReviewMovieEntity createFakeReview(){
        ReviewMovieEntity reviewMovieEntity = new ReviewMovieEntity();
        reviewMovieEntity.setId(FAKE_REVIEW_ID);
        reviewMovieEntity.setAuthor(FAKE_REVIEW_AUTHOR);
        reviewMovieEntity.setContent(FAKE_REVIEW_CONTENT);
        reviewMovieEntity.setUrl(FAKE_REVIEW_URL);
        return reviewMovieEntity;
    }

    public static VideoMovieEntity createFakeVideo(){
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
