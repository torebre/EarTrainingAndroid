package com.kjipo.eartraining;


import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kjipo.eartraining.svg.SvgModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;


public class EarTrainingApplication extends Application {
    private MainComponent mainComponent;

    private static final String GLYPH_URL = "file:///android_asset/js/glyphs.json";


    @Override
    public void onCreate() {
        super.onCreate();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();



        try(InputStream inputStream =  getAssets().open(GLYPH_URL);
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader)) {
            Glyphs glyphs = gson.fromJson(bufferedReader, Glyphs.class);

            mainComponent = DaggerMainComponent.builder()
                    .serviceModule(new ServiceModule())
                    .svgModule(new SvgModule(glyphs))
                    .build();

        } catch (IOException e) {
            // TODO Use logger
            e.printStackTrace();

            throw new RuntimeException(e);
        }

    }


    public MainComponent getMainComponent() {
        return mainComponent;
    }
}
