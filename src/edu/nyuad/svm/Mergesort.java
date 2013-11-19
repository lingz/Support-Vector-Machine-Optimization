package edu.nyuad.svm;

/**
 * Created with IntelliJ IDEA.
 * User: ling
 * Date: 19/11/13
 * Time: 5:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Mergesort {

    private static double[][] merge(double[][] a, double[][] b) {
        double[][] c = new double[a.length + b.length][2];
        int i = 0, j = 0;
        for (int k = 0; k < c.length; k++) {
            if      (i >= a.length) c[k] = b[j++];
            else if (j >= b.length) c[k] = a[i++];
            else if (a[i][1] <= b[j][1])  c[k] = a[i++];
            else                    c[k] = b[j++];
        }
        return c;
    }

    public static double[][] mergesort(double[][] input) {
        int N = input.length;
        if (N <= 1) return input;
        double[][] a = new double[N/2][2];
        double[][] b = new double[N - N/2][2];
        for (int i = 0; i < a.length; i++) a[i] = input[i];
        for (int i = 0; i < b.length; i++) b[i] = input[i + N/2];
        return merge(mergesort(a), mergesort(b));
    }
}