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
public class GaussianFilter extends SimpleBatchFilter{
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
        int removeCount = 0;
        Random random = new Random();
        Stack<Integer> toRemove = new Stack<Integer>();
        // iterate over the instances
        Instance instance;
        Double classVal;
        Double[] gaussianVals;
        for (int i=0; i < numInstances; i++) {
            instance = instances.get(i);
            classVal = instance.classValue();
            Double removeProbability = 0.0;
            // iterate over each instances attributes
            for (int j=0; j < numAttributes; j++) {
                gaussianVals = statsMap.getClassAttributeGaussian(classVal, j);
                // skip the actual class
                if (j == classIndex) {
                    continue;
                }
                // add up the remove probabilities
                removeProbability += Gaussian.phi(instance.value(j), gaussianVals[0], gaussianVals[1]) /
                        gaussianVals[2];

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
        String filepath = "data/ling.arff";

        DataFilters dataset = null;
        try {
            dataset = new DataFilters(filepath);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Instances data = dataset.getData();
        StatsMap statsMap = new StatsMap(data);
        GaussianFilter test = new GaussianFilter();
        try {
            test.process(data);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
