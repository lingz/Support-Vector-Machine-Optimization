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

    public DataFilters(String filename) throws Exception{
        oldData = readData(filename);
    }

    public Instances getData() {
        return oldData;
    }

    public void noFilter() {
        newData = oldData;
    }

    public void percentageFilter(double percentage) throws Exception {
        newData = removePercentage(percentage);
    }

    public Instances removePercentage(double percentage) throws Exception{
        RemovePercentage remove = new RemovePercentage();
        remove.setPercentage(percentage);
        remove.setInputFormat(oldData);
        Instances newData = Filter.useFilter(oldData, remove);
        return newData;
    }

    public void gaussianFilter() throws Exception {
        newData = removeGaussian();
    }

    public Instances removeGaussian() throws Exception {
        GaussianFilter gaussianFilter = new GaussianFilter();
        gaussianFilter.setInputFormat(oldData);
        Instances newData = Filter.useFilter(oldData, gaussianFilter);
        return newData;
    }

    public void wilsonFilter() throws Exception {
        newData = removeWilson();
    }

    public Instances removeWilson() throws Exception {
        WilsonFilter wilsonFilter = new WilsonFilter();
        wilsonFilter.setInputFormat(oldData);
        Instances newData = Filter.useFilter(oldData, wilsonFilter);
        return newData;
    }

    // gets the data for use with all training methods
    private Instances readData(String filename) throws Exception{
        Instances data = ConverterUtils.DataSource.read(filename);
        // Set the class as the last attribute  (as written by preprocessor)
        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }

}
