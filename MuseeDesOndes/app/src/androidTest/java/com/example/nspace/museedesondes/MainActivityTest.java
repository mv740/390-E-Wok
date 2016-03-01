package com.example.nspace.museedesondes;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.Button;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by michal on 2/18/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    //language test use conditions because you need need to delete app to get that "please select language menu again"

    @Test
    public void selectFrenchLanguage() {

        try {
            onView(withText("FRANÇAIS")).check(matches(isDisplayed()));
            onView(withText("FRANÇAIS")).perform(ViewActions.click());
            onView(withText("DÉBUTER LA VISITE")).check(matches(isDisplayed()));
            //view is displayed logic
        } catch (NoMatchingViewException e) {
            //view not displayed logic
        }
    }

    @Test
    public void selectEnglishLanguage() {

        try {
            onView(withText("ENGLISH")).check(matches(isDisplayed()));
            onView(withText("ENGLISH")).perform(ViewActions.click());
            onView(withText("BEGIN TOUR")).check(matches(isDisplayed()));
            //view is displayed logic
        } catch (NoMatchingViewException e) {
            //view not displayed logic
        }
    }

    @Test
    public void selectBeginTour() {

        onView(withText("BEGIN TOUR")).check(matches(isDisplayed()));
        onView(withText("BEGIN TOUR")).perform(ViewActions.click());
        onView(withText("STORYLINES")).check(matches(isDisplayed()));
    }
}