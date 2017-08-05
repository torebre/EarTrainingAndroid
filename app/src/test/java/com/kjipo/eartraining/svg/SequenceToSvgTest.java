package com.kjipo.eartraining.svg;


import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kjipo.eartraining.data.ElementType;
import com.kjipo.eartraining.data.Note;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.junit.Test;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.svg.SVGSVGElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class SequenceToSvgTest {
    private static final String SVG_NAMESPACE_URI = "http://www.w3.org/2000/svg";

    @Test
    public void generateSequence() throws IOException, ParserConfigurationException, TransformerException {
        SvgSequenceConfig svgSequenceConfig = new SvgSequenceConfig(4);

        Map<ElementType, String> typePathMap = ImmutableMap.<ElementType, String>builder()
                .put(ElementType.G_CLEF, "M 0 0 m 869.545 406.87 c -7.621 2.8 -37.638 31.68 -54.954 52.88 c -56.862 69.564 -87.028 156.64 -87.028 247.587 c 0 23.399 1.997 47.054 6.049 70.732 c 1.037 6.221 16.486 79.528 19.701 93.577 l 8.813 40.957 l 7.984 37.586 l -22.137 22.293 c -63.405 63.715 -115.248 124.735 -166.573 195.967 c -47.685 66.184 -71.258 141.493 -71.258 215.793 c 0 86.452 31.915 171.537 94.899 239.288 c 63.774 68.645 157.378 107.88 248.941 107.88 c 24.405 -0 48.665 -2.788 72.176 -8.548 c 4.416 -1.08 8.135 -1.927 8.565 -1.927 c 0.025 -0 0.038 0.003 0.041 0.009 c 0.156 0.207 20.323 94.251 22.863 106.849 c 5.362 26.344 8.043 50.971 8.043 73.803 c 0 25.739 -3.407 49.198 -10.22 70.269 c -21.962 67.944 -89.714 113.801 -162.345 113.801 c -9.667 -0 -19.421 -0.812 -29.164 -2.493 c -15.916 -2.748 -35.513 -8.917 -46.556 -14.724 l -2.436 -1.244 l 4.51 -0.052 c 51.947 -0.259 94.251 -43.704 94.251 -96.843 v -0.064 c 0 -60.903 -49.755 -100.629 -102.135 -100.629 c -25.878 -0 -52.397 9.696 -73.873 31.327 c -21.275 21.412 -32.045 48.407 -32.045 75.406 c 0 23.857 8.409 47.717 25.409 67.733 c 42.01 49.483 102.251 75.143 162.599 75.143 c 45.999 -0 92.061 -14.909 130.16 -45.54 c 50.448 -40.604 75.529 -97.486 75.529 -171.686 c 0 -13.339 -0.81 -27.237 -2.43 -41.702 c -2.073 -18.871 -4.665 -32.505 -17.574 -92.747 c -6.688 -31.417 -12.184 -57.287 -12.184 -57.495 c 0 -0.259 2.644 -1.555 5.859 -2.903 c 96.07 -41.129 160.99 -135.723 160.99 -233.392 c 0 -2.42 -0.04 -4.842 -0.121 -7.265 c -4.095 -124.321 -91.399 -221.008 -213.387 -236.199 c -3.777 -0.469 -12.737 -0.694 -22.131 -0.694 c -11.362 -0 -23.358 0.329 -27.586 0.954 c -2.886 0.412 -5.368 0.743 -5.942 0.743 c -0.075 -0 -0.118 -0.005 -0.124 -0.018 c -0.052 -0.051 -8.399 -39.193 -18.612 -86.941 l -18.56 -86.837 l 9.28 -8.969 l 24.833 -23.796 c 93.318 -88.601 139.77 -181.659 147.028 -294.626 c 0.414 -6.45 0.618 -13.029 0.618 -19.713 c 0 -95.296 -41.3 -211.963 -100.209 -279.061 c -15.417 -17.58 -25.878 -25.36 -34.587 -25.36 c -1.716 -0 -3.365 0.3 -4.97 0.89 z M 0 0 m 915.167 549.751 c 15.294 2.022 28.203 10.576 34.062 22.448 c 5.571 11.3 8.975 33.439 8.975 54.775 c 0 6.856 -0.351 13.628 -1.095 19.932 c -8.969 76.313 -53.658 162.114 -127.068 243.871 c -6.955 7.779 -30.547 32.042 -31.149 32.042 l -0.009 -0.003 c -0.415 -0.415 -21.567 -100.784 -22.707 -107.679 c -2.871 -17.671 -4.287 -35.201 -4.287 -52.382 c 0 -54.194 14.092 -104.918 41.095 -145.659 c 26.619 -40.244 66.071 -67.745 96.091 -67.745 c 2.079 -0 4.112 0.132 6.092 0.4 z M 0 0 m 808.992 1132.055 c 5.237 24.574 9.799 46.089 10.214 47.8 c 1.651 7.172 12.546 58.512 12.546 59.045 v 0.004 c -0.104 0.052 -1.452 0.519 -3.007 1.037 c -85.973 28.644 -140.686 106.965 -140.686 190.863 c 0 19.058 2.823 38.403 8.744 57.519 c 16.02 51.791 54.799 94.925 104.257 116.025 c 3.895 1.667 7.512 2.476 10.718 2.476 c 7.166 -0 12.28 -4.043 13.856 -11.6 c 0.261 -1.269 0.396 -2.461 0.396 -3.596 c 0 -5.634 -3.342 -9.829 -11.283 -14.705 c -37.122 -22.787 -55.619 -61.015 -55.619 -100.107 c 0 -39.798 19.171 -80.49 57.382 -106.696 c 10.145 -6.941 34.561 -18.519 38.779 -18.519 c 0.287 -0 0.481 0.054 0.57 0.166 c 0.103 0.104 6.065 27.84 13.272 61.642 l 14.101 66.1 l 1.763 8.036 l 8.761 40.956 l 21.567 100.784 c 7.414 34.786 13.479 63.508 13.479 63.819 c 0 1.451 -22.552 7.465 -36.29 9.746 c -12.705 2.103 -25.384 3.127 -37.946 3.127 c -98.609 -0 -189.955 -63.113 -229.825 -162.856 c -13.514 -33.867 -20.178 -70.07 -20.178 -106.672 c 0 -56.073 15.64 -113.081 46.255 -164.054 c 15.502 -25.766 55.836 -79.165 93.37 -123.646 c 18.197 -21.567 53.917 -61.434 55.006 -61.434 c 0.156 -0 4.562 20.115 9.798 44.74 z M 0 0 m 913.923 1346.842 c 86.237 9.468 149.006 85.297 149.006 169.824 c 0 13.598 -1.624 27.422 -5.037 41.23 c -10.524 42.616 -38.053 81.913 -76.261 108.768 c -6.378 4.519 -20.001 12.832 -20.993 12.832 c -0.032 -0 -0.051 -0.009 -0.056 -0.027 c -0.155 -0.467 -6.895 -31.832 -8.969 -41.578 c -0.777 -3.837 -15.138 -70.974 -31.831 -149.102 c -16.694 -78.18 -30.381 -142.258 -30.381 -142.413 c 0 -0.379 2.392 -0.553 5.903 -0.553 c 5.138 -0 12.675 0.372 18.619 1.019 z ")
                .build();

        Glyphs glyphs = new Glyphs(typePathMap);

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try (Writer writer = Files.newWriter(new File("glyphs.json"), StandardCharsets.UTF_8)) {
            String json = gson.toJson(glyphs);
            writer.write(json);
        }


        SequenceToSvg sequenceToSvg = new SequenceToSvg(glyphs);
        Note note = new Note(1, 60, 1, ElementType.HALFNOTE, 2);
        sequenceToSvg.transformToSvg(Collections.singletonList(note), svgSequenceConfig);

        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Document doc = impl.createDocument(svgNS, "svg", null);

        SVGDocument svgDocument = (SVGDocument)doc;

//        Element svgRoot = doc.getDocumentElement();

// Set the width and height attributes on the root 'svg' element.

        SVGSVGElement rootElement = svgDocument.getRootElement();
        rootElement.setAttributeNS(null, "width", "400");
        rootElement.setAttributeNS(null, "height", "450");

//        addPath(svgDocument, rootElement, "M 0 0 m 869.545 406.87 c -7.621 2.8 -37.638 31.68 -54.954 52.88 c -56.862 69.564 -87.028 156.64 -87.028 247.587 c 0 23.399 1.997 47.054 6.049 70.732 c 1.037 6.221 16.486 79.528 19.701 93.577 l 8.813 40.957 l 7.984 37.586 l -22.137 22.293 c -63.405 63.715 -115.248 124.735 -166.573 195.967 c -47.685 66.184 -71.258 141.493 -71.258 215.793 c 0 86.452 31.915 171.537 94.899 239.288 c 63.774 68.645 157.378 107.88 248.941 107.88 c 24.405 -0 48.665 -2.788 72.176 -8.548 c 4.416 -1.08 8.135 -1.927 8.565 -1.927 c 0.025 -0 0.038 0.003 0.041 0.009 c 0.156 0.207 20.323 94.251 22.863 106.849 c 5.362 26.344 8.043 50.971 8.043 73.803 c 0 25.739 -3.407 49.198 -10.22 70.269 c -21.962 67.944 -89.714 113.801 -162.345 113.801 c -9.667 -0 -19.421 -0.812 -29.164 -2.493 c -15.916 -2.748 -35.513 -8.917 -46.556 -14.724 l -2.436 -1.244 l 4.51 -0.052 c 51.947 -0.259 94.251 -43.704 94.251 -96.843 v -0.064 c 0 -60.903 -49.755 -100.629 -102.135 -100.629 c -25.878 -0 -52.397 9.696 -73.873 31.327 c -21.275 21.412 -32.045 48.407 -32.045 75.406 c 0 23.857 8.409 47.717 25.409 67.733 c 42.01 49.483 102.251 75.143 162.599 75.143 c 45.999 -0 92.061 -14.909 130.16 -45.54 c 50.448 -40.604 75.529 -97.486 75.529 -171.686 c 0 -13.339 -0.81 -27.237 -2.43 -41.702 c -2.073 -18.871 -4.665 -32.505 -17.574 -92.747 c -6.688 -31.417 -12.184 -57.287 -12.184 -57.495 c 0 -0.259 2.644 -1.555 5.859 -2.903 c 96.07 -41.129 160.99 -135.723 160.99 -233.392 c 0 -2.42 -0.04 -4.842 -0.121 -7.265 c -4.095 -124.321 -91.399 -221.008 -213.387 -236.199 c -3.777 -0.469 -12.737 -0.694 -22.131 -0.694 c -11.362 -0 -23.358 0.329 -27.586 0.954 c -2.886 0.412 -5.368 0.743 -5.942 0.743 c -0.075 -0 -0.118 -0.005 -0.124 -0.018 c -0.052 -0.051 -8.399 -39.193 -18.612 -86.941 l -18.56 -86.837 l 9.28 -8.969 l 24.833 -23.796 c 93.318 -88.601 139.77 -181.659 147.028 -294.626 c 0.414 -6.45 0.618 -13.029 0.618 -19.713 c 0 -95.296 -41.3 -211.963 -100.209 -279.061 c -15.417 -17.58 -25.878 -25.36 -34.587 -25.36 c -1.716 -0 -3.365 0.3 -4.97 0.89 z M 0 0 m 915.167 549.751 c 15.294 2.022 28.203 10.576 34.062 22.448 c 5.571 11.3 8.975 33.439 8.975 54.775 c 0 6.856 -0.351 13.628 -1.095 19.932 c -8.969 76.313 -53.658 162.114 -127.068 243.871 c -6.955 7.779 -30.547 32.042 -31.149 32.042 l -0.009 -0.003 c -0.415 -0.415 -21.567 -100.784 -22.707 -107.679 c -2.871 -17.671 -4.287 -35.201 -4.287 -52.382 c 0 -54.194 14.092 -104.918 41.095 -145.659 c 26.619 -40.244 66.071 -67.745 96.091 -67.745 c 2.079 -0 4.112 0.132 6.092 0.4 z M 0 0 m 808.992 1132.055 c 5.237 24.574 9.799 46.089 10.214 47.8 c 1.651 7.172 12.546 58.512 12.546 59.045 v 0.004 c -0.104 0.052 -1.452 0.519 -3.007 1.037 c -85.973 28.644 -140.686 106.965 -140.686 190.863 c 0 19.058 2.823 38.403 8.744 57.519 c 16.02 51.791 54.799 94.925 104.257 116.025 c 3.895 1.667 7.512 2.476 10.718 2.476 c 7.166 -0 12.28 -4.043 13.856 -11.6 c 0.261 -1.269 0.396 -2.461 0.396 -3.596 c 0 -5.634 -3.342 -9.829 -11.283 -14.705 c -37.122 -22.787 -55.619 -61.015 -55.619 -100.107 c 0 -39.798 19.171 -80.49 57.382 -106.696 c 10.145 -6.941 34.561 -18.519 38.779 -18.519 c 0.287 -0 0.481 0.054 0.57 0.166 c 0.103 0.104 6.065 27.84 13.272 61.642 l 14.101 66.1 l 1.763 8.036 l 8.761 40.956 l 21.567 100.784 c 7.414 34.786 13.479 63.508 13.479 63.819 c 0 1.451 -22.552 7.465 -36.29 9.746 c -12.705 2.103 -25.384 3.127 -37.946 3.127 c -98.609 -0 -189.955 -63.113 -229.825 -162.856 c -13.514 -33.867 -20.178 -70.07 -20.178 -106.672 c 0 -56.073 15.64 -113.081 46.255 -164.054 c 15.502 -25.766 55.836 -79.165 93.37 -123.646 c 18.197 -21.567 53.917 -61.434 55.006 -61.434 c 0.156 -0 4.562 20.115 9.798 44.74 z M 0 0 m 913.923 1346.842 c 86.237 9.468 149.006 85.297 149.006 169.824 c 0 13.598 -1.624 27.422 -5.037 41.23 c -10.524 42.616 -38.053 81.913 -76.261 108.768 c -6.378 4.519 -20.001 12.832 -20.993 12.832 c -0.032 -0 -0.051 -0.009 -0.056 -0.027 c -0.155 -0.467 -6.895 -31.832 -8.969 -41.578 c -0.777 -3.837 -15.138 -70.974 -31.831 -149.102 c -16.694 -78.18 -30.381 -142.258 -30.381 -142.413 c 0 -0.379 2.392 -0.553 5.903 -0.553 c 5.138 -0 12.675 0.372 18.619 1.019 z");
//        addPath(svgDocument, rootElement, "M 0 0 m 869.545 406.87 c -7.621 2.8 -37.638 31.68 -54.954 52.88 c -56.862 69.564 -87.028 156.64 -87.028 247.587 c 0 23.399 1.997 47.054 6.049 70.732 c 1.037 6.221 16.486 79.528 19.701 93.577 l 8.813 40.957 l 7.984 37.586 l -22.137 22.293 c -63.405 63.715 -115.248 124.735 -166.573 195.967 c -47.685 66.184 -71.258 141.493 -71.258 215.793 c 0 86.452 31.915 171.537 94.899 239.288 c 63.774 68.645 157.378 107.88 248.941 107.88 c 24.405 -0 48.665 -2.788 72.176 -8.548 c 4.416 -1.08 8.135 -1.927 8.565 -1.927 c 0.025 -0 0.038 0.003 0.041 0.009 c 0.156 0.207 20.323 94.251 22.863 106.849 c 5.362 26.344 8.043 50.971 8.043 73.803 c 0 25.739 -3.407 49.198 -10.22 70.269 c -21.962 67.944 -89.714 113.801 -162.345 113.801 c -9.667 -0 -19.421 -0.812 -29.164 -2.493 c -15.916 -2.748 -35.513 -8.917 -46.556 -14.724 l -2.436 -1.244 l 4.51 -0.052 c 51.947 -0.259 94.251 -43.704 94.251 -96.843 v -0.064 c 0 -60.903 -49.755 -100.629 -102.135 -100.629 c -25.878 -0 -52.397 9.696 -73.873 31.327 c -21.275 21.412 -32.045 48.407 -32.045 75.406 c 0 23.857 8.409 47.717 25.409 67.733 c 42.01 49.483 102.251 75.143 162.599 75.143 c 45.999 -0 92.061 -14.909 130.16 -45.54 c 50.448 -40.604 75.529 -97.486 75.529 -171.686 c 0 -13.339 -0.81 -27.237 -2.43 -41.702 c -2.073 -18.871 -4.665 -32.505 -17.574 -92.747 c -6.688 -31.417 -12.184 -57.287 -12.184 -57.495 c 0 -0.259 2.644 -1.555 5.859 -2.903 c 96.07 -41.129 160.99 -135.723 160.99 -233.392 c 0 -2.42 -0.04 -4.842 -0.121 -7.265 c -4.095 -124.321 -91.399 -221.008 -213.387 -236.199 c -3.777 -0.469 -12.737 -0.694 -22.131 -0.694 c -11.362 -0 -23.358 0.329 -27.586 0.954 c -2.886 0.412 -5.368 0.743 -5.942 0.743 c -0.075 -0 -0.118 -0.005 -0.124 -0.018 c -0.052 -0.051 -8.399 -39.193 -18.612 -86.941 l -18.56 -86.837 l 9.28 -8.969 l 24.833 -23.796 c 93.318 -88.601 139.77 -181.659 147.028 -294.626 c 0.414 -6.45 0.618 -13.029 0.618 -19.713 c 0 -95.296 -41.3 -211.963 -100.209 -279.061 c -15.417 -17.58 -25.878 -25.36 -34.587 -25.36 c -1.716 -0 -3.365 0.3 -4.97 0.89 z M 0 0 m 915.167 549.751 c 15.294 2.022 28.203 10.576 34.062 22.448 c 5.571 11.3 8.975 33.439 8.975 54.775 c 0 6.856 -0.351 13.628 -1.095 19.932 c -8.969 76.313 -53.658 162.114 -127.068 243.871 c -6.955 7.779 -30.547 32.042 -31.149 32.042 l -0.009 -0.003 c -0.415 -0.415 -21.567 -100.784 -22.707 -107.679 c -2.871 -17.671 -4.287 -35.201 -4.287 -52.382 c 0 -54.194 14.092 -104.918 41.095 -145.659 c 26.619 -40.244 66.071 -67.745 96.091 -67.745 c 2.079 -0 4.112 0.132 6.092 0.4 z M 0 0 m 808.992 1132.055 c 5.237 24.574 9.799 46.089 10.214 47.8 c 1.651 7.172 12.546 58.512 12.546 59.045 v 0.004 c -0.104 0.052 -1.452 0.519 -3.007 1.037 c -85.973 28.644 -140.686 106.965 -140.686 190.863 c 0 19.058 2.823 38.403 8.744 57.519 c 16.02 51.791 54.799 94.925 104.257 116.025 c 3.895 1.667 7.512 2.476 10.718 2.476 c 7.166 -0 12.28 -4.043 13.856 -11.6 c 0.261 -1.269 0.396 -2.461 0.396 -3.596 c 0 -5.634 -3.342 -9.829 -11.283 -14.705 c -37.122 -22.787 -55.619 -61.015 -55.619 -100.107 c 0 -39.798 19.171 -80.49 57.382 -106.696 c 10.145 -6.941 34.561 -18.519 38.779 -18.519 c 0.287 -0 0.481 0.054 0.57 0.166 c 0.103 0.104 6.065 27.84 13.272 61.642 l 14.101 66.1 l 1.763 8.036 l 8.761 40.956 l 21.567 100.784 c 7.414 34.786 13.479 63.508 13.479 63.819 c 0 1.451 -22.552 7.465 -36.29 9.746 c -12.705 2.103 -25.384 3.127 -37.946 3.127 c -98.609 -0 -189.955 -63.113 -229.825 -162.856 c -13.514 -33.867 -20.178 -70.07 -20.178 -106.672 c 0 -56.073 15.64 -113.081 46.255 -164.054 c 15.502 -25.766 55.836 -79.165 93.37 -123.646 c 18.197 -21.567 53.917 -61.434 55.006 -61.434 c 0.156 -0 4.562 20.115 9.798 44.74 z M 0 0 m 913.923 1346.842 c 86.237 9.468 149.006 85.297 149.006 169.824 c 0 13.598 -1.624 27.422 -5.037 41.23 c -10.524 42.616 -38.053 81.913 -76.261 108.768 c -6.378 4.519 -20.001 12.832 -20.993 12.832 c -0.032 -0 -0.051 -0.009 -0.056 -0.027 c -0.155 -0.467 -6.895 -31.832 -8.969 -41.578 c -0.777 -3.837 -15.138 -70.974 -31.831 -149.102 c -16.694 -78.18 -30.381 -142.258 -30.381 -142.413 c 0 -0.379 2.392 -0.553 5.903 -0.553 c 5.138 -0 12.675 0.372 18.619 1.019 z");
        addPath(svgDocument, rootElement, "M-206.769 126.234c-8.52901 -2.68 -13.007 -10.14 -13.007 -17.835c0 -6.451 3.146 -13.0666 9.69099 -17.1707c1.8 -1.1368 -9.047 1.8 139.074 -37.9894l103.026 -27.7106l71.6689 -19.2789c12.268 -3.31579 22.357 -6.11053 22.357 -6.25263 c0 -0.142105 -10.089 -2.93684 -22.357 -6.25263l-71.6689 -19.2789l-103.026 -27.7105c-148.121 -39.7895 -137.274 -36.8527 -139.074 -37.9895c-6.57199 -4.12849 -9.864 -10.4672 -9.864 -16.8022c0 -6.313 3.26801 -12.621 9.817 -16.735 c2.31801 -1.43999 4.964 -2.16299 8.31801 -2.16299c3.157 0 6.94099 0.640999 11.671 1.92599l161.053 43.2952c228.488 61.4133 240.486 64.527 240.486 65.2851c0 0.0888996 -0.164993 0.145399 -0.164993 0.259899c0 0.0703011 0.0619965 0.162401 0.263 0.2971 c5.63699 3.7421 8.455 9.8053 8.455 15.8685c0 6.06316 -2.81801 12.1263 -8.455 15.8684c-3.174 2.0842 2.274 0.521 -46.137 13.5473l-194.447 52.2948l-161.053 43.2947c-4.80099 1.296 -8.558 1.933 -11.647 1.933c-1.87901 0 -3.51001 -0.236 -4.979 -0.700996z");

        Element rect = svgDocument.createElementNS(SVG_NAMESPACE_URI, "rect");
        rect.setAttribute("width", "50");
        rect.setAttribute("height", "50");

        rootElement.appendChild(rect);

        addPath(svgDocument, rootElement, "M 100 100 L 300 100 L 200 300 z");

        System.out.println("Number of children: " +svgDocument.getRootElement().getChildNodes().getLength());



        DOMSource source = new DOMSource(svgDocument);
        FileWriter writer = new FileWriter(new File("test3.xml"));
        System.out.println("Test25");
        StreamResult result = new StreamResult(writer);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(source, result);

        writer.close();


    }



    private static void addPath(Document document, Node node, String path) {
        Element path1 = document.createElementNS(SVG_NAMESPACE_URI, "path");
        path1.setAttribute("d", path);
        path1.setAttribute("stroke", "blue");
        path1.setAttribute("fill", "yellow");
        node.appendChild(path1);


    }


//    public static void main(String args[]) throws ParserConfigurationException, TransformerException, IOException {
//        generateSequence();
//    }


}
