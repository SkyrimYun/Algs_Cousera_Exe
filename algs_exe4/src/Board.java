import java.util.LinkedList;
import java.util.List;

public class Board {

    private final int sz;
    private int hamming;
    private int manhattan;
    private int[][] tile;
    private List<Board> neighbors;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null)
            throw new IllegalArgumentException();
        int row = tiles.length;
        int col = tiles[0].length;
        for (int[] ay : tiles) {
            if (col != ay.length)
                throw new IllegalArgumentException();
        }
        if (col != row)
            throw new IllegalArgumentException();

        sz = row;
        tile = tiles;

        hamming = 0;
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                if (tile[i][j] != (i * sz + j + 1) && tile[i][j] != 0)
                    hamming++;
            }
        }

        manhattan = 0;
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                if (tile[i][j] != 0)
                    manhattan += Math.abs(i - (tile[i][j] - 1) / sz) + Math.abs(j - (tile[i][j] - 1) % sz);
            }
        }

    }

    // string representation of this board
    public String toString() {
        String ss = sz + "\n";
        for (int[] ay : tile) {
            for (int n : ay) {
                ss += n + " ";
            }
            ss += "\n";
        }
        return ss;
    }

    // board dimension n
    public int dimension() {
        return sz;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (hamming() == 0)
            return true;
        else
            return false;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;
        if (dimension() != that.dimension())
            return false;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (tile[i][j] != that.tile[i][j])
                    return false;
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        if (neighbors != null)
            return neighbors;

        neighbors = new LinkedList<Board>();
        int[][] tileCopy;

        int zx = -1;
        int zy = -1;

        outerLoop: for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                if (tile[i][j] == 0) {
                    zx = i;
                    zy = j;
                    break outerLoop;
                }
            }
        }

        for (int i = 0; i < 4; i++) {

            int blockI = zx;
            int blockJ = zy;

            switch (i) {
                case 0:
                    blockI++;
                    break;
                case 1:
                    blockI--;
                    break;
                case 2:
                    blockJ++;
                    break;
                case 3:
                    blockJ--;
                    break;
            }

            if (blockI < 0 || blockI == dimension() || blockJ < 0 || blockJ == dimension())
                continue;

            tileCopy = blocksCopy();
            tileCopy[zx][zy] = tileCopy[blockI][blockJ];
            tileCopy[blockI][blockJ] = 0;

            Board neighbour = new Board(tileCopy);
            neighbors.add(neighbour);
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinBlocks = blocksCopy();

        if (tile[0][0] != 0 && tile[0][1] != 0) {
            twinBlocks[0][0] = tile[0][1];
            twinBlocks[0][1] = tile[0][0];
        } else {
            twinBlocks[1][0] = tile[1][1];
            twinBlocks[1][1] = tile[1][0];
        }

        return new Board(twinBlocks);
    }

    private int[][] blocksCopy() {
        int[][] blocksCopy = new int[dimension()][dimension()];

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                blocksCopy[i][j] = tile[i][j];
            }
        }

        return blocksCopy;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
    }

}
