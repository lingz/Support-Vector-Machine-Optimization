package edu.nyuad.svm;

import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;
import weka.filters.SimpleBatchFilter;

/*
Wilson Editing filter, code by Seung Man Oh

Using the k-nn of each instance, decides to keep the point
only if the majority of the neighbours are of the same class.

The k is set as 3 by default.
*/
public class WilsonFilter extends SimpleBatchFilter{
    private int K=3;

    public String globalInfo(){
        return   " Wilson Editing with k=3 nearest neighbor rule";
    }

    public void setK(int i){
        K=i;
    }
    public Capabilities getCapabilities(){
        return super.getCapabilities();
    }

    protected Instances determineOutputFormat(Instances inputFormat){
        return inputFormat;
    }

    protected Instances process(Instances inst) throws Exception{
        Instances result = new Instances(inst, 0);
        Instances knn;
        Instance temp;
        int n;

        LinearNNSearch kNN = new LinearNNSearch();
        kNN.setInstances(inst);


        for (int i = 0; i < inst.numInstances(); i++) {
            temp=inst.instance(i);
            knn= kNN.kNearestNeighbours(temp,K);
            n=0;
            for(int j=0;j<K;j++){
                if (temp.value(temp.classAttribute()) == knn.instance(j).value(knn.instance(j).classAttribute())){
                    n++;
                }
            }

            if(n > K-n){
                result.add(temp);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        runFilter(new WilsonFilter(), args);
    }
}
