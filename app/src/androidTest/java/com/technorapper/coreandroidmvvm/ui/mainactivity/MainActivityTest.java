package com.technorapper.coreandroidmvvm.ui.mainactivity;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.technorapper.coreandroidmvvm.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    @Test
    public void btn_click() {



    }
    @Test
    public void btn_show() {
        Espresso.onView(withId(R.id.btnPrev)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.btnNext)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.img)).check(matches(isDisplayed()));


        Espresso.onView(withId(R.id.btnNext)).perform(click());
    }


}