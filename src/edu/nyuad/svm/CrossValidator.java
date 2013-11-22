package edu.nyuad.svm;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.classifiers.functions.SMO;
import weka.classifiers.Evaluation;

import java.lang.Math;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;

/**
 * Cross validation implementation
 * For the NYUAD SVM Research Group
 * Fall 2013 - Artificial Intelligence Class
 * Written by Lingliang Zhang
 */
public class CrossValidator {
    private long[] trainingTimes = new long[10];
    private long[] testingTimes = new long[10];
    private long[] filterTimes = new long[10];
    private double[] filterRemoved = new double[10];
    private double[] accuracy = new double[10];
    private Classifier classifier;
    Instances data;
    private String type;
    String output;
    String filterStrategy;

    public CrossValidator(String selectedType, Instances instances, String strategy) {
        // build the classifier
        type = selectedType;
        data = instances;
        data.stratify(10);
        filterStrategy = strategy;
    }

    // Instantiate the classifier
    IBk getkNNClassifier() {
        IBk classifier = new IBk(10);
        // Turn on cross validation
        classifier.setCrossValidate(true);
        // build the classifier
        return classifier;
    }

    SMO getSMOClassifier() {
        SMO classifier = new SMO();
        return classifier;
    }

    private double getMean(double[] nums) {
        double mean = 0;
        for (int i = 0; i < nums.length; i++) {
            mean += nums[i];
        }
        return mean / nums.length;
    }

    private double getVariance(double[] nums) {
        double mean = getMean(nums);
        double variance = 0;
        for (int i = 0; i < nums.length; i++) {
            variance += (mean - nums[i]) * (mean - nums[i]);
        }
        return variance / nums.length;
    }

    private long getVariance(long[] nums) {
        long mean = getMean(nums);
        long variance = 0;
        for (int i = 0; i < nums.length; i++) {
            variance += (mean - nums[i]) * (mean - nums[i]);
        }
        return variance / nums.length;
    }

    private long getMean(long[] nums) {
        long mean = 0;
        for (int i = 0; i < nums.length; i++) {
            mean += nums[i];
        }
        return mean / nums.length;
    }

    public static void trainValidator(Classifier classifier, Instances train) throws Exception{
//            long startTrain = System.nanoTime();
        classifier.buildClassifier(train);
    }

    public static Evaluation evaluateValidator(Instances data, Classifier classifier) throws Exception{
        // perform cross-validation
        Evaluation eval = new Evaluation(data);

        eval.evaluateModel(classifier, data);

        return eval;
    }

    public DataFilters filterData(Instances instances) throws  Exception{
        DataFilters dataset = new DataFilters(instances);
        if (filterStrategy.equals("nofilter")) {
            dataset.noFilter();
        } else if (filterStrategy.equals("removeRandom-10.0")) {
            dataset.percentageFilter(10.0);
        } else if (filterStrategy.equals("removeRandom-20.0")) {
            dataset.percentageFilter(20.0);
        } else if (filterStrategy.equals("removeRandom-30.0")) {
            dataset.percentageFilter(30.0);
        } else if (filterStrategy.equals("removeRandom-40.0")) {
            dataset.percentageFilter(40.0);
        } else if (filterStrategy.equals("removeRandom-50.0")) {
            dataset.percentageFilter(50.0);
        } else if (filterStrategy.equals("removeRandom-60.0")) {
            dataset.percentageFilter(60.0);
        } else if (filterStrategy.equals("removeRandom-70.0")) {
            dataset.percentageFilter(70.0);
        } else if (filterStrategy.equals("removeRandom-80.0")) {
            dataset.percentageFilter(80.0);
        } else if (filterStrategy.equals("removeRandom-90.0")) {
            dataset.percentageFilter(90.0);
        } else if (filterStrategy.equals("gaussianCondensing")) {
            dataset.gaussianFilter();
        } else if (filterStrategy.equals("wilsonAndGaussian")) {
            dataset.wilsonAndGaussianFilter();
        } else if (filterStrategy.equals("wilson")) {
            dataset.wilsonFilter();
        } else if (filterStrategy.equals("wilsonCondensation")) {
            dataset.wilsonCondensationFilter();
        } else if (filterStrategy.equals("wilsonAndWilsonCondensation")) {
            dataset.wilsonAndWilsonCondensationFilter();
        } else {
            throw new Exception("Stategy " + filterStrategy + " not found");
        }
        return dataset;
    }

    public Evaluation singleValidate(int fold) throws Exception{
        Classifier classifier;
        DataFilters trainData = filterData(data.trainCV(10, fold));
        Instances train = trainData.newData;
        Instances test = data.testCV(10, fold);
        filterRemoved[fold] = 1 - (trainData.newData.numInstances() / (double) trainData.oldData.numInstances());
        filterTimes[fold] = trainData.filterTime;

        if (type.equals("kNN")) {
            classifier = getkNNClassifier();
        } else if (type.equals("SMO")) {
            classifier = getSMOClassifier();
        } else {
            classifier = getkNNClassifier();
        }
        long startTrain = System.nanoTime();
        trainValidator(classifier, train);
        trainingTimes[fold] = System.nanoTime() - startTrain;
        long startTest = System.nanoTime();
        Evaluation eval = evaluateValidator(test, classifier);
        if (type.equals("kNN") && fold == 0) {
            output += String.format("Optimal K: %s\n", ((IBk) classifier).getKNN());
        }
        testingTimes[fold] = System.nanoTime() - startTest;

        return eval;
    }

    private void nanoToMicro(long[] results) {
        for (int i = 0; i < results.length; i++) {
            results[i] = results[i] / 1000;
        }
    }

    public void crossValidate(String filename) throws Exception {
        crossValidate("results", filename);
    }

    public void crossValidate(String outputPath, String filename) throws Exception {
        // split into 10 folds
        output = "";

        Classifier classifier;
        Evaluation results;
        for (int i = 0; i < 10; i++) {
            results = singleValidate(i);
            accuracy[i] = 1 - results.errorRate();
        }


        output += String.format("Cross Validation results for %s", filename + "-" + type + "-" + filterStrategy) + "\n";
        output += "Instance\tAccuracy" + "\n";
        for (int i = 0; i < 10; i++) {
            // print results for each instance
            output += String.format("%s\t\t\t%s", Integer.toString(i),
                    Double.toString(accuracy[i]).substring(0,3) + (Double.toString(accuracy[i]).length() < 4 ? " " : Double.toString(accuracy[i]).substring(3,4))) + "\n";
//                    Long.toString(trainingTimes[i]), Long.toString(testingTimes[i])));
        }
        nanoToMicro(filterTimes);
        nanoToMicro(trainingTimes);
        nanoToMicro(testingTimes);

        output += "== Summary ==" + "\n";
        output += String.format("Mean Accuracy: \t\t\t\t\t\t\t%s", Double.toString(getMean(accuracy))) + "\n";
        output += String.format("Variace of Accuracy: \t\t\t\t\t%s", Double.toString(getVariance(accuracy))) + "\n";
        output += String.format("Standard Deviation of Accuracy: \t%s", Double.toString(Math.sqrt(getVariance(accuracy)))) + "\n";
        output += String.format("Mean Filter Removal: \t\t\t\t\t\t\t%s", Double.toString(getMean(filterRemoved))) + "\n";
        output += String.format("Variace of Filter Removal: \t\t\t\t\t%s", Double.toString(getVariance(filterRemoved))) + "\n";
        output += String.format("Standard Deviation of Filter Removal: \t%s", Double.toString(Math.sqrt(getVariance(filterRemoved)))) + "\n";
        output += String.format("Mean Filter Time: \t\t\t\t\t\t\t%s", Long.toString(getMean(filterTimes))) + "\n";
        output += String.format("Variace of Filter Time: \t\t\t\t\t%s", Long.toString(getVariance(filterTimes))) + "\n";
        output += String.format("Standard Deviation of Filter Time: \t%s", Double.toString(Math.sqrt(getVariance(filterTimes)))) + "\n";
        output += String.format("Mean Training Time: \t\t\t\t\t%s", Long.toString(getMean(trainingTimes))) + "\n";
        output += String.format("Variace of Training Time: \t\t\t\t%s", Long.toString(getVariance(trainingTimes))) + "\n";
        output += String.format("Standard Deviation of Training Time: \t%s", Double.toString(Math.sqrt(getVariance(trainingTimes)))) + "\n";
        output += String.format("Mean Testing Time: \t\t\t\t\t\t%s", Long.toString(getMean(testingTimes))) + "\n";
        output += String.format("Variace of Testing Time: \t\t\t\t%s", Long.toString(getVariance(testingTimes))) + "\n";
        output += String.format("Standard Deviation of Testing Time: \t%s", Double.toString(Math.sqrt(getVariance(testingTimes)))) + "\n";

        // print results
        java.util.Date date= new java.util.Date();
        String time = new Timestamp(date.getTime()).toString().replace(" ", "-");
        File file = new File(outputPath + "/" + "-" + filename + "-" + type +
                "-" + filterStrategy + "-" + time + ".txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(output);
        bw.close();

        System.out.println(output);
    }
}
