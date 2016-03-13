package com.example.nspace.museedesondes;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by michal on 3/12/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class StoryLineActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void initValidLocale()
    {
        onView(withId(R.id.begin_tour_button))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void testValidateTitle()
    {
        onView(withText(R.string.storyline_activity)).check(matches(isDisplayed()));
    }

    @Test
    public void testSelectingFreeExploration()
    {
        onView(withText(R.string.free_exploration)).perform(click());
        onView(withText(R.string.storyline_activity)).check(doesNotExist());
    }

    @Test
    public void testStorylinesExist()
    {
        onView(withContentDescription(R.style.CardView));
    }

}