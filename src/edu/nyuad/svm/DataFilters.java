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
    private Instances data;

    public DataFilters(String filename) throws Exception{
        data = readData(filename);
    }

    public Instances getData() {
        return data;
    }

    public Instances removePercentage(double percentage) throws Exception{
        System.out.println(data.numInstances());
        RemovePercentage remove = new RemovePercentage();
        remove.setPercentage(percentage);
        remove.setInputFormat(data);
        Instances newData = Filter.useFilter(data, remove);
        System.out.println(newData.numInstances());
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
