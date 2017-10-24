package com.kjipo.eartraining;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ObjectModel extends BaseObservable {


    public WebViewClient getCustomWebViewClient() {
        return new CustomWebViewClient();


    }



}
