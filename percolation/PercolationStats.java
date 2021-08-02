/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] arrX;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Grid number and trials should be greater than 0");

        arrX = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation per = new Percolation(n);
            while (!per.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (!per.isOpen(row, col)) per.open(row, col);
            }

            arrX[i] = per.numberOfOpenSites() / (double) (n * n);
        }
    }

    public double mean() {
        return StdStats.mean(arrX);
    }

    public double stddev() {
        return StdStats.stddev(arrX);
    }

    public double confidenceLo() {
        return mean() - stddev() * CONFIDENCE_95 / Math.sqrt(arrX.length);
    }

    public double confidenceHi() {
        return mean() + stddev() * CONFIDENCE_95 / Math.sqrt(arrX.length);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        System.out
                .printf("mean() = %f\nstddev() = %f\n95%% confidence interval = [%f, %f]",
                        stats.mean(),
                        stats.stddev(),
                        stats.confidenceLo(), stats.confidenceHi());
    }
}
