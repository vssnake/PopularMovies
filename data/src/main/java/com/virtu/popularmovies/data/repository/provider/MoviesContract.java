package com.virtu.popularmovies.data.repository.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by virtu on 21/08/2015.
 */
public class MoviesContract {

    /**
     * The "Content authority is a name for the entire content provider, similar to the
     * relantionship between domain name and its website. A convenient string to use for the content
     * authority is the package name for the app, which is guaranteed to be unique on the device
     */
    public static final String CONTENT_AUTHORITY = "com.virtu.popularmovies.app";

    /**
     * Use CONTENT_AUTHORITY to breate the base of all URI´s which apps will use to content to the
     * content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible paths (appended to the {@link #BASE_CONTENT_URI}
     * For instance, content://com.virtu.popularmovies.app/movies is a valid path for looking movies
     * data. content://com.virtu.popularmovies.app/givemeroot/ will fail because the ContentProvider
     * hasn´t been given any information on what to do with givemeroot
     */
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_VIDEOS = "videos";
    public static final String PATH_REVIEWS = "reviews";

    /* Inner class that defines the table content of the Movies table */
    public static final class MovieEntry implements BaseColumns{

        /* Build the final path for looking movies in the DB */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" + CONTENT_AUTHORITY + "/" +
                        PATH_MOVIES;

        //Table name
        public static final String TABLE_NAME = "movies";

        //The expiration time of the movie in the cache
        public static final String COLUMN_EXPIRATION_CACHE_DATE = "expiration_date";

        public static final String COLUMN_FAVOURITE = "favourite";

        //The data of the movie in JSON format
        public static final String COLUMN_MOVIE_DATA = "movie_data";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }

    public static final class ReviewEntry implements  BaseColumns{
        /* Build the final path for looking Review movies in the DB */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" + CONTENT_AUTHORITY + "/" +
                        PATH_MOVIES;

        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_MOVIE_KEY = "movie_id";

        public static final String COLUMN_REVIEW_DATA = "review_data";

        public static Uri buildReviewUri(long id){
            Uri uri = ContentUris.withAppendedId(CONTENT_URI,id);
            return Uri.withAppendedPath(uri, PATH_REVIEWS);
        }
    }

    public static final class VideoEntry implements  BaseColumns{
        /* Build the final path for video movies in the DB */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_VIDEOS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" + CONTENT_AUTHORITY + "/" +
                        PATH_VIDEOS;

        public static final String TABLE_NAME = "videos";

        public static final String COLUMN_MOVIE_KEY ="movie_id";

        public static final String COLUMN_VIDEO_DATA = "video_data";

        public static Uri buildVideoUri(long id){
            //return ContentUris.withAppendedId(CONTENT_URI,id).withAppendedPath(null,PATH_VIDEOS);
           // return ContentUris.withAppendedId(CONTENT_URI,id);
            Uri uri = ContentUris.withAppendedId(CONTENT_URI,id);
            return Uri.withAppendedPath(uri, PATH_VIDEOS);
        }
    }
}
