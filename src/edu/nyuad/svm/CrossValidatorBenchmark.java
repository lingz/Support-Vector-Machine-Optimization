package edu.nyuad.svm;


import weka.classifiers.Classifier;
import weka.core.Instances;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.options.CaliperOptions;
import com.google.caliper.options.OptionsModule;
import com.google.caliper.runner.CaliperMain;
import com.google.caliper.util.Util;

import static com.google.common.collect.ObjectArrays.concat;
/**
 * Created with IntelliJ IDEA.
 * User: ling
 * Date: 27/10/13
 * Time: 12:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrossValidatorBenchmark{
	
	Classifier classifier;
	@Param({
		"kNN",
		"SMO",
	}) String type;
	
	@Param({
		"0",
		"1",
		"2",
		"3",
		"4",
		"5",
		"6",
		"7",
		"8",
		"9",
	}) int fold;
	
	Instances train;
	Instances test;
	
	@BeforeExperiment public void setUp() throws Exception{
		String filepath = "data/ling.arff";
        DataFilters dataset = new DataFilters(filepath);
        CrossValidator kNN = new CrossValidator("kNN", dataset.getData());
        CrossValidator smo = new CrossValidator("SMO", dataset.getData());
    
        if (type == "kNN") {
        	train = kNN.data.trainCV(10, fold);
            test = kNN.data.testCV(10, fold);
            classifier = kNN.getkNNClassifier();
        } else if (type == "SMO") {
        	train = smo.data.trainCV(10, fold);
            test = smo.data.testCV(10, fold);
            classifier = smo.getSMOClassifier();
        } 
    }
	
	@Benchmark public int timeTrain(int reps) throws Exception {
		int dummy = 0;
		for (int i = 0; i<reps; i++){
			CrossValidator.trainValidator(classifier, train);
			dummy |= i;
		}
		return dummy;
	}
	@Benchmark public int timeEval(int reps) throws Exception {
		int dummy = 0;
		for (int i = 0; i<reps; i++){
			CrossValidator.evaluateValidator(test, classifier);
			dummy |= i;
		}
		return dummy;
	}
	
	public static void main(String[] args) {
        CaliperMain.main(CrossValidatorBenchmark.class, args);
    }
	
}
