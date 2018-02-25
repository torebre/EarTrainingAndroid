package com.kjipo.eartraining;


import android.databinding.DataBindingComponent;
import android.support.v4.app.Fragment;

public class FragmentDataBindingComponent implements DataBindingComponent {
    private final FragmentBindingAdapters adapter;

    public FragmentDataBindingComponent(Fragment fragment) {
        this.adapter = new FragmentBindingAdapters(fragment);
    }

//    @Override
    public FragmentBindingAdapters getFragmentBindingAdapters() {
        return adapter;
    }


}
