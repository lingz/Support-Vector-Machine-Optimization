package edu.nyuad.svm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import weka.core.Instances;



public class Main {
    private static final Pattern fileNamePattern = Pattern.compile(".*?([^/]+)\\.arff");

    public static void main(String[] args) throws Exception {
        String filepath;
        String outputPath;
        if (args.length > 0) {
            filepath = args[0];
        } else {
            filepath = "data/ling_test.arff";
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
        Instances dataset = DataFilters.readData(filepath);

        CrossValidator kNN;
        CrossValidator smo;

        kNN = new CrossValidator("kNN", dataset, "nofilter");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "nofilter");
        smo.crossValidate(outputPath, filename);


        for (double percentage = 10; percentage <= 90; percentage += 10) {;
            kNN = new CrossValidator("kNN", dataset, "removeRandom-" + percentage);
            kNN.crossValidate(outputPath, filename);
            smo = new CrossValidator("SMO", dataset, "removeRandom-" + percentage);
            smo.crossValidate(outputPath, filename);
        }

        kNN = new CrossValidator("kNN", dataset, "gaussianCondensing");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "gaussianCondensing");
        smo.crossValidate(outputPath, filename);

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

        kNN = new CrossValidator("kNN", dataset, "wilsonAndGaussian");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "wilsonAndGaussian");
        smo.crossValidate(outputPath, filename);

        kNN = new CrossValidator("kNN", dataset, "wilson");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "wilson");
        smo.crossValidate(outputPath, filename);

        kNN = new CrossValidator("kNN", dataset, "wilsonCondensation");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "wilsonCondensation");
        smo.crossValidate(outputPath, filename);

        kNN = new CrossValidator("kNN", dataset, "wilsonAndWilsonCondensation");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "wilsonAndWilsonCondensation");
        smo.crossValidate(outputPath, filename);


    }
}
