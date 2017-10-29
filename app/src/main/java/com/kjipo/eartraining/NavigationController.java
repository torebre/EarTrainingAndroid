package com.kjipo.eartraining;


import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;


    @Inject
    public NavigationController(MainActivity mainActivity) {
        containerId = R.id.frame_container;
        fragmentManager = mainActivity.getSupportFragmentManager();
    }


    public void navigateToNoteView() {
        NoteViewFragment noteViewFragment = new NoteViewFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, noteViewFragment)
                .commitAllowingStateLoss();


    }


}
