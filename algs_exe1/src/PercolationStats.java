import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1)
            throw new IllegalArgumentException();
        if (trials < 1)
            throw new IllegalArgumentException();

        double[] precolationthres = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation pre = new Percolation(n);

            int col = 1;
            int row = 1;

            int count = 0;

            while (!pre.percolates()) {

                do {
                    col = 1 + StdRandom.uniform(n);
                    row = 1 + StdRandom.uniform(n);
                } while (pre.isOpen(row, col));

                pre.open(row, col);
                count++;
            }
            precolationthres[i] = count / (double) (n * n);
        }
        mean = StdStats.mean(precolationthres);
        stddev = StdStats.stddev(precolationthres);
        double confidenceFraction = (1.96 * stddev()) / Math.sqrt(trials);
        confidenceLo = mean - confidenceFraction;
        confidenceHi = mean + confidenceFraction;

    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        // System.out.println("all right");
        PercolationStats stats = new PercolationStats(n, t);
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi());
    }

}