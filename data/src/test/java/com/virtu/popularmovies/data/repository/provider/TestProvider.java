package com.virtu.popularmovies.data.repository.provider;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriPermission;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.virtu.popularmovies.data.AplicationStub;
import com.virtu.popularmovies.data.ApplicationTestCase;
import com.virtu.popularmovies.data.repository.provider.MoviesContract.MovieEntry;

import org.apache.tools.ant.types.resources.comparators.Content;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.internal.Shadow;
import org.robolectric.res.builder.RobolectricPackageManager;
import org.robolectric.shadows.Provider;
import org.robolectric.shadows.ShadowApplication;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsSame.sameInstance;

/**
 * Created by virtu on 22/08/2015.
 */
public class TestProvider extends ApplicationTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecordFromProvider(){
        getContext().getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = getContext().getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertThat(0, is(cursor.getCount()));
        cursor.close();

    }

    public void deleteAllRecords(){
        deleteAllRecordFromProvider();
    }

    @Before
    public void setUp() throws Exception{
        deleteAllRecords();
    }

    @Test
    /*
    This test check to make sure that the content provider is registered correctly
     */
    public void testProviderRegistry(){
        PackageManager pm = getContext().getPackageManager();

        /**
         * We define the component name bases on the package name from the context and the
         * MovieProvider class
         */
        ComponentName componentName = new ComponentName(getContext().getPackageName(),
                MoviesProvider.class.getCanonicalName());

        try{
            ShadowApplication shadowApplication = Shadows.shadowOf(RuntimeEnvironment.application);
            PackageManager shadowPackageManger = shadowApplication.getPackageManager();

            ProviderInfo providerInfo = shadowPackageManger.getProviderInfo(componentName,0);
            assertThat("Error: MovieProvider with authority: " + providerInfo.authority +
                    "instead of authority: " + MoviesContract.CONTENT_AUTHORITY,
                    providerInfo.authority, sameInstance(MoviesContract.CONTENT_AUTHORITY));
           // assertEquals("Error: MovieProvider with authority: " + providerInfo.authority +
           // "instead of authority: " + MoviesContract.CONTENT_AUTHORITY,
           // providerInfo.authority, MoviesContract.CONTENT_AUTHORITY);

        }catch (PackageManager.NameNotFoundException e){
            assertThat("Error MovieProvider not registered at " + getContext().getPackageName(), true,
                    is(false));
            //assertTrue("Error MovieProvider not registered at " + getContext().getPackageName(),false);
        }
    }

    @Test
    /**
     * This test doesn´t touch the database. It verifies that the ContentProvider returns the
     * correct type for earch type of URI that it can handle.
     */
    public void testGetType(){
        String type = getContext().getContentResolver().getType(MovieEntry.CONTENT_URI);

        assertThat("Error: the MoviesEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                MovieEntry.CONTENT_TYPE, is(type));
    }

    @Test
    /**
     * This test uses the database directly to insert a movie and then uses the content provider
     *  to read out the data.
     */
    public void testBasicMovieQuery(){
       //Insert our test records into the database
        MoviesDbHelper dbHelper = new MoviesDbHelper(getContext());

        ContentValues movieValue = TestUtilities.createMovieValue();
        ContentValues reviewValue = TestUtilities.createReviewValues();
        ContentValues videoValue = TestUtilities.createVideoValues();
        ContentValues allInner = new ContentValues();
        allInner.putAll(movieValue);
        allInner.putAll(reviewValue);
        allInner.putAll(videoValue);
        long movieRowId = TestUtilities.insertDefaultMovieWithReviewAndVideo(getContext());

        Cursor movieCursor = getContext().getContentResolver().query(
                MovieEntry.buildMovieUri(movieRowId),
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBasicMovieQuery", movieCursor, allInner);

    }


    @Test
    /**
     * This test uses the provider to insert and then update the data.
     */
    public void testUpdateMovie(){
        //Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createMovieValue();

        Uri movieUri = getContext().getContentResolver().insert(
                MovieEntry.CONTENT_URI, values);
        long movieRowId = ContentUris.parseId(movieUri);

        //Verfity we got a row back
        assertThat(movieRowId,not(-1L));
        //assertTrue(movieRowId != -1);

        ContentValues updateValues = new ContentValues(values);
        updateValues.put(MovieEntry._ID,movieRowId);
        updateValues.put(MovieEntry.COLUMN_FAVOURITE, 0);

        //Create a cursor with observer to make sure that the content provider is notifying
        //the observer as expented
        Cursor movieCursor = getContext().getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);




        TestUtilities.TestContentObserver testContentObserver =
                TestUtilities.getTestContentObserver();
        getContext().getContentResolver().registerContentObserver( MoviesContract.MovieEntry.buildMovieUri(movieRowId),
                true,
                testContentObserver);
        int count = getContext().getContentResolver().update(
                MoviesContract.MovieEntry.buildMovieUri(movieRowId), updateValues, MovieEntry._ID + "= ?",
                new String[]{Long.toString(movieRowId)});
        testContentObserver.waitForNotificationOrFail();
        assertThat(count, is(1));


        //Test to make sure our observer is called. If not, we throw an assertion.

        //assertEquals(count,1);
        getContext().getContentResolver().unregisterContentObserver(testContentObserver);

        movieCursor.close();

        //A cursor is your primary interface to query result.

        Cursor cursor = getContext().getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                MovieEntry._ID + " = " + movieRowId,
                null,
                null
        );

        TestUtilities.validateCursor("testUpdateMovie. Error validating location entry update.",
                cursor,updateValues);

        cursor.close();

    }

    @Test
    /**
     * Make sure we call still delete after adding/updating stuff
     */
    public void testInsertReadProvider(){
        ContentValues testValues = TestUtilities.createMovieValue();

        //Register a content observer for our insert. This time. directly with the content resolver
        TestUtilities.TestContentObserver testContentObserver = TestUtilities.getTestContentObserver();
        getContext().getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI,
                true,
                testContentObserver);
        Uri movieUri = getContext().getContentResolver().insert(MovieEntry.CONTENT_URI, testValues);

        //Verify if the insert call the content resolver
        testContentObserver.waitForNotificationOrFail();
        getContext().getContentResolver().unregisterContentObserver(testContentObserver);

        long movieRowId = ContentUris.parseId(movieUri);

        //Verify we got a row back
        assertThat(movieRowId, not(-1L));
       // assertTrue(movieRowId != -1);

        //Data´s inserted. In Theory. Now pull some out to stare at it and verify it made the
        //round trip.

        //A cursor is your primary interface to the query result
        Cursor cursor = getContext().getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieEntry",
                cursor, testValues);
    }

    @Test
    /**
     * Make sure we can till delete after adding/updating stuff
     */
    public void testDeleteRecords(){
        testInsertReadProvider();

        //Register a content observer for our location delete.
        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        getContext().getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI,
                true, movieObserver);

        deleteAllRecordFromProvider();

        movieObserver.waitForNotificationOrFail();

        getContext().getContentResolver().unregisterContentObserver(movieObserver);
    }

    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static ContentValues[] createBulkInsertMoviesValues(){
        long currentTestDate = TestUtilities.TEST_DATE;
        long millisecondsInADay = 100*60*60*24;
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i<BULK_INSERT_RECORDS_TO_INSERT; i++, currentTestDate +=millisecondsInADay){
            returnContentValues[i] = TestUtilities.createVariableValues(i);
        }
        return returnContentValues;
    }

    @Test
    /**
     * Bulk insert ContentProvider function
     */
    public void testBulkInsert(){

        //Now we can bulkInsert some weather.
        ContentValues[] bulkInsertContentValues = createBulkInsertMoviesValues();

        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        getContext().getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true,
                movieObserver);

        int insertCount = getContext().getContentResolver().bulkInsert(MovieEntry.CONTENT_URI,
                bulkInsertContentValues);

        movieObserver.waitForNotificationOrFail();
        getContext().getContentResolver().unregisterContentObserver(movieObserver);

        assertThat(insertCount, is(BULK_INSERT_RECORDS_TO_INSERT));
        //assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        Cursor cursor = getContext().getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                MovieEntry._ID
        );

        //we should have as many record in the database as we have inserted
        assertThat(cursor.getCount(), is(BULK_INSERT_RECORDS_TO_INSERT));
        //assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        cursor.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()){
            TestUtilities.validateCurrentRecord("testBulkInsert. Error validating WeatherEntry " + i,
            cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}
