package edu.nyuad.svm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Main {
    private static final Pattern fileNamePattern = Pattern.compile(".*?([^/]+)\\.arff");

    public static void main(String[] args) throws Exception {
        String filepath;
        String outputPath;
        if (args.length > 0) {
            filepath = args[0];
        } else {
            filepath = "data/oleg_test.arff";
        }
        if (args.length > 1) {
            outputPath = args[1];
        } else {
            outputPath = "results";
        }

        System.out.println("processing " + filepath);
        Matcher match = fileNamePattern.matcher(filepath);
        match.matches();
        String filename = match.group(1);
        DataFilters dataset = new DataFilters(filepath);

        CrossValidator kNN;
        CrossValidator smo;

        dataset.noFilter();
        kNN = new CrossValidator("kNN", dataset);
        kNN.crossValidate(outputPath, filename + "-kNN-nofilter");
        smo = new CrossValidator("SMO", dataset);
        smo.crossValidate(outputPath, filename + "-SMO-nofilter");


        for (double percentage = 10; percentage <= 90; percentage += 10) {
            dataset.percentageFilter(percentage);
            kNN = new CrossValidator("kNN", dataset);
            kNN.crossValidate(outputPath, filename + "-kNN-removeRandom-" + percentage);
            smo = new CrossValidator("SMO", dataset);
            smo.crossValidate(outputPath, filename + "-SMO-removeRandom-" + percentage);
        }

        dataset.gaussianFilter();
        kNN = new CrossValidator("kNN", dataset);
        kNN.crossValidate(outputPath, filename + "-kNN-gaussianCondensing");
        smo = new CrossValidator("SMO", dataset);
        smo.crossValidate(outputPath, filename + "-SMO-gaussianCondensing");

//        dataset.gaussianSmoothingFilter();
//        kNN = new CrossValidator("kNN", dataset);
//        kNN.crossValidate(outputPath, filename + "-kNN-gaussianSmoothing");
//        smo = new CrossValidator("SMO", dataset);
//        smo.crossValidate(outputPath, filename + "-SMO-gaussianSmoothing");
//
//        dataset.gaussianCombinedFilter();
//        kNN = new CrossValidator("kNN", dataset);
//        kNN.crossValidate(outputPath, filename + "-kNN-gaussianCombined");
//        smo = new CrossValidator("SMO", dataset);
//        smo.crossValidate(outputPath, filename + "-SMO-gaussianCombined");
//
//        dataset.gaussianAndWilsonFilter();
//        kNN = new CrossValidator("kNN", dataset);
//        kNN.crossValidate(outputPath, filename + "-kNN-gaussianAndWilson");
//        smo = new CrossValidator("SMO", dataset);
//        smo.crossValidate(outputPath, filename + "-SMO-gaussianAndWilson");
//
//        dataset.percentageAndWilsonFilter();
//        kNN = new CrossValidator("kNN", dataset);
//        kNN.crossValidate(outputPath, filename + "-kNN-percentageAndWilson");
//        smo = new CrossValidator("SMO", dataset);
//        smo.crossValidate(outputPath, filename + "-SMO-percentageAndWilson");
//
//        dataset.percentageAndGaussianSmoothingFilter();
//        kNN = new CrossValidator("kNN", dataset);
//        kNN.crossValidate(outputPath, filename + "-kNN-percentageAndGaussianSmoothing");
//        smo = new CrossValidator("SMO", dataset);
//        smo.crossValidate(outputPath, filename + "-SMO-percentageAndGaussianSmoothing");

        dataset.wilsonAndGaussianFilter();
        kNN = new CrossValidator("kNN", dataset);
        kNN.crossValidate(outputPath, filename + "-kNN-wilsonAndGaussian");
        smo = new CrossValidator("SMO", dataset);
        smo.crossValidate(outputPath, filename + "-SMO-wilsonAndGaussian");

        dataset.wilsonFilter();
        kNN = new CrossValidator("kNN", dataset);
        kNN.crossValidate(outputPath, filename + "-kNN-wilson");
        smo = new CrossValidator("SMO", dataset);
        smo.crossValidate(outputPath, filename + "-SMO-wilson");

        dataset.wilsonCondensationFilter();
        kNN = new CrossValidator("kNN", dataset);
        kNN.crossValidate(outputPath, filename + "-kNN-wilsonCondensation");
        smo = new CrossValidator("SMO", dataset);
        smo.crossValidate(outputPath, filename + "-SMO-wilsonCondensation");

        dataset.wilsonAndWilsonCondensationFilter();
        kNN = new CrossValidator("kNN", dataset);
        kNN.crossValidate(outputPath, filename + "-kNN-wilsonAndWilsonCondensation");
        smo = new CrossValidator("SMO", dataset);
        smo.crossValidate(outputPath, filename + "-SMO-wilsonAndWilsonCondensation");


    }
}
