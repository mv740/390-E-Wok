package com.example.nspace.museedesondes;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.content.ContextCompat;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import com.example.nspace.museedesondes.utility.MapManager;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.model.GroundOverlay;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.runner.lifecycle.Stage.RESUMED;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Collection;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
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

    //application use language locale, only the first activity load the language preference setting
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void initValidLocale()
    {
        onView(withId(R.id.begin_tour_button))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(R.string.free_exploration))
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
        MapActivity mapActivity = (MapActivity) getActivityInstance();

        final FloatingActionButton floatingActionButton = (FloatingActionButton) mapActivity.findViewById(R.id.fab2);

        //Assert.assertEquals(floatingActionButton.getColorNormal(),R.color.rca_onclick);
        Assert.assertEquals(floatingActionButton.getColorNormal(), ContextCompat.getColor(mapActivity.getApplicationContext(), R.color.rca_onclick));
        GroundOverlay groundOverlay = mapActivity.getMapManager().getGroundOverlayFloorMap();
        mapActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                floatingActionButton.setVisibility(View.VISIBLE);
            }
        });
        onView(withId(R.id.fab2)).perform(click());
        //image changed
        Assert.assertNotSame(groundOverlay.hashCode(),mapActivity.getMapManager().getGroundOverlayFloorMap().hashCode());
        Assert.assertEquals(floatingActionButton.getColorNormal(), ContextCompat.getColor(mapActivity.getApplicationContext(), R.color.rca_primary));

    }

    @Test
    public void testZoomIn() throws Exception {

        MapActivity mapActivity = (MapActivity) getActivityInstance();
        MapManager mapManager = mapActivity.getMapManager();
        assertEquals(mapManager.getZoomLevel(), 1);
        onView(withId(R.id.zoomInButton)).check(matches(isDisplayed())).perform(click());
        assertEquals(mapManager.getZoomLevel(), 2);
    }

    @Test
    public void testZoomOut() throws Exception {
        MapActivity mapActivity = (MapActivity) getActivityInstance();
        MapManager mapManager = mapActivity.getMapManager();
        assertEquals(mapManager.getZoomLevel(), 1);
        onView(withId(R.id.zoomInButton)).check(matches(isDisplayed())).perform(click());
        assertEquals(mapManager.getZoomLevel(), 2);
        onView(withId(R.id.zoomOutButton)).check(matches(isDisplayed())).perform(click());
        assertEquals(mapManager.getZoomLevel(), 1);

    }

    @Test
    public void testPlayAudioFile() throws Exception {
    }


    @Test
    public void testClickMarker() throws Exception {

        MapActivity mapActivity = (MapActivity) getActivityInstance();
        String title = mapActivity.getInformation().getPointOfInterests().get(0).getLocaleDescription(mapActivity.getApplicationContext()).getTitle();

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains(title));
        marker.click();


    }

    @Test
    public void testFullScreenImage() throws Exception {



    }

    //helper get current activity from https://gist.github.com/elevenetc/df58a6ee4b776edb67c2
    //http://stackoverflow.com/questions/24517291/get-current-activity-in-espresso-android/34084377#34084377
    public Activity getActivityInstance(){

        final Activity[] currentActivity = new Activity[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    currentActivity[0] = resumedActivities.iterator().next();
                }
            }
        });


        return currentActivity[0];
    }


}