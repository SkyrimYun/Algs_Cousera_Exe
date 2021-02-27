import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Bag;
import java.util.HashMap;

public class WordNet {
    private final HashMap<String, Integer> nouns;
    private final HashMap<Integer, Bag<String>> synsets;
    private final Digraph DAG;
    private final SAP sap;

    private String[] readAllLine(String filename) {
        In in = new In(filename);
        String[] lines = in.readAllLines();
        return lines;
    }

    private void buildSynset(String[] lines) {
        for (String line : lines) {

            String[] fields = line.split(",");

            int id = Integer.parseInt(fields[0]);
            Bag<String> synset = new Bag<String>();

            for (String noun : fields[1].split(" ")) {
                synset.add(noun);
                nouns.put(noun, id);
            }

            this.synsets.put(id, synset);
        }
    }

    private void buildDAG(String[] lines) {
        for (String line : lines) {

            String[] fields = line.split(",");
            int synset = Integer.parseInt(fields[0]);

            for (int i = 1; i < fields.length; i++) {
                int hypernym = Integer.parseInt(fields[i]);
                this.DAG.addEdge(synset, hypernym);
            }
        }
    }

    private void checkRoot() {
        int roots = 0;
        for (int i = 0; i < this.DAG.V(); i++) {
            if (this.DAG.outdegree(i) == 0) {
                roots++;
            }
        }

        if (roots != 1) {
            throw new IllegalArgumentException();
        }
    }

    private void checkCycle() {
        DirectedCycle directedCycle = new DirectedCycle(this.DAG);

        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException();
        }
    }

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        this.synsets = new HashMap<Integer, Bag<String>>();
        this.nouns = new HashMap<String, Integer>();

        buildSynset(readAllLine(synsets));
        this.DAG = new Digraph(this.synsets.keySet().size());
        buildDAG(readAllLine(hypernyms));

        this.sap = new SAP(this.DAG);

        checkRoot();
        checkCycle();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();

        return nouns.get(word) != null;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nouns.get(nounA) == null || nounB == null || nouns.get(nounB) == null)
            throw new IllegalArgumentException();

        int idA = nouns.get(nounA);
        int idB = nouns.get(nounB);
        return sap.length(idA, idB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nouns.get(nounA) == null || nounB == null || nouns.get(nounB) == null)
            throw new IllegalArgumentException();

        int idA = nouns.get(nounA);
        int idB = nouns.get(nounB);
        int ancestor = sap.ancestor(idA, idB);

        String result = "";
        for (String element : synsets.get(ancestor)) {
            result += " " + element;
        }

        return result;
    }

    public static void main(String[] args) {
    }
}