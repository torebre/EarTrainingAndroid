package com.kjipo.eartraining;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {


    @ContributesAndroidInjector
    abstract ScoreFragment contributeScoreFragment();

    @ContributesAndroidInjector
    abstract NoteViewFragment contributeNoteViewFragment();


}
