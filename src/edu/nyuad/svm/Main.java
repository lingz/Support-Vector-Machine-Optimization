package edu.nyuad.svm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Main {
    private static final Pattern fileNamePattern = Pattern.compile(".*?([^/]+)\\.arff");

    public static void main(String[] args) throws Exception {
        String filepath;
        if (args.length > 0) {
            filepath = args[0];
        } else {
            filepath = "data/ling.arff";
        }
        System.out.println("processing " + filepath);
        Matcher match = fileNamePattern.matcher(filepath);
        match.matches();
        String filename = match.group(1);
        DataFilters dataset = new DataFilters(filepath);

        dataset.noFilter();
        CrossValidator kNN = new CrossValidator("kNN", dataset);
        kNN.crossValidate(filename + "-kNN-nofilter");
        CrossValidator smo = new CrossValidator("SMO", dataset);
        smo.crossValidate(filename + "-SMO-nofilter");


        for (double percentage = 10; percentage <= 90; percentage += 10) {
            dataset.percentageFilter(percentage);
            kNN = new CrossValidator("kNN", dataset);
            kNN.crossValidate(filename + "-kNN-removeRandom-" + percentage);
            smo = new CrossValidator("SMO", dataset);
            smo.crossValidate(filename + "-SMO-removeRandom-" + percentage);
        }

        dataset.gaussianFilter();
        kNN = new CrossValidator("kNN", dataset);
        kNN.crossValidate(filename + "-kNN-gaussian");
        smo = new CrossValidator("SMO", dataset);
        smo.crossValidate(filename + "-SMO-gaussian");
//
//        dataset.wilsonFilter();
//        kNN = new CrossValidator("kNN", dataset);
//        kNN.crossValidate(filename + "-kNN-wilson");
//        smo = new CrossValidator("SMO", dataset);
//        smo.crossValidate(filename + "-SMO-wilson");


    }
}
