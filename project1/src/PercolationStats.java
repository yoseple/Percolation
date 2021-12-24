import stdlib.StdOut;
import stdlib.StdRandom;
import stdlib.StdStats;

public class PercolationStats {
    // number of experiments
    private int m;
    // threshold for m experiments
    private double [] x;
    // int n which represents the system
    private int n;



    // Performs m independent experiments on an n x n percolation system.
    public PercolationStats(int n, int m) {
        if (n <= 0 || m <= 0) {
            throw new IllegalArgumentException("Illegal n or m");
        }
        // Initialize instance variables.
        this.m = m;
        this.n = n;
        this.x = new double[m];
        // Perform the following experiment m times
        for (int i = 0; i < m; i++) {
            // Create an n ×n percolation system (use the UFPercolation implementation
            UFPercolation perc = new UFPercolation(n);
            // Until the system percolates, choose a site (i,j) at random and open it
            // if it is not already open
            while (!perc.percolates()) {
                // basically getting a random # from size of grid
                int row = StdRandom.uniform(n);
                int column = StdRandom.uniform(n);
                // opens the # of rows and columns
                perc.open(row, column);
            }

            // Calculate percolation threshold as the fraction of sites opened,
            // and store the value in x[].
            double d = (double) (perc.numberOfOpenSites() / (double) (n * n));
            x[i] = d;

        }

    }



    // Returns sample mean of percolation threshold.
    public double mean() {
        return StdStats.mean(x); // the average of x
    }

    // Returns sample standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(x);
    }

    // Returns low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        // lowest interval
        return StdStats.mean(x) - (1.96 * StdStats.stddev(x) / Math.sqrt(x.length));
    }

    // Returns high endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        // highest interval
        return StdStats.mean(x) + (1.96 * StdStats.stddev(x) / Math.sqrt(x.length));
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, m);
        StdOut.printf("Percolation threshold for a %d x %d system:\n", n, n);
        StdOut.printf("  Mean                = %.3f\n", stats.mean());
        StdOut.printf("  Standard deviation  = %.3f\n", stats.stddev());
        StdOut.printf("  Confidence interval = [%.3f, %.3f]\n", stats.confidenceLow(),
                stats.confidenceHigh());
    }
}