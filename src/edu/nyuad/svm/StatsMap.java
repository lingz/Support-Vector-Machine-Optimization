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
    // two way links, given a class-feature pair, produce its rank,
    // given a feature, produce the ranked class array.
    HashMap<Integer, HashMap<Double, Integer>> featureClassToRankMap;
    HashMap<Integer, Double[][]> featureToRankedClassesMap;

    public StatsMap(Instances data) {
        instances = data;
        mapClasses();
        mapStats();
    }

    // extend the stats class by ranking the contained classes
    public void rankMap() {
        mapRank();
        // debug code
//        System.out.println(featureClassToRankMap);
//        for (int i = 0; i < instances.numAttributes() - 1; i++) {
//            Double[][] list = getFeatureClassRankList(i);
//            System.out.println("-----------Feature" + i);
//            for (int j=0; j < list.length; j++) {
//                System.out.println(list[j][0] + " - " + list[j][1]);
//            }
//        }
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
        int numAttributes = instances.numAttributes();
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

    private void mapRank() {
        featureToRankedClassesMap = new HashMap<Integer, Double[][]>();
        featureClassToRankMap = new HashMap<Integer, HashMap<Double, Integer>>();


        double[][] ranks;
        Double[][] storedRanks;
        Iterator<Double> classItr;
        Double classKey;
        int numClasses = classMap.keySet().size();
        int j;
        // go through each attribute
        for (int i = 0; i < instances.numAttributes(); i++) {
            if (i == instances.classIndex()) continue;
            HashMap<Double, Integer> classToRankMap = new HashMap<Double, Integer>();
            ranks = new double[numClasses][2];
            storedRanks = new Double[numClasses][2];
            // add the class-attribute mean to the ranks
            classItr = classMap.keySet().iterator();
            j = 0;
            // load all the classes in to the ranks
            while(classItr.hasNext()) {
                classKey = classItr.next();
                ranks[j++] = new double[] {classKey, getClassAttributeStats(classKey, i).mean};
            }
            ranks = Mergesort.mergesort(ranks);
            // backwards iterate to cast to Double and record their positions in classFeatureRankMap
            Double[] classPair;
            while (j-- > 0) {
                classPair = new Double[] {(Double) ranks[j][0], (Double) ranks[j][1]};
                storedRanks[j] = classPair;
                classToRankMap.put(classPair[0], j);
            }

            featureClassToRankMap.put(i, classToRankMap);
            featureToRankedClassesMap.put(i, storedRanks);
        }


    }


    public Stats getClassAttributeStats(Double classVal, int attributeIndex) {
        return statsMap.get(classVal).get(attributeIndex);
    }

    public Double[] getClassAttributeGaussian(Double classVal, int attributeIndex) {
        return gaussianMap.get(classVal).get(attributeIndex);
    }

    public int getFeatureClassRank(int featureIndex, Double classKey) {
        return  featureClassToRankMap.get(featureIndex).get(classKey);
    }

    public Double[][] getFeatureClassRankList(int featureIndex) {
        return featureToRankedClassesMap.get(featureIndex);
    }

}
