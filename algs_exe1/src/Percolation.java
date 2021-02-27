import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] id;
    private final int sz;
    private final WeightedQuickUnionUF qu;
    private final WeightedQuickUnionUF quickUnionStructureForIsFull;
    private int count = 0;

    private static final int firstsite = 0;
    private final int lastsite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1)
            throw new IllegalArgumentException();

        sz = n;
        id = new boolean[n][n];
        qu = new WeightedQuickUnionUF(n * n + 2);
        quickUnionStructureForIsFull = new WeightedQuickUnionUF(n * n + 1);
        lastsite = n * n + 1;

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkInput(row, col);

        if (!isOpen(row, col)) {
            int ufid = transsiteuf(row, col);

            if (row == 1) {
                qu.union(firstsite, ufid);
                quickUnionStructureForIsFull.union(firstsite, ufid);
            }
            if (row == sz) {
                qu.union(lastsite, ufid);
            }

            connectifopen(row, col, ufid);

            id[row - 1][col - 1] = true;
            count++;
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkInput(row, col);

        return id[row - 1][col - 1];

    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkInput(row, col);
        int quid = transsiteuf(row, col);
        if (isOpen(row, col)) {
            return quickUnionStructureForIsFull.connected(quid, firstsite);
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {

        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return qu.connected(firstsite, lastsite);
    }

    private void checkInput(int row, int col) {
        if (row < 1 || row > sz || col > sz || col < 1)
            throw new IllegalArgumentException();
    }

    private int transsiteuf(int i, int j) {
        return (i - 1) * sz + j;
    }

    private void connectifopen(int i, int j, int ufid) {
        if (i > 1 && isOpen(i - 1, j)) {
            qu.union(ufid, transsiteuf(i - 1, j));
            quickUnionStructureForIsFull.union(ufid, transsiteuf(i - 1, j));
        }

        if (i < sz && isOpen(i + 1, j)) {
            qu.union(ufid, transsiteuf(i + 1, j));
            quickUnionStructureForIsFull.union(ufid, transsiteuf(i + 1, j));
        }

        if (j > 1 && isOpen(i, j - 1)) {
            qu.union(ufid, transsiteuf(i, j - 1));
            quickUnionStructureForIsFull.union(ufid, transsiteuf(i, j - 1));
        }

        if (j < sz && isOpen(i, j + 1)) {
            qu.union(ufid, transsiteuf(i, j + 1));
            quickUnionStructureForIsFull.union(ufid, transsiteuf(i, j + 1));
        }

    }
}