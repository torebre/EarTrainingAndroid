package com.kjipo.eartraining;

import android.databinding.BindingAdapter;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BindingAdapterUtil {

    @BindingAdapter({ "setWebViewClient" })
    public static void setWebViewClient(WebView view, WebViewClient client) {
        view.setWebViewClient(client);
    }
}
