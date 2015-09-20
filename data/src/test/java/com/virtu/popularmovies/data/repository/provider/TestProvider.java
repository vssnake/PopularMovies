package com.virtu.popularmovies.data.repository.provider;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;

import com.virtu.popularmovies.data.ApplicationTestCase;
import com.virtu.popularmovies.data.repository.provider.MoviesContract.MovieEntry;
import com.virtu.popularmovies.data.repository.provider.utils.ProviderTestUtilities;
import com.virtu.popularmovies.data.repository.provider.utils.ProviderUtilities;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

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

        ContentValues movieValue = ProviderTestUtilities.createMovieValue(false);
        ContentValues reviewValue = ProviderTestUtilities.createReviewValues();
        ContentValues videoValue = ProviderTestUtilities.createVideoValues();
        ContentValues allInner = new ContentValues();
        allInner.putAll(movieValue);
        allInner.putAll(reviewValue);
        allInner.putAll(videoValue);
        long movieRowId = ProviderTestUtilities.insertDefaultMovieWithReviewAndVideo(getContext());

        Cursor movieCursor = getContext().getContentResolver().query(
                MovieEntry.buildMovieUri(movieRowId),
                null,
                null,
                null,
                null
        );

        ProviderTestUtilities.validateCursor("testBasicMovieQuery", movieCursor, allInner);

    }


    @Test
    /**
     * This test uses the provider to insert and then update the data.
     * Update favourite and movieID
     */
    public void testUpdateMovie(){
        //Create a new map of values, where column names are the keys
        ContentValues values = ProviderTestUtilities.createMovieValue(true);

        Uri movieUri = getContext().getContentResolver().insert(
                MovieEntry.CONTENT_URI, values);
        long movieRowId = ContentUris.parseId(movieUri);

        //Verfity we got a row back
        assertThat(movieRowId,not(-1L));
        //assertTrue(movieRowId != -1);

        ContentValues updateValues = new ContentValues(values);
        //Update data
        updateValues.put(MovieEntry._ID,movieRowId);
        updateValues.put(MovieEntry.COLUMN_FAVOURITE, 0);

        //Create a cursor with observer to make sure that the content provider is notifying
        //the observer as expected
        Cursor movieCursor = getContext().getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);




        ProviderTestUtilities.TestContentObserver testContentObserver =
                ProviderTestUtilities.getTestContentObserver();
        getContext().getContentResolver().registerContentObserver(MoviesContract.MovieEntry.buildMovieUri(movieRowId),
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

        ProviderTestUtilities.validateCursor("testUpdateMovie. Error validating location entry update.",
                cursor, updateValues);

        cursor.close();

    }

    @Test
    /**
     * Make sure we call still delete after adding/updating stuff
     */
    public void testInsertReadProvider(){
        ContentValues testValues = ProviderTestUtilities.createMovieValue(false);

        //Register a content observer for our insert. This time. directly with the content resolver
        ProviderTestUtilities.TestContentObserver testContentObserver = ProviderTestUtilities.getTestContentObserver();
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

        ProviderTestUtilities.validateCursor("testInsertReadProvider. Error validating MovieEntry",
                cursor, testValues);
    }

    @Test
    /**
     * Make sure we can till delete after adding/updating stuff
     */
    public void testDeleteRecords(){
        testInsertReadProvider();

        //Register a content observer for our location delete.
        ProviderTestUtilities.TestContentObserver movieObserver = ProviderTestUtilities.getTestContentObserver();
        getContext().getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI,
                true, movieObserver);

        deleteAllRecordFromProvider();

        movieObserver.waitForNotificationOrFail();

        getContext().getContentResolver().unregisterContentObserver(movieObserver);
    }

    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static ContentValues[] createBulkInsertMoviesValues(){
        long currentTestDate = ProviderTestUtilities.TEST_DATE;
        long millisecondsInADay = 100*60*60*24;
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i<BULK_INSERT_RECORDS_TO_INSERT; i++, currentTestDate +=millisecondsInADay){
            returnContentValues[i] = ProviderTestUtilities.createVariableValues(i);
        }
        return returnContentValues;
    }


    @Test
    /**
     * Insert a full movie with Reviews and videos
     */
    public void insertFullMovie(){
        testInsertReadProvider();

        ContentValues reviewValues = ProviderTestUtilities.createReviewValues();

        ProviderTestUtilities.TestContentObserver testContentObserver = ProviderTestUtilities.getTestContentObserver();
        getContext().getContentResolver().registerContentObserver(MoviesContract.ReviewEntry.buildReviewUri(1),
                true,
                testContentObserver);
        Uri reviewUri = getContext().getContentResolver().insert(
                MoviesContract.ReviewEntry.buildReviewUri(1), reviewValues);

        //Verify if the insert call the content resolver
        testContentObserver.waitForNotificationOrFail();

        getContext().getContentResolver().unregisterContentObserver(testContentObserver);


        // long reviewRowId = ContentUris.parseId(reviewUri);

        //Verify we got a row back
        assertThat(MoviesContract.ReviewEntry.buildReviewUri(1), is(reviewUri));

        getContext().getContentResolver().registerContentObserver(MoviesContract.VideoEntry.CONTENT_URI,
                true,
                testContentObserver);

        ContentValues videoValues = ProviderTestUtilities.createVideoValues();

        Uri videoUri = getContext().getContentResolver().insert(
                MoviesContract.VideoEntry.buildVideoUri(1), videoValues);

        //Verify if the insert call the content resolver
        testContentObserver.waitForNotificationOrFail();

        getContext().getContentResolver().unregisterContentObserver(testContentObserver);

        //Verify we got a row back
        assertThat(MoviesContract.VideoEntry.buildVideoUri(1), is(videoUri));



        //Query with joins of the three tables
        Cursor fullMoviesCursor = getContext().getContentResolver().query(MoviesContract.MovieEntry.buildMovieUri(1),
                null,
                null,
                null,
                null);

        ProviderTestUtilities.validateWithoutCloseCursor("testingVideoValues. Error validating VideoEntry",
                fullMoviesCursor, videoValues);

        ProviderTestUtilities.validateCursor("testInsertReadProvider. Error validating Review",
                fullMoviesCursor, reviewValues);


        //Now is time to delete
    }

    @Test
    public void deleteFullMovie(){
        insertFullMovie();

        /*getContext().getContentResolver().delete(
                MoviesContract.ReviewEntry.buildReviewUri(1),
                MoviesContract.ReviewEntry.COLUMN_MOVIE_KEY +"= ?",
                new String[]{"1"}
        );

        getContext().getContentResolver().delete(
                MoviesContract.VideoEntry.buildVideoUri(1),
                MoviesContract.VideoEntry.COLUMN_MOVIE_KEY + "= ?",
                new String[]{"1"}
        );*/

        getContext().getContentResolver().delete(
                MovieEntry.buildMovieUri(1),
                null,
                null
        );



        //Query with joins of the three tables
        Cursor fullMoviesCursor = getContext().getContentResolver().query(MoviesContract.MovieEntry.buildMovieUri(1),
                null,
                null,
                null,
                null);
        assertThat(0, is(fullMoviesCursor.getCount()));
        fullMoviesCursor.close();

        //Query with joins of the three tables
        Cursor reviewCursor = getContext().getContentResolver().query(MoviesContract.ReviewEntry.buildReviewUri(1),
                null,
                MoviesContract.ReviewEntry.COLUMN_MOVIE_KEY +"= ?",
                new String[]{"1"},
                null);
        assertThat(0, is(reviewCursor.getCount()));
        reviewCursor.close();

        //Query with joins of the three tables
        Cursor videoCursor = getContext().getContentResolver().query(MoviesContract.VideoEntry.buildVideoUri(1),
                null,
                MoviesContract.VideoEntry.COLUMN_MOVIE_KEY +"= ?",
                new String[]{"1"},
                null);
        assertThat(0, is(videoCursor.getCount()));
        videoCursor.close();



    }


    @Test
    /**
     * Bulk insert ContentProvider function
     */
    public void testBulkInsert(){

        //Now we can bulkInsert some weather.
        ContentValues[] bulkInsertContentValues = createBulkInsertMoviesValues();

        ProviderTestUtilities.TestContentObserver movieObserver = ProviderTestUtilities.getTestContentObserver();
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
            ProviderTestUtilities.validateCurrentRecord("testBulkInsert. Error validating MovieEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}
