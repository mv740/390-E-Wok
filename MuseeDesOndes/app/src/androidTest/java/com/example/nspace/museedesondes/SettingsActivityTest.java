package com.example.nspace.museedesondes;

import android.app.Activity;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.nspace.museedesondes.utility.TextPicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.runner.lifecycle.Stage.RESUMED;

/**
 * Created by michal on 3/13/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SettingsActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void initValidLocale() {
        onView(withId(R.id.begin_tour_button))
                .check(matches(isDisplayed()))
                .perform(click());
        StoryLineActivity storyLineActivity = (StoryLineActivity) getActivityInstance();
        //always last card = free exploration
        onView(withId(R.id.material_listview)).perform(RecyclerViewActions.scrollToPosition(storyLineActivity.getCardsNumbers() - 1));
        onView(withText(R.string.free_exploration)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.hamburger)).perform(click());
        onView(withText("Language")).perform(click());
    }

    @Test
    public void testTitle() throws Exception {

        onView(withText(R.string.settings_activity)).check(matches(isDisplayed()));
    }

    @Test
    public void testLanguageChoices() throws Exception {
        onView(withClassName(Matchers.equalTo(TextPicker.class.getName()))).check(matches(isDisplayed()));
        onView(withText("English")).check(matches(isDisplayed()));
    }

    @Test
    public void testSelectLanguage() throws Exception {

        onView(withId(R.id.ok_button)).check(matches(isDisplayed())).perform(click());
        onView(withText(R.string.settings_activity)).check(doesNotExist());
    }

    public Activity getActivityInstance() {

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