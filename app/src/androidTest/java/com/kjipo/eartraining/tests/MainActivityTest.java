package com.kjipo.eartraining.tests;


import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest
            extends ActivityInstrumentationTestCase2<MainActivity> {

        private MainActivity mainActivity;

        public MainActivityTest() {
            super(MainActivity.class);
        }


        @Override
        protected void setUp() throws Exception {
            super.setUp();
            mainActivity = getActivity();

            // TODO


        }
    }



