package edu.nyuad.svm;

import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

/**
 * Cross validation implementation
 * For the NYUAD SVM Research Group
 * Fall 2013 - Artificial Intelligence Class
 * Written by Lingliang Zhang
 */
public class DataFilters {
    public Instances oldData;
    public Instances newData;
    long filterTime;

    public DataFilters(String filename) throws Exception{
        oldData = readData(filename);
    }

    public Instances getData() {
        return oldData;
    }

    public void noFilter() {
        filterTime = 0;
        newData = oldData;
    }

    public void percentageFilter(double percentage) throws Exception {
        long startFilter = System.nanoTime();
        newData = removePercentage(percentage);
        filterTime = System.nanoTime() - startFilter;
    }

    public Instances removePercentage(double percentage) throws Exception{
        RemovePercentage remove = new RemovePercentage();
        remove.setPercentage(percentage);
        remove.setInputFormat(oldData);
        Instances newData = Filter.useFilter(oldData, remove);
        return newData;
    }

    public void gaussianFilter() throws Exception {
        long startFilter = System.nanoTime();
        newData = removeGaussian();
        filterTime = System.nanoTime() - startFilter;
    }

    public Instances removeGaussian() throws Exception {
        GaussianFilter gaussianFilter = new GaussianFilter();
        gaussianFilter.setInputFormat(oldData);
        Instances newData = Filter.useFilter(oldData, gaussianFilter);
        return newData;
    }

    public void gaussianSmoothingFilter() throws Exception {
        long startFilter = System.nanoTime();
        newData = removeGaussianSmoothing();
        filterTime = System.nanoTime() - startFilter;
    }

    public Instances removeGaussianSmoothing() throws Exception {
        GaussianSmoothing gaussianSmoothingFilter = new GaussianSmoothing();
        gaussianSmoothingFilter.setInputFormat(oldData);
        Instances newData = Filter.useFilter(oldData, gaussianSmoothingFilter);
        return newData;
    }

    public void gaussianCombinedFilter() throws Exception {
        long startFilter = System.nanoTime();
        newData = removeGaussianCombined();
        filterTime = System.nanoTime() - startFilter;
    }

    public Instances removeGaussianCombined() throws Exception {
        GaussianCombined gaussianCombinedFilter = new GaussianCombined();
        gaussianCombinedFilter.setInputFormat(oldData);
        Instances newData = Filter.useFilter(oldData, gaussianCombinedFilter);
        return newData;
    }

    public void wilsonFilter() throws Exception {
        long startFilter = System.nanoTime();
        newData = removeWilson();
        filterTime = System.nanoTime() - startFilter;
    }

    public Instances removeWilson() throws Exception {
        WilsonFilter wilsonFilter = new WilsonFilter();
        wilsonFilter.setInputFormat(oldData);
        Instances newData = Filter.useFilter(oldData, wilsonFilter);
        return newData;
    }


    public void wilsonCondensationFilter() throws Exception {
        long startFilter = System.nanoTime();
        newData = removeWilsonCondensation();
        filterTime = System.nanoTime() - startFilter;
    }

    public Instances removeWilsonCondensation() throws Exception {
        WilsonCondensationFilter wilsonCondensationFilter = new WilsonCondensationFilter();
        wilsonCondensationFilter.setInputFormat(oldData);
        Instances newData = Filter.useFilter(oldData, wilsonCondensationFilter);
        return newData;
    }

    public void wilsonAndWilsonCondensationFilter() throws Exception {
        Instances tempData = oldData;
        long startFilter = System.nanoTime();
        oldData = removeWilson();
        newData = removeWilsonCondensation();
        oldData = tempData;
        filterTime = System.nanoTime() - startFilter;
    }

    public void wilsonAndGaussianFilter() throws Exception {
        Instances tempData = oldData;
        long startFilter = System.nanoTime();
        oldData = removeWilson();
        newData = removeGaussian();
        oldData = tempData;
        filterTime = System.nanoTime() - startFilter;
    }

    public void gaussianAndWilsonFilter() throws Exception {
        Instances tempData = oldData;
        long startFilter = System.nanoTime();
        oldData = removeGaussian();
        newData = removeWilson();
        oldData = tempData;
        filterTime = System.nanoTime() - startFilter;
    }

    public void percentageAndWilsonFilter() throws Exception {
        Instances tempData = oldData;
        long startFilter = System.nanoTime();
        oldData = removePercentage(70);
        newData = removeWilson();
        oldData = tempData;
        filterTime = System.nanoTime() - startFilter;
    }

    public void percentageAndGaussianSmoothingFilter() throws Exception {
        Instances tempData = oldData;
        long startFilter = System.nanoTime();
        oldData = removePercentage(70);
        newData = removeGaussianSmoothing();
        oldData = tempData;
        filterTime = System.nanoTime() - startFilter;
    }





    // gets the data for use with all training methods
    private Instances readData(String filename) throws Exception{
        Instances data = ConverterUtils.DataSource.read(filename);
        // Set the class as the last attribute  (as written by preprocessor)
        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }

}
