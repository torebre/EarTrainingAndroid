package com.kjipo.eartraining;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NoteViewModel.class)
    abstract ViewModel bindRepoViewModel(NoteViewModel noteViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(AppViewModelFactory factory);

}
