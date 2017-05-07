package com.kjipo.eartraining;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kjipo.eartraining.score.ScoreActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.web.sugar.Web.onWebView;

@RunWith(AndroidJUnit4.class)
public class ScoreScreenTest {

    @Rule
    public ActivityTestRule<ScoreActivity> mActivityRule = new ActivityTestRule<ScoreActivity>(ScoreActivity.class,
            false, true) {
        @Override
        protected void afterActivityLaunched() {
            // Enable JavaScript
            onWebView().forceJavascriptEnabled();
        }
    };

    @Test
    public void espressoTest() {
        onView(withId(R.id.scoreLayout))
                .perform(click());

        // TODO Create test case



    }


}
