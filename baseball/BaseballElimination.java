/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BaseballElimination {
    private final int numOfteams;
    private final String[] names;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] g;
    private final Map<String, Integer> teams;
    private Set<String> certificateOfElimination;

    public BaseballElimination(String filename) {
        if (filename == null)
            throw new IllegalArgumentException("no null");
        teams = new HashMap<>();
        In in = new In(filename);
        String[] lines = in.readAllLines();
        numOfteams = Integer.parseInt(lines[0]);
        names = new String[numOfteams];
        wins = new int[numOfteams];
        losses = new int[numOfteams];
        remaining = new int[numOfteams];
        g = new int[numOfteams][numOfteams];
        parseIn(lines);
    }

    private void parseIn(String[] lines) {
        for (int i = 1; i < lines.length; i++) {
            String[] info = lines[i].trim().split("\\s+");
            names[i - 1] = info[0];
            teams.put(info[0], i - 1);
            wins[i - 1] = Integer.parseInt(info[1]);
            losses[i - 1] = Integer.parseInt(info[2]);
            remaining[i - 1] = Integer.parseInt(info[3]);

            for (int j = 0; j < numOfteams; j++) {
                g[i - 1][j] = Integer.parseInt(info[j + 4]);
            }
        }
    }

    public int numberOfTeams() {
        return numOfteams;
    }                      // number of teams

    public Iterable<String> teams() {
        return teams.keySet();
    }                                // all teams

    public int wins(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException("no such team");
        return wins[teams.get(team)];
    }                      // number of wins for given team

    public int losses(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException("no such team");
        return losses[teams.get(team)];
    }                    // number of losses for given team

    public int remaining(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException("no such team");
        return remaining[teams.get(team)];
    }                 // number of remaining games for given team

    public int against(String team1, String team2) {
        if (!teams.containsKey(team1) || !teams.containsKey(team2))
            throw new IllegalArgumentException("no such team");
        return g[teams.get(team1)][teams.get(team2)];
    }    // number of remaining games between team1 and team2

    private void createNetwork(FlowNetwork fn, int id) {
        int k = 1;
        int deltaI = 1;
        int deltaJ = 1;
        int numOfGames = (numOfteams - 2) * (numOfteams - 1) / 2;
        for (int i = 0; i < numOfteams; i++) {
            if (i == id) {
                deltaI = 0;
                continue;
            }
            for (int j = i + 1; j < numOfteams; j++) {
                if (j == id) {
                    continue;
                }
                deltaJ = j > id ? 0 : 1;
                fn.addEdge(new FlowEdge(0, k, g[i][j]));
                fn.addEdge(new FlowEdge(k, numOfGames + i + deltaI, Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(k, numOfGames + j + deltaJ, Double.POSITIVE_INFINITY));
                k++;
            }
        }
        deltaI = 1;
        int chance = wins[id] + remaining[id];
        for (int i = 0; i < numOfteams; i++) {
            if (i == id) {
                deltaI = 0;
                continue;
            }
            fn.addEdge(new FlowEdge(numOfGames + i + deltaI, fn.V() - 1, chance - wins[i]));
        }
    }

    private int calculateV() {
        return numOfteams + 1 + (numOfteams - 2) * (numOfteams - 1) / 2;
    }

    public boolean isEliminated(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException("no such team");

        boolean res = false;
        certificateOfElimination = new HashSet<>();
        int numOfGames = (numOfteams - 2) * (numOfteams - 1) / 2;
        int teamId = teams.get(team);
        int chance = wins[teamId] + remaining[teamId];
        for (int i = 0; i < numOfteams; i++) {
            if (i == teamId)
                continue;
            if (chance < wins[i]) {
                certificateOfElimination.add(names[i]);
                res = true;
            }
        }

        if (res)
            return true;

        FlowNetwork fn = new FlowNetwork(calculateV());
        createNetwork(fn, teamId);
        FordFulkerson ff = new FordFulkerson(fn, 0, calculateV() - 1);

        for (int i = 1; i < fn.V() - 1; i++) {
            if (ff.inCut(i)) {
                res = true;
                if (i > numOfGames) {
                    int id = i - numOfGames > teamId ? i - numOfGames : i - numOfGames - 1;
                    certificateOfElimination.add(names[id]);
                }
            }
        }
        return res;
    }              // is given team eliminated?

    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException("no such team");
        if (!isEliminated(team))
            return null;
        else
            return certificateOfElimination;
    }  // subset R of teams that eliminates given team; null if not eliminated

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
