package edu.nyuad.svm;

import weka.core.Instance;
import weka.core.Instances;
import weka.experiment.Stats;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: ling
 * Date: 5/11/13
 * Time: 11:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatsMap {
    Instances instances;
    HashMap<Double, Instances> classMap;
    HashMap<Double, HashMap<Integer, Stats>> statsMap;
    // index 0 of Double[] is the mean value, index 1 is the standard deviation
    // index 2 is the value of the gaussian of the mean
    HashMap<Double, HashMap<Integer, Double[]>> gaussianMap;

    public StatsMap(Instances data) {
        instances = data;
        mapClasses();
        mapStats();
    }

    // Maps instances to classes.
    private void mapClasses() {
        classMap = new HashMap<Double, Instances>();
        Instance instance;
        Double classVal;
        for (int i = 0; i < instances.numInstances() ; i++) {
            instance = instances.get(i);
            classVal = instance.classValue();
            // if the hashmap key doesn't exist, create it with this instance as the value.
            // Otherwise, just add the newest instance to the value.
            if (!classMap.containsKey(classVal)) {
                Instances classCollection = new Instances(instances, i, 1);
                classMap.put(classVal, classCollection);
            } else {
                classMap.get(classVal).add(instance);
            }
        }
    }

    // creates statsMap and gaussian Map
    private void mapStats() {
        statsMap = new HashMap<Double, HashMap<Integer, Stats>>();
        gaussianMap = new HashMap<Double, HashMap<Integer, Double[]>>();
        Iterator<Double> itr = classMap.keySet().iterator();
        Double classKey;
        Instances classInstances;
        int numAttributes = classMap.get(classMap.keySet().toArray()[0]).numAttributes();
        HashMap<Integer, Stats> attributeMap;
        HashMap<Integer, Double[]> gaussianAttributeMap;
        // for each class map onto attributes which map onto the stats of that class's attributes
        while(itr.hasNext()) {
            classKey = itr.next();
            classInstances = classMap.get(classKey);
            attributeMap = new HashMap<Integer, Stats>();
            gaussianAttributeMap = new HashMap<Integer, Double[]>();
            for (int i=0;i < numAttributes; i++) {
                // avoid the class attribute
                if (i == classInstances.classIndex()) {
                    continue;
                }
                // add the attribute class to the stats map
                Stats stats = classInstances.attributeStats(i).numericStats;
                attributeMap.put(i, stats);
                gaussianAttributeMap.put(i, new Double[]{
                    stats.mean,
                    stats.stdDev,
                    Gaussian.phi(stats.mean, stats.mean, stats.stdDev)
                });
            }
            statsMap.put(classKey, attributeMap);
            gaussianMap.put(classKey, gaussianAttributeMap);
        }
    }


    public Stats getClassAttributeStats(Double classVal, int attributeIndex) {
        return statsMap.get(classVal).get(attributeIndex);
    }

    public Double[] getClassAttributeGaussian(Double classVal, int attributeIndex) {
        return gaussianMap.get(classVal).get(attributeIndex);
    }

}
