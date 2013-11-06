package edu.nyuad.svm;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weka.classifiers.Classifier;
import weka.core.Instances;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.runner.CaliperMain;
/**
 * Created with IntelliJ IDEA.
 * User: ling
 * Date: 27/10/13
 * Time: 12:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrossValidatorTrainBenchmark{
    private static final Pattern fileNamePattern = Pattern.compile(".*?([^/]+)\\.arff");

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
	 	
	
	private Instances train;
	private Instances test;
	private Classifier classifier;
	
	@BeforeExperiment public void setUp() throws Exception{
		String filepath = "data/ling_test.arff";
        Matcher match = fileNamePattern.matcher(filepath);
        match.matches();
        String filename = match.group(1);
        DataFilters dataset = new DataFilters(filepath);
        dataset.noFilter();
        CrossValidator cv = new CrossValidator(type, dataset);
        train = cv.data.trainCV(10, fold);
        test = cv.data.testCV(10, fold);
        System.out.println(type);
        
        if (type.equals("kNN")){
        	classifier = cv.getkNNClassifier();
        } else if (type.equals("SMO")){
        	classifier = cv.getSMOClassifier();
        } else {
        	System.out.println("AAAAAA");
        }
    }
	
	@Benchmark public int timeTrain(int reps) throws Exception {
		int dummy = 0;
		for (int i = 0; i<reps; i++){
			System.out.println(reps);
			System.out.println(fold);
			System.out.println(type);
			CrossValidator.trainValidator(classifier, train);
			System.out.println("done");
			System.out.println();
			dummy |= System.nanoTime();
		}
		return dummy;
	}
	
	public static void main(String[] args) {
        CaliperMain.main(CrossValidatorTrainBenchmark.class, args);
    }
	
}
