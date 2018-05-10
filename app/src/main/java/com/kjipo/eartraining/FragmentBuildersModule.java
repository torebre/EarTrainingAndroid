package com.kjipo.eartraining;

import com.kjipo.eartraining.recorder.Recorder;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {


//    @ContributesAndroidInjector
//    abstract ScoreFragment contributeScoreFragment();

    @ContributesAndroidInjector
    abstract NoteViewFragment contributeNoteViewFragment();

    @ContributesAndroidInjector
    abstract Recorder contributeRecorderFragment();


}
