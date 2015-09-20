package com.virtu.popularmovies.data.repository.provider.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;


import com.virtu.popularmovies.data.repository.provider.MoviesContract;
import com.virtu.popularmovies.data.repository.provider.MoviesDbHelper;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ProviderTestUtilities {
    public static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    public static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertThat("Empty cursor returned. " + error, valueCursor.moveToFirst(), is(true));
        //assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }
    public static void validateWithoutCloseCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertThat("Empty cursor returned. " + error, valueCursor.moveToFirst(), is(true));
        //assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
    }

    public static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertThat("Column '" + columnName + "' not found. " + error,
                    idx, not(-1));
            //assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertThat("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error,expectedValue,is(valueCursor.getString(idx)));
            //assertEquals("Value '" + entry.getValue().toString() +
            //       "' did not match the expected value '" +
            //        expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }



    public static ContentValues createMovieValue(boolean favourite) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.MovieEntry._ID, 1L);
        testValues.put(MoviesContract.MovieEntry.COLUMN_EXPIRATION_CACHE_DATE, 12345L);
        if (favourite){
            testValues.put(MoviesContract.MovieEntry.COLUMN_FAVOURITE,1);
        }else{
            testValues.put(MoviesContract.MovieEntry.COLUMN_FAVOURITE,0);
        }
        testValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_DATA, "TESTING");

        return testValues;
    }
    public static ContentValues createVariableValues(int seed){
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.MovieEntry._ID, 1L + (long)seed);
        testValues.put(MoviesContract.MovieEntry.COLUMN_EXPIRATION_CACHE_DATE,
                12345L + (long) seed);
        testValues.put(MoviesContract.MovieEntry.COLUMN_FAVOURITE,1);
        testValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_DATA, "TESTING Number" + seed);

        return testValues;
    }

    public static ContentValues createReviewValues(){
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.ReviewEntry.COLUMN_MOVIE_KEY, 1L);
        testValues.put(MoviesContract.ReviewEntry.COLUMN_REVIEW_DATA,"data");
        return testValues;
    }
    public static ContentValues createVideoValues(){
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.VideoEntry.COLUMN_MOVIE_KEY,1L);
        testValues.put(MoviesContract.VideoEntry.COLUMN_VIDEO_DATA, "data");
        return testValues;
    }


    public static long insertDefaultMovieValues(Context context) {
        // insert our test records into the database
        MoviesDbHelper dbHelper = new MoviesDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = ProviderTestUtilities.createMovieValue(false);


        long locationRowId;
        locationRowId = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertThat("Error: Failure to insert North Pole Location Values", locationRowId, not(-1L));
        //assertTrue("Error: Failure to insert North Pole Location Values", locationRowId != -1);

        return locationRowId;
    }

    public static long insertDefaultMovieWithReviewAndVideo(Context context){
        // insert our test records into the database
        MoviesDbHelper dbHelper = new MoviesDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testMovieValues = ProviderTestUtilities.createMovieValue(false);
        ContentValues testReviewValues = ProviderTestUtilities.createReviewValues();
        ContentValues testVideoValues = ProviderTestUtilities.createVideoValues();

        long locationRowId;
        long originalRowID;
        locationRowId = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, testMovieValues);
        originalRowID = locationRowId;
        // Verify we got a row back.
        assertThat("Error: Failure to insert North Pole Location Values", locationRowId, not(-1L));
        locationRowId = db.insert(MoviesContract.ReviewEntry.TABLE_NAME, null, testReviewValues);
        // Verify we got a row back.
        assertThat("Error: Failure to insert North Pole Location Values", locationRowId, not(-1L));
        locationRowId = db.insert(MoviesContract.VideoEntry.TABLE_NAME, null, testVideoValues);
        // Verify we got a row back.
        assertThat("Error: Failure to insert North Pole Location Values", locationRowId, not(-1L));

        return originalRowID;
    }

    /*
        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    public static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    public static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
