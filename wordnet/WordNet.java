/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.LinkedList;

public class WordNet {
    private final HashMap<String, LinkedList<Integer>> nouns;
    private final String[] idToNoun;
    private final Digraph g;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("arguments shouldnt be null");
        String[] syn = new In(synsets).readAllLines();
        String[] hyp = new In(hypernyms).readAllLines();
        this.g = new Digraph(syn.length);
        this.idToNoun = new String[syn.length];

        for (String s : hyp) {
            if (s == null)
                throw new IllegalArgumentException("arguments cant be null");
            String[] ids = s.split(",");
            int v = Integer.parseInt(ids[0]);
            if (v < 0 || v >= g.V())
                throw new IllegalArgumentException("vertex is outside the range");
            for (int i = 1; i < ids.length; i++) {
                int w = Integer.parseInt(ids[i]);
                if (w < 0 || w >= g.V())
                    throw new IllegalArgumentException("vertex is outside the range");
                g.addEdge(v, w);
            }
        }

        if (!checkDAG())
            throw new IllegalArgumentException("not a DAG");

        this.nouns = new HashMap<>();
        for (String s : syn) {
            if (s == null)
                throw new IllegalArgumentException("arguments cant be null");
            String[] parsed = s.split(",");
            int id = Integer.parseInt(parsed[0]);
            if (id < 0 || id >= g.V())
                throw new IllegalArgumentException("vertex is outside the range");
            idToNoun[id] = parsed[1];
            String[] set = parsed[1].split(" ");
            for (String noun : set) {
                if (!nouns.containsKey(noun))
                    nouns.put(noun, new LinkedList<>());
                LinkedList<Integer> nounId = nouns.get(noun);
                nounId.add(id);
                nouns.replace(noun, nounId);
            }
        }
        this.sap = new SAP(g);
    }

    private boolean checkDAG() {
        DirectedCycle dc = new DirectedCycle(this.g);
        int count = 0;
        for (int i = 0; i < g.V(); i++) {
            if (g.outdegree(i) == 0)
                count++;
        }
        return count == 1 && !dc.hasCycle();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("shouldnt be null");
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("args cant be null");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("is not wordNet noun");
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("args cant be null");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("is not wordNet noun");
        return idToNoun[sap.ancestor(nouns.get(nounA), nouns.get(nounB))];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String s1 = args[2];
        String s2 = args[3];
        StdOut.println(s1 + ": " + wordnet.isNoun(s1));
        StdOut.println(s1 + "-" + s2 + ": " + wordnet.distance(s1, s2));
        StdOut.println(s1 + "-" + s2 + ": " + wordnet.sap(s1, s2));
    }
}
