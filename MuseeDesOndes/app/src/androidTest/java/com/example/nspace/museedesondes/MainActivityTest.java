package com.example.nspace.museedesondes;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by michal on 2/18/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    //all test are always in english because you can't restart app during test to select other language
    // also need to delete app from emulator/phone between test. Because choosing language is not showed if it is already save in a profile
    @Test
    public void selectEnglishLanguage() {

        onView(withText("ENGLISH")).check(matches(isDisplayed()));
        onView(withText("ENGLISH")).perform(click());
        onView(withId(R.id.begin_tour_button)).check(matches(isDisplayed()));

    }

    @Test
    public void selectStorylineButton() {

        onView(withText(R.string.begin_tour_button_text))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(R.string.storyline_activity)).check(matches(isDisplayed()));
    }
}