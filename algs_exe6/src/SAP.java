import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph G;

    private void checkArg(int v, int w) {
        if (v > G.V() - 1 || v < 0)
            throw new IllegalArgumentException();
        if (w > G.V() - 1 || w < 0)
            throw new IllegalArgumentException();
    }

    private void checkArg(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        for (int value : v) {
            if (value > G.V() - 1 || value < 0) {
                throw new IllegalArgumentException();
            }
        }

        for (int value : w) {
            if (value > G.V() - 1 || value < 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }

        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkArg(v, w);

        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(G, w);

        int distance = -1;
        for (int i = 0; i < G.V(); i++) {
            if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                if (vBFS.distTo(i) + wBFS.distTo(i) < distance || distance == -1) {
                    distance = vBFS.distTo(i) + wBFS.distTo(i);
                }
            }
        }

        return distance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        checkArg(v, w);

        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(G, w);

        int acst = -1;
        int distance = -1;
        for (int i = 0; i < G.V(); i++) {
            if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                if (vBFS.distTo(i) + wBFS.distTo(i) < distance || distance == -1) {
                    distance = vBFS.distTo(i) + wBFS.distTo(i);
                    acst = i;
                }
            }
        }

        return acst;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkArg(v, w);

        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(G, w);

        int distance = -1;
        for (int i = 0; i < G.V(); i++) {
            if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                if (vBFS.distTo(i) + wBFS.distTo(i) < distance || distance == -1) {
                    distance = vBFS.distTo(i) + wBFS.distTo(i);
                }
            }
        }

        return distance;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkArg(v, w);

        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(G, w);

        int acst = -1;
        int distance = -1;
        for (int i = 0; i < G.V(); i++) {
            if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                if (vBFS.distTo(i) + wBFS.distTo(i) < distance || distance == -1) {
                    distance = vBFS.distTo(i) + wBFS.distTo(i);
                    acst = i;
                }
            }
        }

        return acst;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}