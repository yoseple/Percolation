import dsa.WeightedQuickUnionUF;
import stdlib.In;
import stdlib.StdOut;

// An implementation of the Percolation API using the UF data structure.
public class UFPercolation implements Percolation {
    private int N; // size of the percolation grid
    private int openSites; // number of sites
    // pointers to the top and bottom of the UF structure... source and sink
    private int top = 0; // virtual top
    private int bot; // virtual bot
    // an empty list setting each site to either true or false
    private boolean[][] open;
    private WeightedQuickUnionUF uf;
    // backwash solver...
    private WeightedQuickUnionUF solver;



    // Constructs an n x n percolation system, with all sites blocked.
    public UFPercolation(int n) {
        // Corner case
        if (n <= 0) {
            throw new IllegalArgumentException("Illegal n"); // throwing this in so that it indicates that a
            // method is passed as an illegal argument
        }
        // initializing the instance variables
        this.N = n;
        // initializing a nxn array that is blocked. it is initially blocked
        this.open = new boolean[N][N];
        this.bot = (N * N) + 1;
        // initializing check and answer
        uf = new WeightedQuickUnionUF(N * N + 2);
        solver = new WeightedQuickUnionUF(N * N + 2);
        // connecting the top row to the virtual source
        for (int i = 0; i < N; i++) {
            uf.union(encode(0, i), top);
            // connecting the bw solver to the source
            solver.union(encode(0, i), top);
        }
        // looping through the bottom column but for back wash we dont connect the solver
        // to the sink ;)
        for (int j = 0; j < N; j++) {
            uf.union(encode(N - 1, j), bot);
        }
    }

    // Opens site (i, j) if it is not already open.
    public void open(int i, int j) {
        // illegal exception for when i or j is out of bound
        if (i < 0 || j < 0 || i > N - 1 || j > N - 1) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        if (!open[i][j]) {
            open[i][j] = true; // setting the site to true meaning its open
            openSites += 1; // incrementing openSites by one while each sites opens up
        }
        // checking its N, E, W, S and making sure its in bound and doesnt move out
        // checking if its open upwards and in bound and connecting neighbors
            if (i - 1 >= 0 && isOpen(i - 1, j)) {
            uf.union(encode(i, j), encode(i - 1, j));
            solver.union(encode(i, j), encode(i - 1, j));
        }
        // checking if it opens up to the right and connecting neighbors
            if (i + 1 < N && isOpen(i + 1, j)) {
            uf.union(encode(i, j), encode(i + 1, j));
            solver.union(encode(i, j), encode(i + 1, j));
        }
            // checking if its open downwards,  connecting neighbors
            if (j - 1 >= 0 && isOpen(i, j - 1)) {  // checking if its open downwards,  connecting neighbors
            uf.union(encode(i, j), encode(i, j) - 1);
            solver.union(encode(i, j), encode(i, j)- 1);
        }
        // checking if its open to the left,  connecting neighbors
            if (j + 1 < N && isOpen(i, j + 1)) {
            uf.union(encode(i, j), encode(i, j + 1));
            solver.union(encode(i, j), encode(i, j + 1));
        }
    }

    // Returns true if site (i, j) is open, and false otherwise.
    public boolean isOpen(int i, int j) {
        // Corner cases
        if (i < 0 || j < 0 || i > N - 1 || j > N - 1) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        // return true if site is open
        return open[i][j];
    }

    // Returns true if site (i, j) is full, and false otherwise.
    public boolean isFull(int i, int j) {
        // corner case
        if (i < 0 || j < 0 || i > N - 1 || j > N - 1) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        // returning if site is full and its corresponding uf site is connected to the source.
        return solver.connected(top, encode(i, j));
    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        // returning the # of open sites ez
        return openSites;
    }

    // Returns true if this system percolates, and false otherwise.
    public boolean percolates() {
        // checking if the system percolates when connected to the sink
        return uf.connected(0, bot);
    }

    // Returns an integer ID (1...n) for site (i, j).
    private int encode(int i, int j) {
        // returning the UF site corresponding to the percolation system site.
        // a formula where no matter what it will correspond to the system site
        return N * i + 1 + j;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        UFPercolation perc = new UFPercolation(n);
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