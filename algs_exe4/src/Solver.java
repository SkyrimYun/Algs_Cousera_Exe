import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private searchNode solutionNode;

    private class searchNode implements Comparable<searchNode> {
        private Board board;
        private int numMove;
        private searchNode prev;
        private int priority;

        public searchNode(Board cur, searchNode parent) {
            board = cur;
            prev = parent;
            numMove = prev == null ? 0 : parent.numMove + 1;
            priority = cur.manhattan() + numMove;
        }

        public void neighbours(MinPQ<searchNode> pq) {
            for (Board b : board.neighbors()) {
                if (prev != null && b.equals(prev.board)) {
                    continue;
                }

                pq.insert(new searchNode(b, this));
            }
        }

        public int compareTo(searchNode that) {
            return this.priority - that.priority;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        MinPQ<searchNode> solutionPQ = new MinPQ<searchNode>();
        MinPQ<searchNode> twinPQ = new MinPQ<searchNode>();

        solutionPQ.insert(new searchNode(initial, null));

        twinPQ.insert(new searchNode(initial.twin(), null));

        while (true) {
            searchNode sn = solutionPQ.delMin();
            searchNode tsn = twinPQ.delMin();

            if (sn.board.isGoal()) {
                this.solutionNode = sn;
                break;
            } else if (tsn.board.isGoal()) {
                this.solutionNode = null;
                break;
            }

            sn.neighbours(solutionPQ);
            tsn.neighbours(twinPQ);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solutionNode != null;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable()) {
            return this.solutionNode.numMove;
        } else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (isSolvable()) {
            Deque<Board> solution = new LinkedList<>();
            searchNode node = this.solutionNode;
            while (node != null) {
                solution.addFirst(node.board);
                node = node.prev;
            }
            return solution;
        } else
            return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        // System.out.println("get input");
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}