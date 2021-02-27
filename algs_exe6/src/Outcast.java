import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wn;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null)
            throw new IllegalArgumentException();

        this.wn = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {

        int maxDistance = 0;
        String lestRelated = nouns[0];

        for (String noun : nouns) {

            int distance = 0;
            for (int i = 0; i < nouns.length; i++) {
                distance += wn.distance(noun, nouns[i]);
            }

            if (maxDistance < distance) {
                maxDistance = distance;
                lestRelated = noun;
            }
        }

        return lestRelated;
    }

    public static void main(String[] args) { // see test client below
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}