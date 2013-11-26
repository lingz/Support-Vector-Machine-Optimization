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
            filepath = "data/seung_test.arff";
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
//
//
//        for (double percentage = 10; percentage <= 90; percentage += 10) {;
//            kNN = new CrossValidator("kNN", dataset, "removeRandom-" + percentage);
//            kNN.crossValidate(outputPath, filename);
//            smo = new CrossValidator("SMO", dataset, "removeRandom-" + percentage);
//            smo.crossValidate(outputPath, filename);
//        }
//
//        kNN = new CrossValidator("kNN", dataset, "gaussianCondensing");
//        kNN.crossValidate(outputPath, filename);
//        smo = new CrossValidator("SMO", dataset, "gaussianCondensing");
//        smo.crossValidate(outputPath, filename);

        kNN = new CrossValidator("kNN", dataset, "gaussianSmoothing");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "gaussianSmoothing");
        smo.crossValidate(outputPath, filename);

        kNN = new CrossValidator("kNN", dataset, "gaussianCombined");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "gaussianCombined");
        smo.crossValidate(outputPath, filename);

        kNN = new CrossValidator("kNN", dataset, "gaussianAndGaussianSmoothing");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "gaussianAndGaussianSmoothing");
        smo.crossValidate(outputPath, filename);

        kNN = new CrossValidator("kNN", dataset, "gaussianSmoothingAndGaussian");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "gaussianSmoothingAndGaussian");
        smo.crossValidate(outputPath, filename);

        kNN = new CrossValidator("kNN", dataset, "gaussianAndWilson");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "gaussianAndWilson");
        smo.crossValidate(outputPath, filename);

        kNN = new CrossValidator("kNN", dataset, "percentageAndWilson");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "percentageAndWilson");
        smo.crossValidate(outputPath, filename);

        kNN = new CrossValidator("kNN", dataset, "percentageAndGaussianSmoothing");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "percentageAndGaussianSmoothing");
        smo.crossValidate(outputPath, filename);
//
//        kNN = new CrossValidator("kNN", dataset, "wilsonAndGaussian");
//        kNN.crossValidate(outputPath, filename);
//        smo = new CrossValidator("SMO", dataset, "wilsonAndGaussian");
//        smo.crossValidate(outputPath, filename);
//
        kNN = new CrossValidator("kNN", dataset, "wilson");
        kNN.crossValidate(outputPath, filename);
        smo = new CrossValidator("SMO", dataset, "wilson");
        smo.crossValidate(outputPath, filename);
//
//        kNN = new CrossValidator("kNN", dataset, "wilsonCondensation");
//        kNN.crossValidate(outputPath, filename);
//        smo = new CrossValidator("SMO", dataset, "wilsonCondensation");
//        smo.crossValidate(outputPath, filename);
//
//        kNN = new CrossValidator("kNN", dataset, "wilsonAndWilsonCondensation");
//        kNN.crossValidate(outputPath, filename);
//        smo = new CrossValidator("SMO", dataset, "wilsonAndWilsonCondensation");
//        smo.crossValidate(outputPath, filename);


    }
}
