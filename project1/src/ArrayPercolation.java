import stdlib.In;
import stdlib.StdOut;

// An implementation of the Percolation API using a 2D array.
public class ArrayPercolation implements Percolation {
    private int N; // system size
    private int openSites; // # of open sites
    private boolean[][] open; // see if it is true or false...



    // Constructs an n x n percolation system, with all sites blocked.
    public ArrayPercolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Illegal n");
        }
        this.N = n; // initializing N = n meaning setting it to 0
        this.open = new boolean[N][N]; // automatically sets it to false
    }

    // Opens site (i, j) if it is not already open...
    public void open(int i, int j) { // void means it will not return something
        // exception
        if (i < 0 || j < 0 || i > N - 1 || j > N - 1) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        if (!open[i][j]) {
            open[i][j] = true;
            openSites += 1; // openSites += 1; // opening sites and incrementing openSites
        }

    }

    // Returns true if site (i, j) is open, and false otherwise.
    public boolean isOpen(int i, int j) {
        // exception
        if (i < 0 || j < 0 || i > N - 1 || j > N - 1) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        return open[i][j]; // returning true
    }
    // Returns true if site (i, j) is full, and false otherwise.
    public boolean isFull(int i, int j) {
        // exception
        if (i < 0 || j < 0 || i > N - 1 || j > N - 1) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        // setting full under the class isFull so whenever isFull is getting called it
        // is under there and can get called repeatedly
        boolean[][] full = new boolean[N][N];
        for (int u = 0; u < N; u++) { // a loop that iterates through the first row...
            floodFill(full, 0, u); // accessing the top row and setting each site to full.
        }
        return full[i][j]; // returns the value stored in full list i j

    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Returns true if this system percolates, and false otherwise.
    public boolean percolates() {
        for (int x = 0; x < N; x++) { // looping through the last row
            if (isFull(N - 1, x)) { // if the site is full then return true
                return true;
            }
        }
        return false; // since it is true from the if statement then return the system percolates
    }

    // Recursively flood fills full[][] using depth-first exploration, starting at (i, j).
    private void floodFill(boolean[][] full, int i, int j) {
        // first check and return if your i and j is inbound. if it's not full then u fill it.
        // make sure it is in
        // bound of (i, j) 0 < i < n -1// 0 < j < n - 1
        // open[i][j] = Blocked 'false' // then it is another base case and just return
        // If its Full[i][j] == true // another base case and just return.
        if (i < 0 || j < 0 || i > N - 1 || j > N - 1 || !isOpen(i, j) || full[i][j]) {
            return;
        }

        full[i][j] = true;

        floodFill(full, i - 1, j); // looking to the upward and checking
        floodFill(full, i, j + 1); // looking right? and checking
        floodFill(full, i + 1, j); // looking to the downwards and checking
        floodFill(full, i, j - 1); // looking left? and checking
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        ArrayPercolation perc = new ArrayPercolation(n);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.printf("%d x %d system:\n", n, n);
        StdOut.printf("  Open sites = %d\n", perc.numberOfOpenSites());
        StdOut.printf("  Percolates = %b\n", perc.percolates());
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.printf("  isFull(%d, %d) = %b\n", i, j, perc.isFull(i, j));
        }
    }
}