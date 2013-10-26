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

        CrossValidator kNN = new CrossValidator("kNN");
        kNN.crossValidate(dataset.getData(), filename + "-kNN-nofilter");
        CrossValidator smo = new CrossValidator("SMO");
        smo.crossValidate(dataset.getData(), filename + "-SMO-nofilter");


        for (double percentage = 10; percentage <= 90; percentage += 10) {
            kNN.crossValidate(dataset.removePercentage(percentage), filename + "-kNN-removeRandom-" + percentage);
            smo.crossValidate(dataset.removePercentage(percentage), filename + "-SMO-removeRandom-" + percentage);
        }

    }
}
