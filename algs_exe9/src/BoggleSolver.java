import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashSet;

final class possibleWords {
    private final String word;
    private final boolean[][] marked;
    private final int[] index;
    private ArrayList<String> possibleMatch;

    public possibleWords(String word, boolean[][] marked, int[] index, ArrayList<String> possibleMatch) {
        this.word = word;
        this.marked = marked;
        this.index = index;
        this.possibleMatch = possibleMatch;
    }

    public String getString() {
        return this.word;
    }

    public boolean[][] getMarked() {
        return this.marked;
    }

    public int[] getIndex() {
        return this.index;
    }

    public ArrayList<String> getMatch() {
        return this.possibleMatch;
    }
}

class SortbyWord implements Comparator<possibleWords> {
    public int compare(possibleWords a, possibleWords b) {
        return a.getString().compareTo(b.getString());
    }
}

public class BoggleSolver {

    private final ArrayList<String> dict;
    private BoggleBoard board;
    private int col;
    private int row;
    private HashSet<String> validWords;

    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dict = new ArrayList<String>(Arrays.asList(dictionary));
        this.validWords = new HashSet<>();
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.col = board.cols();
        this.row = board.rows();
        this.board = board;
        this.validWords = new HashSet<>();

        // for (int i = 0; i < col * row; i++) {
        // marked char
        // check and collect possible matches in dicts
        // if(possible matches==empty)
        // continue;
        // add surrounding to quene
        // while(!quene.isEmpty){
        // pop quene
        // update marked
        // check and collect possible matches in dicts
        // if(possible match==empty)
        // continue;
        // if(size==16)
        // continue;
        // if not marked, add surrounding to quene
        // }
        // }

        for (int i = 0; i < this.col * this.row; i++) {
            int indexR = i / this.col;
            int indexC = i % this.col;
            String word = new String();
            char cc = this.board.getLetter(indexR, indexC);
            word = String.valueOf(cc);
            if (cc == 'Q')
                word += "U";

            boolean[][] marked = new boolean[this.row][this.col];
            marked[indexR][indexC] = true;

            ArrayList<String> possibleMatch = new ArrayList<>();
            possibleMatch = checkMatch(word, dict);
            if (possibleMatch.isEmpty())
                continue;

            PriorityQueue<possibleWords> quene = new PriorityQueue<>(100, new SortbyWord());
            quene = addSurroud(indexR, indexC, quene, marked, word, possibleMatch);

            while (!quene.isEmpty()) {
                possibleWords possibleWord = quene.poll();
                word = possibleWord.getString();
                marked = possibleWord.getMarked();
                int[] lastCharIndex = possibleWord.getIndex();
                ArrayList<String> matchs = possibleWord.getMatch();

                matchs = checkMatch(word, matchs);
                if (matchs.isEmpty())
                    continue;

                quene = addSurroud(lastCharIndex[0], lastCharIndex[1], quene, marked, word, matchs);
            }
        }
        return validWords;
    }

    private ArrayList<String> checkMatch(String word, ArrayList<String> dict) {
        ArrayList<String> possibleMatch = new ArrayList<>();
        int n = word.length();
        for (String s : dict) {
            if (s.length() < n)
                continue;
            for (int i = 0; i < n; i++) {
                if (word.charAt(i) != s.charAt(i))
                    break;
                if (i == s.length() - 1) {
                    if (s.length() >= 3)
                        this.validWords.add(s);
                    break;
                }
                if (i == n - 1)
                    possibleMatch.add(s);
            }
        }
        return possibleMatch;
    }

    private PriorityQueue<possibleWords> updateQuene(PriorityQueue<possibleWords> quene, int r, int c, String word,
            boolean[][] marked, ArrayList<String> possibleMatch) {

        boolean[][] markedCopy = deepCopy(marked);
        int[] lastIndex = new int[2];

        char cc = this.board.getLetter(r, c);
        String wordCopy = word + cc;
        if (cc == 'Q') {
            // possibleMatch = checkMatch(word, possibleMatch);
            wordCopy += "U";
        }

        markedCopy[r][c] = true;

        lastIndex[0] = r;
        lastIndex[1] = c;

        possibleWords possibleWord = new possibleWords(wordCopy, markedCopy, lastIndex, possibleMatch);
        quene.add(possibleWord);
        return quene;
    }

    private PriorityQueue<possibleWords> addSurroud(int r, int c, PriorityQueue<possibleWords> quene,
            boolean[][] marked, String word, ArrayList<String> possibleMatch) {
        // move left
        if (c - 1 >= 0 && !marked[r][c - 1]) {
            quene = updateQuene(quene, r, c - 1, word, marked, possibleMatch);
        }
        // move right
        if (c + 1 < this.col && !marked[r][c + 1]) {
            quene = updateQuene(quene, r, c + 1, word, marked, possibleMatch);
        }
        // move up
        if (r - 1 >= 0 && !marked[r - 1][c]) {
            quene = updateQuene(quene, r - 1, c, word, marked, possibleMatch);
        }
        // move down
        if (r + 1 < this.row && !marked[r + 1][c]) {
            quene = updateQuene(quene, r + 1, c, word, marked, possibleMatch);
        }
        // move left-up
        if (c - 1 >= 0 && r - 1 >= 0 && !marked[r - 1][c - 1]) {
            quene = updateQuene(quene, r - 1, c - 1, word, marked, possibleMatch);
        }
        // move left-down
        if (c - 1 >= 0 && r + 1 < this.row && !marked[r + 1][c - 1]) {
            quene = updateQuene(quene, r + 1, c - 1, word, marked, possibleMatch);
        }
        // move right-up
        if (c + 1 < this.col && r - 1 >= 0 && !marked[r - 1][c + 1]) {
            quene = updateQuene(quene, r - 1, c + 1, word, marked, possibleMatch);
        }
        // move right-down
        if (c + 1 < this.col && r + 1 < this.row && !marked[r + 1][c + 1]) {
            quene = updateQuene(quene, r + 1, c + 1, word, marked, possibleMatch);
        }
        return quene;
    }

    private boolean[][] deepCopy(boolean[][] marked) {
        boolean[][] copy = new boolean[this.row][this.col];
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                copy[i][j] = marked[i][j];
            }
        }
        return copy;
    }

    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        for (String s : this.dict) {
            if (s.equals(word)) {
                int n = s.length();
                if (n <= 4)
                    return 1;
                else if (n == 5)
                    return 2;
                else if (n == 6)
                    return 3;
                else if (n == 7)
                    return 5;
                else
                    return 11;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        int score = solver.scoreOf("LOOKING");
        StdOut.println(score);
    }
}
