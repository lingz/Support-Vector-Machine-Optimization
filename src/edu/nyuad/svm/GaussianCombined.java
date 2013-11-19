package edu.nyuad.svm;

import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.SimpleBatchFilter;

import java.util.Random;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: ling
 * Date: 28/10/13
 * Time: 10:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class GaussianCombined extends SimpleBatchFilter{
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
        statsMap.rankMap();
        int numInstances = instances.numInstances();
        int numAttributes = instances.numAttributes();
        int numClasses = instances.numClasses();
        int classIndex = instances.classIndex();
        int removeCount = 0;
        Random random = new Random();
        Stack<Integer> toRemove = new Stack<Integer>();
        // iterate over the instances
        Instance instance;
        Double classVal;
        Double partialRemoveProbability;
        Double removeProbability;
        Double[] gaussianVals;
        Double[] gaussianValsOther;
        int rank;
        double value;
        Double[] otherClassValuePair;
        for (int i=0; i < numInstances; i++) {
            instance = instances.get(i);
            classVal = instance.classValue();
            removeProbability = 0.0;
            // iterate over each instances attributes
            for (int j=0; j < numAttributes; j++) {
                // skip the actual class
                if (j == classIndex) {
                    continue;
                }
                gaussianVals = statsMap.getClassAttributeGaussian(classVal, j);
                value = instance.value(j);
                // find its rank
                rank = statsMap.getFeatureClassRank(j, classVal);
                // if its to  the left of the mean
                // if it's the leftmost, skip
                // otherwise find the gaussian of the rank to the left
                if (value < gaussianVals[0] && rank-- == 0) continue;
                else if (++rank == numClasses) continue;
                otherClassValuePair = statsMap.getFeatureClassRankList(j)[rank];
                gaussianValsOther = statsMap.getClassAttributeGaussian(otherClassValuePair[0], j);

                // add up the remove probabilities
                partialRemoveProbability =
                        (Gaussian.phi(instance.value(j), gaussianVals[0], gaussianVals[1]) /
                        Gaussian.phi(otherClassValuePair[1], gaussianValsOther[0], gaussianValsOther[1]));
                // if this instance does not have a bigger pdf than it's neighbor, use condensing
                if (partialRemoveProbability > 1)
                    removeProbability +=
                        Gaussian.phi(instance.value(j), gaussianVals[0], gaussianVals[1]) /
                        gaussianVals[2];
                // otherwise use smoothing
                else removeProbability += 1 - partialRemoveProbability;
            }
            // take the average of the removeProbabilities
            removeProbability /= numAttributes;
//            System.out.println(removeProbability);
            // probabilitistically add the instance to the remove stack
            if (removeProbability > random.nextDouble()) {
                toRemove.push(i);
                removeCount += 1;
            }
        }
        // delete the instances in reverse order
        while (!toRemove.empty()) {
            instances.delete(toRemove.pop());
        }

        return instances;
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
        String filepath = "data/ling_test.arff";

        DataFilters dataset = null;
        try {
            dataset = new DataFilters(filepath);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Instances data = dataset.getData();
        StatsMap statsMap = new StatsMap(data);
        statsMap.rankMap();
        GaussianCombined test = new GaussianCombined();
        try {
            System.out.println(data.numInstances());
            test.process(data);
            System.out.println(data.numInstances());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
