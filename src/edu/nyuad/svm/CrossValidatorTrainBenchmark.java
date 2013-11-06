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
	 
	@Param Algorithm algorithm;
	
	public enum Algorithm{
		KNN{
			@Override
			Classifier createClassifier(CrossValidator cv) {
				return cv.getkNNClassifier();
			}
		},
		SMO{
			@Override
			Classifier createClassifier(CrossValidator cv) {
				return cv.getSMOClassifier();
			}
		};
		CrossValidator createCV(String type, DataFilters dataset){
			return new CrossValidator(type, dataset);
		}
		Instances createTrain(CrossValidator cv, int fold){
			return cv.data.trainCV(10, fold);
		}
		Instances createTest(CrossValidator cv, int fold){
			return cv.data.testCV(10, fold);
		}
		abstract Classifier createClassifier(CrossValidator cv);
	}
	
	Instances train;
	Instances test;
	Classifier classifier;
	
	@BeforeExperiment public void setUp() throws Exception{
		String filepath = "data/ling.arff";
        Matcher match = fileNamePattern.matcher(filepath);
        match.matches();
        String filename = match.group(1);
        DataFilters dataset = new DataFilters(filepath);
        dataset.noFilter();
        CrossValidator cv = algorithm.createCV(type, dataset);
        train = algorithm.createTrain(cv, fold);
        test = algorithm.createTest(cv, fold);
        classifier = algorithm.createClassifier(cv);
    }
	
	@Benchmark public int timeTrain(int reps) throws Exception {
		int dummy = 0;
		for (int i = 0; i<reps; i++){
			CrossValidator.trainValidator(classifier, train);
			dummy |= i;
		}
		return dummy;
	}
	
	public static void main(String[] args) {
        CaliperMain.main(CrossValidatorTrainBenchmark.class, args);
    }
	
}
