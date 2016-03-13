package com.example.nspace.museedesondes;

import android.app.Dialog;
import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Harrison on 2016-03-01.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MapActivityTest {

    //application use language locale, only the first activity load the language preference setting
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void initValidLocale()
    {
        onView(withId(R.id.begin_tour_button))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText("FREE EXPLORATION"))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText("OK"))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void testOnHamClick() throws Exception {
        onView(withId(R.id.hamburger))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText("Storylines")).check(matches(isDisplayed()));
        onView(withText("Language")).check(matches(isDisplayed()));
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

        onView(withId(R.id.floor_button)).check(matches(isDisplayed())).perform(click());

    }

    @Test
    public void testChangeFloor() throws Exception {


    }

    @Test
    public void testPlayAudioFile() throws Exception {

    }


}