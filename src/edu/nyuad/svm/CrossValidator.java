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
    private double[] accuracy = new double[10];
    private Classifier classifier;
    Instances data;
    private int oldDataCount;
    private int newDataCount;
    private long filterTime;
    private String type;
    String output;

    public CrossValidator(String selectedType, DataFilters testData) {
        // build the classifier
        type = selectedType;
        data = testData.newData;
        filterTime = testData.filterTime;
        oldDataCount = testData.oldData.numInstances();
        newDataCount = testData.newData.numInstances();
        data.stratify(10);
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

    public Evaluation singleValidate(int fold) throws Exception{
        Classifier classifier;
        Instances train = data.trainCV(10, fold);
        Instances test = data.testCV(10, fold);

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


        output += String.format("Cross Validation results for %s", filename) + "\n";
        output += "Instance\tAccuracy" + "\n";
        for (int i = 0; i < 10; i++) {
            // print results for each instance
            output += String.format("%s\t\t\t%s", Integer.toString(i),
                    Double.toString(accuracy[i]).substring(0,3) + (Double.toString(accuracy[i]).length() < 4 ? " " : Double.toString(accuracy[i]).substring(3,4))) + "\n";
//                    Long.toString(trainingTimes[i]), Long.toString(testingTimes[i])));
        }
        output += "== Summary ==" + "\n";
        output += String.format("Original Instances: \t%d", oldDataCount) + "\n";
        output += String.format("New Instances: \t%d", newDataCount) + "\n";
        output += String.format("Mean Accuracy: \t\t\t\t\t\t\t%s", Double.toString(getMean(accuracy))) + "\n";
        output += String.format("Variace of Accuracy: \t\t\t\t\t%s", Double.toString(getVariance(accuracy))) + "\n";
        output += String.format("Standard Deviation of Accuracy: \t%s", Double.toString(Math.sqrt(getVariance(accuracy)))) + "\n";
        output += String.format("Filter Time: \t\t\t\t\t%s", Long.toString(filterTime)) + "\n";
        output += String.format("Mean Training Time: \t\t\t\t\t%s", Long.toString(getMean(trainingTimes))) + "\n";
        output += String.format("Variace of Training Time: \t\t\t\t%s", Long.toString(getVariance(trainingTimes))) + "\n";
        output += String.format("Standard Deviation of Training Time: \t%s", Double.toString(Math.sqrt(getVariance(trainingTimes)))) + "\n";
        output += String.format("Mean Testing Time: \t\t\t\t\t\t%s", Long.toString(getMean(testingTimes))) + "\n";
        output += String.format("Variace of Testing Time: \t\t\t\t%s", Long.toString(getVariance(testingTimes))) + "\n";
        output += String.format("Standard Deviation of Testing Time: \t%s", Double.toString(Math.sqrt(getVariance(testingTimes)))) + "\n";

        // print results
        java.util.Date date= new java.util.Date();
        String time = new Timestamp(date.getTime()).toString().replace(" ", "-");
        File file = new File(outputPath + "/" + "-" + filename + "-" + time + ".txt");
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
