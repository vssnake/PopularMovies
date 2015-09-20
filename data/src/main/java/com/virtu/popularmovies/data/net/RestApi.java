package com.virtu.popularmovies.data.net;

import com.virtu.popularmovies.data.entity.MovieEntity;
import com.virtu.popularmovies.data.entity.ReviewMovieEntity;
import com.virtu.popularmovies.data.entity.VideoMovieEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by virtu on 09/07/2015.
 */
public interface RestApi {

    static final String API_BASE_URL = "http://api.themoviedb.org/3/";


    static final String API_W300_ = "w300/";
    static final String API_MOVIE_ = "movie";
    static final String API_REVIEW_ = "/reviews";
    static final String API_VIDEO_ = "/videos";

    static final String API_URL_GET_MOVIE_LIST =  "discover/movie";

    static final String API_QUERY_SORT_BY = "sort_by=";
    static final String API_QUERY_SORT_BY_POPULARITY_DESC_VALUE = "popularity.desc";
    static final String API_QUERY_SORT_BY_VOTE_AVERAGE_DESC_VALUE = "vote_average.desc";
    static final String API_QUERY_SORT_BY_VOTE_COUNT_DESC_VALUE = "vote_count.desc";

    static final String API_KEY_NAME = "api_key=";
    static final String API_KEY_VALUE = "877202529bf5105ae5e88de9b3e28142";




    static final String API_QUERY_KEY = "?";
    static final String API_CONCAT_QUERY = "&";

    Observable<List<MovieEntity>> getMovieEntityListPopularityDESC();

    Observable<List<MovieEntity>> getMovieEntityListVoteAverageDesc();

    Observable<List<ReviewMovieEntity>> getReviewMovie(Long idMovie);

    Observable<List<VideoMovieEntity>> getVideosMovie(Long idMovie);


}
