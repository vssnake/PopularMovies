package com.virtu.popularmovies.data;

/**
 * Created by unai on 20/08/15.
 */
import com.virtu.spotifystreamer.data.BuildConfig;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * Base class for Robolectric data layer tests.
 * Inherit from this class to create a test.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, application = AplicationStub.class)
public abstract class ApplicationTestCase {}
