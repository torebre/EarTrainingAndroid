package com.kjipo.eartraining.data;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class DummyData {
    private static final int numberOfPoints = 1000;


    private static void createDummyData(File file) {
        List<DataGeneratorInterface> dataGenerators = new ArrayList<DataGeneratorInterface>();
        dataGenerators.add(new DataGenerator());


        // TODO



    }


    private interface DataGeneratorInterface {

        public String getParameterName();

        public double getNextValue();


    }


    private static class DataGenerator implements DataGeneratorInterface {
        private double currentValue = -0.01;
        private double increment = 0.01;


        public DataGenerator() {

        }

        @Override
        public String getParameterName() {
            return "Sine wave parameter";
        }

        @Override
        public double getNextValue() {
            currentValue += increment;
            return Math.sin(currentValue);
        }
    }


    public static void main(String args[]) {
        createDummyData(new File("/home/student/testParameter.json"));


    }


}
