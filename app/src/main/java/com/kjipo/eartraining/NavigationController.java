package com.kjipo.eartraining;


import android.app.FragmentManager;

import javax.inject.Inject;

public class NavigationController {
//    private final int containerId;
    private FragmentManager fragmentManager;


    @Inject
    public NavigationController(MainActivity mainActivity) {
        fragmentManager = mainActivity.getFragmentManager();
    }


    public void navigateToStart() {


    }


    public void navigatetoNoteView() {
        NoteViewFragment noteViewFragment = new NoteViewFragment();
//        fragmentManager.beginTransaction()
//                .replace()


    }


}
