import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.HashSet;

public class BaseballElimination {

    private int N;
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] a;
    private HashMap<String, HashSet<String>> eliminationMap;
    private HashMap<String, Integer> teamName;
    private String[] teams;

    public BaseballElimination(String filename) // create a baseball division from given filename
    {
        In file = new In(filename);
        this.N = file.readInt();
        this.w = new int[N];
        this.l = new int[N];
        this.r = new int[N];
        this.a = new int[N][N];
        this.eliminationMap = new HashMap<>();
        this.teamName = new HashMap<>();
        this.teams = new String[this.N];

        for (int i = 0; i < this.N; i++) {
            this.teams[i] = file.readString();
            this.teamName.put(teams[i], i);
            this.w[i] = file.readInt();
            this.l[i] = file.readInt();
            this.r[i] = file.readInt();
            for (int j = 0; j < this.N; j++) {
                this.a[i][j] = file.readInt();
            }
        }
    }

    public int numberOfTeams() // number of teams
    {
        return this.N;
    }

    public Iterable<String> teams() // all teams
    {
        return this.teamName.keySet();
    }

    public int wins(String team) // number of wins for given team
    {
        if (this.teamName.get(team) == null) {
            throw new IllegalArgumentException();
        }
        int i = this.teamName.get(team);
        return this.w[i];
    }

    public int losses(String team) // number of losses for given team
    {
        if (this.teamName.get(team) == null) {
            throw new IllegalArgumentException();
        }
        int i = teamName.get(team);
        return this.l[i];
    }

    public int remaining(String team) // number of remaining games for given team
    {
        if (this.teamName.get(team) == null) {
            throw new IllegalArgumentException();
        }
        int i = teamName.get(team);
        return this.r[i];
    }

    public int against(String team1, String team2) // number of remaining games between team1 and team2
    {
        if (this.teamName.get(team1) == null || this.teamName.get(team2) == null) {
            throw new IllegalArgumentException();
        }
        int i = teamName.get(team1);
        int j = teamName.get(team2);
        return this.a[i][j];

    }

    public boolean isEliminated(String team) // is given team eliminated?
    {
        if (this.teamName.get(team) == null) {
            throw new IllegalArgumentException();
        }
        int index = teamName.get(team);

        if (!TrivialElimination(index, team)) {
            int match = 0;
            for (int i = this.N - 1; i > 0; i--) {
                match += i;
            }
            int totalNode = 2 + match + this.N;

            FlowNetwork fn = new FlowNetwork(totalNode);
            HashMap<Integer, int[]> competeTeams = new HashMap<>();

            int team1 = 0;
            int vertexInd = 1;
            while (team1 < this.N - 1) {
                for (int team2 = team1 + 1; team2 < this.N; team2++) {
                    int[] rivals = { team1, team2 };
                    competeTeams.put(vertexInd, rivals);

                    fn.addEdge(new FlowEdge(0, vertexInd, a[team1][team2]));
                    fn.addEdge(new FlowEdge(vertexInd, team1 + match + 1, Double.POSITIVE_INFINITY));
                    fn.addEdge(new FlowEdge(vertexInd, team2 + match + 1, Double.POSITIVE_INFINITY));
                    vertexInd++;
                }
                team1++;
            }

            for (int i = match + 1; i <= totalNode - 2; i++) {
                fn.addEdge(new FlowEdge(i, totalNode - 1, this.w[index] + this.r[index] - this.w[i - match - 1]));
            }

            // fn.addEdge(new FlowEdge(match + 1 + index, totalNode - 1, this.r[index],
            // this.r[index]));

            FordFulkerson ff = new FordFulkerson(fn, 0, totalNode - 1);
            HashSet<String> eliminatingTeams = new HashSet<>();
            for (int i = 1; i <= match; i++) {
                if (ff.inCut(i)) {
                    eliminatingTeams.add(teams[competeTeams.get(i)[0]]);
                    eliminatingTeams.add(teams[competeTeams.get(i)[1]]);
                }
            }
            this.eliminationMap.put(team, eliminatingTeams);
            return !eliminatingTeams.isEmpty();
        }
        return true;
    }

    private boolean TrivialElimination(int index, String team) {
        int highestScore = this.w[index] + this.r[index];
        HashSet<String> eliminatingTeams = new HashSet<>();

        for (int i = 0; i < this.N; i++) {
            if (i == index)
                continue;

            if (highestScore < w[i]) {
                eliminatingTeams.add(this.teams[i]);
            }
        }
        this.eliminationMap.put(team, eliminatingTeams);
        return !eliminatingTeams.isEmpty();
    }

    public Iterable<String> certificateOfElimination(String team) // subset R of teams that eliminates given team; null
                                                                  // if not eliminated
    {
        if (this.teamName.get(team) == null) {
            throw new IllegalArgumentException();
        }
        return isEliminated(team) ? this.eliminationMap.get(team) : null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
