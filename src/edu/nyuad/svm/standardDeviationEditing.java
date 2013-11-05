package edu.nyuad.svm;

import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.SimpleBatchFilter;
import weka.experiment.Stats;

import java.util.HashMap;
import java.util.Iterator;


/**
 * Created with IntelliJ IDEA.
 * User: ling
 * Date: 28/10/13
 * Time: 10:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class standardDeviationEditing extends SimpleBatchFilter{
    @Override
    public String globalInfo() {
        return "Probabilistically removes points proportional to their standard deviations away from the mean of their class";
    }

    @Override
    protected Instances determineOutputFormat(Instances instances) throws Exception {
        return instances;  //To change body of implemented methods use File | Settings | File Templates.
    }



    @Override
    protected Instances process(Instances instances) throws Exception {
        // Generate the stats map
        StatsMap statsMap = new StatsMap(instances);
        int numInstances = instances.numInstances();
        int numAttributes = instances.numAttributes();
        int classIndex = instances.classIndex();
        Iterator<Double> classItr = statsMap.classMap.keySet().iterator();
        Double classVal;
        // iterate over the instances
        for (int i=0; i < numInstances; i++) {

        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Capabilities getCapabilities() {
        return super.getCapabilities();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String getRevision() {
        return super.getRevision();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public static void main(String[] args) {
        String filepath = "data/ling.arff";

        DataFilters dataset = null;
        try {
            dataset = new DataFilters(filepath);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Instances data = dataset.getData();
        StatsMap statsMap = new StatsMap(data);
        System.out.println(statsMap.statsMap);
    }
}
