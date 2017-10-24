package com.kjipo.eartraining;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class FragmentValue<T> {
    private T value;

    public FragmentValue(Fragment fragment, T value) {
        fragment.getFragmentManager().registerFragmentLifecycleCallbacks(
                new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                        if (f == fragment) {
                            FragmentValue.this.value = null;
                            fragment.getFragmentManager().unregisterFragmentLifecycleCallbacks(this);
                        }
                    }
                }, false);
        this.value = value;
    }

    public T get() {
        return value;
    }
}




