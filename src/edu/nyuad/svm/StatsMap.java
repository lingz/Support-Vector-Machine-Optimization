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
    HashMap<Double, Instances> classMap;
    HashMap<Double, HashMap<Integer, Stats>> statsMap;

    public StatsMap(Instances instances) {
        classMap = mapClasses(instances);
        statsMap = mapStats(classMap);
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
            }
        }
        return map;
    }

    private static HashMap<Double, HashMap<Integer, Stats>> mapStats(HashMap<Double, Instances> classMap) {
        HashMap<Double, HashMap<Integer, Stats>> statsMap = new HashMap<Double, HashMap<Integer, Stats>>();
        Iterator<Double> itr = classMap.keySet().iterator();
        Double classKey;
        Instances classInstances;
        int numAttributes = classMap.get(classMap.keySet().toArray()[0]).numAttributes();
        HashMap<Integer, Stats> attributeMap;
        // for each class map onto attributes which map onto the stats of that class's attributes
        while(itr.hasNext()) {
            classKey = itr.next();
            classInstances = classMap.get(classKey);
            attributeMap = new HashMap<Integer, Stats>();
            for (int i=0;i < numAttributes; i++) {
                // avoid the last class (the attribute class)
                if (i == classInstances.classIndex()) {
                    continue;
                }
                // add the attribute class to the stats map
                attributeMap.put(i, classInstances.attributeStats(i).numericStats);
            }
            statsMap.put(classKey, attributeMap);
        }
        return statsMap;


    }

    public Stats getClassAttributeStats(Double classVal, int attributeIndex) {
        return statsMap.get(classVal).get(attributeIndex);
    }
}
