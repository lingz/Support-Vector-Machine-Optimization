package edu.nyuad.svm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Main {
    private static final Pattern fileNamePattern = Pattern.compile(".*?([^/]+)\\.arff");

    public static void main(String[] args) throws Exception {
        String filepath = "data/ling.arff";
        Matcher match = fileNamePattern.matcher(filepath);
        match.matches();
        String filename = match.group(1);

        DataFilters dataset = new DataFilters(filepath);

        CrossValidator kNN = new CrossValidator("kNN", dataset.getData());
        kNN.crossValidate(filename + "-kNN-nofilter");
        CrossValidator smo = new CrossValidator("SMO", dataset.getData());
        smo.crossValidate(filename + "-SMO-nofilter");


        for (double percentage = 10; percentage <= 90; percentage += 10) {
            kNN = new CrossValidator("kNN", dataset.removePercentage(percentage));
            kNN.crossValidate(filename + "-kNN-removeRandom-" + percentage);
            smo = new CrossValidator("SMO", dataset.removePercentage(percentage));
            smo.crossValidate(filename + "-SMO-removeRandom-" + percentage);
        }


    }
}
