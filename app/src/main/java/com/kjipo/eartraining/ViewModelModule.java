package com.kjipo.eartraining;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.MapKey;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
//
//        @Binds
//        @IntoMap
//        @ViewModelKey(UserViewModel.class)
//        abstract ViewModel bindUserViewModel(UserViewModel userViewModel);
//
//        @Binds
//        @IntoMap
//        @ViewModelKey(SearchViewModel.class)
//        abstract ViewModel bindSearchViewModel(SearchViewModel searchViewModel);
//
        @Binds
        @IntoMap
        @ViewModelKey(NoteViewModel.class)
        abstract ViewModel bindRepoViewModel(NoteViewModel noteViewModel);

        @Binds
        abstract ViewModelProvider.Factory bindViewModelFactory(AppViewModelFactory factory);

}
