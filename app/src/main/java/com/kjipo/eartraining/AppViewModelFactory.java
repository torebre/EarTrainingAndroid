package com.kjipo.eartraining;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class AppViewModelFactory implements ViewModelProvider.Factory {
    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    @Inject
    public AppViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {

        // TODO Figure out how this works

        this.creators = creators;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return null;
    }
}
