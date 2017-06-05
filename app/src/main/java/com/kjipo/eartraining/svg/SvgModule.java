package com.kjipo.eartraining.svg;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SvgModule {
    private final Glyphs glyphs;


    public SvgModule(Glyphs glyphs) {
        this.glyphs = glyphs;
    }


    @Provides
    @Singleton
    public SequenceToSvg getSequenceToSvg() {
        return new SequenceToSvg(glyphs);
    }

}
