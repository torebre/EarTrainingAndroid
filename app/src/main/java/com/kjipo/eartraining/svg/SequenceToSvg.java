package com.kjipo.eartraining.svg;


import com.kjipo.eartraining.data.Note;

import java.util.List;


public class SequenceToSvg {

    private final Glyphs glyphs;


    public SequenceToSvg(Glyphs glyphs) {
        this.glyphs = glyphs;
    }


    public void transformToSvg(List<Note> temporalElements, SvgSequenceConfig svgSequenceConfig) {
        int barWidth = svgSequenceConfig.getBarWidth();

        // TODO


//        ("file:///android_asset/js/glyphs.json");

//        Gson gson = new Gson();
//        gson.fromJson()


        for (Note temporalElement : temporalElements) {
            // TODO Just for testing
            drawPathAt(1, glyphs.getPath(temporalElement.getElementType()));


        }


    }


    private void drawPathAt(int xcoord, String path) {
        // TODO

    }


}
