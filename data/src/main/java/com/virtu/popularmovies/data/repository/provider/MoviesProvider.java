package com.virtu.popularmovies.data.repository.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by virtu on 21/08/2015.
 */
public class MoviesProvider extends ContentProvider {


    public static final String TAG = MoviesProvider.class.getSimpleName();
    //The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildURIMatcher();
    private MoviesDbHelper moviesDbHelper;

    static final int MOVIE_ID = 100;
    static final int VIDEO = 101;
    static final int REVIEW = 102;
    static final int MOVIES = 200;

    public static final SQLiteQueryBuilder sMovieWithVideosAndReviewsQueryBuilder;

    private static final String sMovieWithID =
            MoviesContract.MovieEntry.TABLE_NAME+
                    "." + MoviesContract.MovieEntry._ID + " = ? ";

    /**
     * The UriMatcher will match each URI integer constant defined above.
     * @return
     */
    static UriMatcher buildURIMatcher(){

        /**
         * ALl paths added to the UriMatcher have a corresponding code to return when a match is found
         * . The code passed into the constructor represents the code to return for the
         * root URI.  It´s common to use NO_MATCH as the code for this case
         */
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code
        matcher.addURI(authority,MoviesContract.PATH_MOVIES + "/*",MOVIE_ID);
        matcher.addURI(authority,MoviesContract.PATH_MOVIES,MOVIES);

        matcher.addURI(authority,MoviesContract.PATH_MOVIES + "/*/" + MoviesContract.PATH_REVIEWS,
                REVIEW);
        matcher.addURI(authority,MoviesContract.PATH_MOVIES + "/*/" + MoviesContract.PATH_VIDEOS,
                VIDEO);

        return matcher;
    }

    static {
        sMovieWithVideosAndReviewsQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join of three tables
        //Movies, Review and Videos
        sMovieWithVideosAndReviewsQueryBuilder.setTables(
                MoviesContract.MovieEntry.TABLE_NAME + " INNER JOIN " +
                        MoviesContract.VideoEntry.TABLE_NAME +
                        " ON " + MoviesContract.MovieEntry.TABLE_NAME +
                        "." + MoviesContract.MovieEntry._ID +
                        " = " + MoviesContract.VideoEntry.TABLE_NAME +
                        "." + MoviesContract.VideoEntry.COLUMN_MOVIE_KEY +
                        " INNER JOIN " + MoviesContract.ReviewEntry.TABLE_NAME +
                        " ON " + MoviesContract.VideoEntry.TABLE_NAME +
                        "." + MoviesContract.VideoEntry.COLUMN_MOVIE_KEY +
                        " = " + MoviesContract.ReviewEntry.TABLE_NAME +
                        "." + MoviesContract.ReviewEntry._ID
        );



    }





    @Override
    public boolean onCreate() {
        moviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Here´s a switch statement that, given a URI, will determine what kind of request it is
        // and query the database accordingly
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE_ID:
            {
                long id = ContentUris.parseId(uri);
               /* retCursor = moviesDbHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder*/

                retCursor = moviesDbHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        MoviesContract.MovieEntry._ID +
                                " = ?",
                        new String[]{id+""},
                        null,
                        null,
                        sortOrder);
                /*retCursor = sMovieWithVideosAndReviewsQueryBuilder.query(
                        moviesDbHelper.getReadableDatabase(),
                        projection,
                        MoviesContract.MovieEntry._ID +
                        " = ?",
                        new String[]{id+""},
                        null,
                        null,
                        sortOrder);*/

                break;
            }
            case VIDEO:
            {

                retCursor = moviesDbHelper.getReadableDatabase().query(
                        MoviesContract.VideoEntry.TABLE_NAME,
                        projection,
                        MoviesContract.VideoEntry.COLUMN_MOVIE_KEY + "= ?",
                        new String[]{uri.getPathSegments().get(1)}, //get the key movie
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REVIEW:
            {

                retCursor = moviesDbHelper.getReadableDatabase().query(
                        MoviesContract.ReviewEntry.TABLE_NAME,
                        projection,
                        MoviesContract.ReviewEntry.COLUMN_MOVIE_KEY + "= ?",
                        new String[]{uri.getPathSegments().get(1)},//get the key movie
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIES:
            {
                retCursor = moviesDbHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE_ID:
                return MoviesContract.MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIES:
                return MoviesContract.MovieEntry.CONTENT_TYPE;
            case REVIEW:
                return MoviesContract.ReviewEntry.CONTENT_ITEM_TYPE;
            case VIDEO:
                return MoviesContract.VideoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        final int math  = sUriMatcher.match(uri);
        Uri returnUri = null;

        long _id = -1;
        switch (math){
            case MOVIES:
            {
                _id = db.insert(MoviesContract.MovieEntry.TABLE_NAME,null,values);
                if (_id > 0){
                    returnUri = MoviesContract.MovieEntry.buildMovieUri(_id);
                }else{
                    Log.d(TAG,"Movie already inserted in the db " +_id);
                    //throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case REVIEW:
            {
                _id = db.insert(MoviesContract.ReviewEntry.TABLE_NAME,null,values);
                if (_id > 0 ){
                    returnUri = MoviesContract.ReviewEntry.buildReviewUri(_id);
                }else{
                    Log.d(TAG,"Review already inserted in the db " +_id);
                    //throw new android.database.SQLException("Failed to insert row into" +uri);
                }
                break;
            }
            case VIDEO:
            {
                _id = db.insert(MoviesContract.VideoEntry.TABLE_NAME,null,values);
                if (_id > 0 ){
                    returnUri = MoviesContract.VideoEntry.buildVideoUri(_id);
                }else{
                    Log.d(TAG,"Video already inserted in the db " +_id);
                   // throw new android.database.SQLException("Failed to insert row into" +uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }
        if (_id != -1){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection ) selection = "1";
        switch (match){
            case MOVIE_ID:
            {
                long id = ContentUris.parseId(uri);
                rowsDeleted = db.delete(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        MoviesContract.MovieEntry._ID + " = ?",
                        new String[]{id+""});
                rowsDeleted += db.delete(MoviesContract.ReviewEntry.TABLE_NAME,
                        MoviesContract.ReviewEntry.COLUMN_MOVIE_KEY + "= ?",
                        new String[]{id+""});
                rowsDeleted += db.delete(MoviesContract.VideoEntry.TABLE_NAME,
                        MoviesContract.VideoEntry.COLUMN_MOVIE_KEY + "= ?",
                        new String[]{id+""});
                break;

            }
            case MOVIES:
            {
                //Delete the main row and the foreign rows in others tables
                rowsDeleted = db.delete(
                        MoviesContract.MovieEntry.TABLE_NAME, selection,selectionArgs);
                rowsDeleted += db.delete(MoviesContract.ReviewEntry.TABLE_NAME,
                        MoviesContract.ReviewEntry.COLUMN_MOVIE_KEY + "= ?",
                        selectionArgs);
                rowsDeleted += db.delete(MoviesContract.VideoEntry.TABLE_NAME,
                        MoviesContract.VideoEntry.COLUMN_MOVIE_KEY + "= ?",
                        selectionArgs);
                break;
            }
            case REVIEW:
            {
                rowsDeleted = db.delete(
                        MoviesContract.ReviewEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case VIDEO:
            {
                rowsDeleted = db.delete(
                        MoviesContract.VideoEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unkonw uri: " + uri);
        }
        //Because a null deletes all rows
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match){
            case MOVIE_ID:
            {
                rowsUpdated = db.update(
                        MoviesContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            }
            case REVIEW:
            {
                rowsUpdated = db.update(
                        MoviesContract.ReviewEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case VIDEO:
            {
                rowsUpdated = db.update(
                        MoviesContract.VideoEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unkonw uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match){
            case MOVIES:
                db.beginTransaction();
                returnCount = 0;
                try{
                    for (ContentValues value : values){
                        long _id = db.insert(MoviesContract.MovieEntry.TABLE_NAME,
                                null,value);
                        if (_id != -1){
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            case REVIEW:
            {
                db.beginTransaction();
                returnCount = 0;
                try{
                    for (ContentValues value : values){
                        long _id = db.insert(MoviesContract.ReviewEntry.TABLE_NAME,
                                null,value);
                        if (_id != -1){
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            }
            case VIDEO:
            {
                db.beginTransaction();
                returnCount = 0;
                try{
                    for (ContentValues value : values){
                        long _id = db.insert(MoviesContract.VideoEntry.TABLE_NAME,
                                null,value);
                        if (_id != -1){
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri,values);
        }
    }
}
