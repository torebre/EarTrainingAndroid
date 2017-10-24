package com.kjipo.eartraining;


import android.support.v4.app.Fragment;

public class FragmentDataBindingComponent implements android.databinding.DataBindingComponent {
    private final FragmentBindingAdapters adapter;

    public FragmentDataBindingComponent(Fragment fragment) {
        this.adapter = new FragmentBindingAdapters(fragment);
    }

    public FragmentBindingAdapters getFragmentBindingAdapters() {
        return adapter;
    }


}
