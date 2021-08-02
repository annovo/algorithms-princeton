/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet w;

    public Outcast(WordNet wordnet) {
        this.w = wordnet;
    }        // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        if (nouns == null)
            throw new IllegalArgumentException("cant be null");
        int distance = Integer.MIN_VALUE;
        String res = "";
        for (String s1 : nouns) {
            int tempDist = 0;
            for (String s2 : nouns)
                tempDist += w.distance(s1, s2);
            if (distance < tempDist) {
                distance = tempDist;
                res = s1;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
