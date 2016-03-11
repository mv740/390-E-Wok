package com.example.nspace.museedesondes;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by Harrison on 2016-03-01.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MapActivityTest {

    @Rule
    public ActivityTestRule<MapActivity> mActivityRule = new ActivityTestRule<>(MapActivity.class);

    @Test
    public void testOnHamClick() throws Exception {
        onView(withId(R.id.hamburger)).check(matches(isDisplayed()));
    }

    @Test
    public void testTracePath() throws Exception {

    }

    @Test
    public void testListNodeCoordinates() throws Exception {

    }

    @Test
    public void testPoiImgOnClick() throws Exception {

    }

    @Test
    public void testFloorButtonOnClick() throws Exception {

    }

    @Test
    public void testChangeFloor() throws Exception {

    }

    @Test
    public void testPlayAudioFile() throws Exception {

    }
}