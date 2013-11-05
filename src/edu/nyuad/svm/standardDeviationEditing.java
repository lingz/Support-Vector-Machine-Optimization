package edu.nyuad.svm;

import weka.classifiers.trees.j48.Stats;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.SimpleBatchFilter;

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

    // Maps instances to classes.
    private static HashMap<Double, Instances> mapClasses(Instances instances) {
        HashMap<Double, Instances> map = new HashMap<Double, Instances>();
        Instance instance;
        Double classVal;
        for (int i = 0; i < instances.numInstances() ; i++) {
            instance = instances.get(i);
            classVal = instance.classValue();
            // if the hashmap key doesn't exist, create it with this instance as the value.
            // Otherwise, just add the newest instance to the value.
            if (!map.containsKey(classVal)) {
                Instances classCollection = new Instances(instances, i, 1);
                map.put(classVal, classCollection);
            } else {
                map.get(classVal).add(instance);
                map.get(classVal).attributeStats(0).numericStats.
            }
        }
        return map;
    }

    private static HashMap<Double, HashMap<Integer, Stats>> mapToStats(HashMap<Double, Instances> classMap) {
        HashMap<Double, HashMap<Integer, Stats>> statsMap = new HashMap<Double, HashMap<Integer, Stats>>();
        Iterator<Double> itr = classMap.keySet().iterator();
        Double classKey;
        Instances classInstances;
        int numAttributes = classMap.get(classMap.keySet().toArray()[0]).numAttributes();
        HashMap<Integer, String> attributeMap;
        // for each class map onto attributes which map onto the stats of that class's attributes
        while(itr.hasNext()) {
            classKey = itr.next();
            classInstances = classMap.get(classKey);
            attributeMap = new HashMap<Integer, String>();
            for (int i=0;i < numAttributes; i++) {
                // avoid the last class (the attribute class)
                if (i == classInstances.classIndex()) {
                    pass
                }
            }
                    classInstances
//            statsMap.put(classKey, ))

        }
        return null;


    }

    @Override
    protected Instances process(Instances instances) throws Exception {
        // iterate once to map the classes
        HashMap<Double, Instances> classMap = mapClasses(instances);

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
        HashMap<Double, Instances> classMap = mapClasses(data);
        System.out.println(classMap.keySet());

    }
}
