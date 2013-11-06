package edu.nyuad.svm;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weka.classifiers.Classifier;
import weka.core.Instances;

import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.runner.CaliperMain;
import com.google.caliper.runner.CaliperRun;
/**
 * Created with IntelliJ IDEA.
 * User: ling
 * Date: 27/10/13
 * Time: 12:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrossValidatorFilterBenchmark extends Benchmark{
    private static final Pattern fileNamePattern = Pattern.compile(".*?([^/]+)\\.arff");
    
    @Param String path;

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
	
	@Param Filter filter;
	public enum Filter{
		NONE{

			@Override
			public void setFilter(DataFilters dataset) {
				dataset.noFilter();
				
			}
			
		},
		PERCENTAGE_10{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.percentageFilter(10);
			}
			
		},
		PERCENTAGE_20{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.percentageFilter(20);
			}
			
		},
		PERCENTAGE_30{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.percentageFilter(30);
			}
			
		},
		PERCENTAGE_40{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.percentageFilter(40);
			}
			
		},
		PERCENTAGE_50{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.percentageFilter(50);
			}
			
		},
		PERCENTAGE_60{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.percentageFilter(60);
			}
			
		},
		PERCENTAGE_70{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.percentageFilter(70);
			}
			
		},
		PERCENTAGE_80{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.percentageFilter(80);
			}
			
		},
		PERCENTAGE_90{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.percentageFilter(90);
			}
			
		},
		GAUSSIAN{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.gaussianFilter();
				
			}
			
		},
		WILSON{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.wilsonFilter();
			}
			
		},
		WILSON_AND_GAUSSIAN{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.wilsonAndGaussianFilter();
			}
			
		},
		WILSON_CONDENSING{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.wilsonCondensationFilter();
			}
			
		},
		WILSON_AND_WILSON_CONDENSING{

			@Override
			public void setFilter(DataFilters dataset) throws Exception {
				dataset.wilsonAndWilsonCondensationFilter();
			}
			
		};
		abstract public void setFilter(DataFilters dataset) throws Exception;
	}
	 		
	DataFilters dataset;
	
	@Override protected void setUp() throws Exception{
		String filepath = path;
        Matcher match = fileNamePattern.matcher(filepath);
        match.matches();
        dataset = new DataFilters(filepath);
    }
	
	public int filterTest(int reps) throws Exception {
		int dummy = 0;
		for (int i = 0; i<reps; i++){
			filter.setFilter(dataset);
			dummy |= System.nanoTime();
		}
		return dummy;
	}
	
	public static void main(String[] args) {
        CaliperMain.main(CrossValidatorFilterBenchmark.class, args);
    }
	
}
