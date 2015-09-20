package com.virtu.popularmovies.data.repository.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.virtu.popularmovies.data.ApplicationTestCase;
import com.virtu.popularmovies.data.repository.provider.utils.ProviderTestUtilities;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by virtu on 22/08/2015.
 */
public class TestDb extends ApplicationTestCase{

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {

        getContext().deleteDatabase(MoviesDbHelper.DATABASE_NAME);
    }

    /*
    This function gets called before each test is executed to delete the database.  This makes
    sure that we always have a clean test.
    */
    @Before
    public void setUp(){
        deleteTheDatabase();
    }

    @Test
    public void testCreateDb() throws Throwable{
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MoviesContract.MovieEntry.TABLE_NAME);

        getContext().deleteDatabase(MoviesDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MoviesDbHelper(
                getContext()).getWritableDatabase();
        assertThat(db.isOpen(),is(true));
        //assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertThat("Error: This means that the database has not been created correctly",
                c.moveToFirst(),is(true));
        //assertTrue("Error: This means that the database has not been created correctly",
        //        c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertThat("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty(),is(true));
       // assertTrue("Error: Your database was created without both the location entry and weather entry tables",
        //        tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.MovieEntry.TABLE_NAME + ")",
                null);

        assertThat("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst(),is(true));
        //assertTrue("Error: This means that we were unable to query the database for table information.",
        //        c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> MovieColumnHashSet = new HashSet<String>();
        MovieColumnHashSet.add(MoviesContract.MovieEntry._ID);
        MovieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_EXPIRATION_CACHE_DATE);
        MovieColumnHashSet.add(MoviesContract.MovieEntry.COLUMN_MOVIE_DATA);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            MovieColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertThat("Error: The database doesn't contain all of the required location entry columns",
                MovieColumnHashSet.isEmpty(),is(true));
        //assertTrue("Error: The database doesn't contain all of the required location entry columns",
        //        MovieColumnHashSet.isEmpty());
        db.close();
    }

    @Test
    public void testMovieTable() {
        insertMovie();
    }
    @Test
    public void testFullInsertMovie(){
        insertMovie();
        insertVideoMovie();
        insertReviewMovie();
    }

    public long insertMovie() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MoviesDbHelper dbHelper = new MoviesDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        ContentValues testValues = ProviderTestUtilities.createMovieValue(false);

        // Third Step: Insert ContentValues into database and get a row ID back
        long locationRowId;
        locationRowId = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertThat(locationRowId, not(-1L));
       // assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                MoviesContract.MovieEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertThat("Error: No Records returned from location query", cursor.moveToFirst(), is(true));
        //assertTrue("Error: No Records returned from location query", cursor.moveToFirst());

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in ProviderTestUtilities to validate the
        // query if you like)
        ProviderTestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertThat("Error: No Records returned from location query",cursor.moveToNext(),is(false));
        //assertFalse( "Error: More than one record returned from location query",
        //       cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return locationRowId;
    }

    public long insertVideoMovie(){
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MoviesDbHelper dbHelper = new MoviesDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        ContentValues videoValues = ProviderTestUtilities.createVideoValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long videoRowId;
        videoRowId = db.insert(MoviesContract.VideoEntry.TABLE_NAME, null, videoValues);

        // Verify we got a row back.
        assertThat(videoRowId, not(-1L));
        // assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                MoviesContract.VideoEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertThat("Error: No Records returned from location query", cursor.moveToFirst(), is(true));
        //assertTrue("Error: No Records returned from location query", cursor.moveToFirst());

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in ProviderTestUtilities to validate the
        // query if you like)
        ProviderTestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                cursor, videoValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertThat("Error: No Records returned from location query",cursor.moveToNext(),is(false));
        //assertFalse( "Error: More than one record returned from location query",
        //       cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return videoRowId;
    }
    public long insertReviewMovie(){
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MoviesDbHelper dbHelper = new MoviesDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        ContentValues reviewValues = ProviderTestUtilities.createReviewValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long reviewRowId;
        reviewRowId = db.insert(MoviesContract.ReviewEntry.TABLE_NAME, null, reviewValues);

        // Verify we got a row back.
        assertThat(reviewRowId, not(-1L));
        // assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                MoviesContract.ReviewEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertThat("Error: No Records returned from location query", cursor.moveToFirst(), is(true));
        //assertTrue("Error: No Records returned from location query", cursor.moveToFirst());

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in ProviderTestUtilities to validate the
        // query if you like)
        ProviderTestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                cursor, reviewValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertThat("Error: No Records returned from location query",cursor.moveToNext(),is(false));
        //assertFalse( "Error: More than one record returned from location query",
        //       cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return reviewRowId;
    }
}
