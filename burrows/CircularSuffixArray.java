/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private Manber m;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException(" args cant be null");
        m = new Manber(s);
    }

    // length of s
    public int length() {
        return m.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > length() - 1)
            throw new IllegalArgumentException("index is outside the range");
        return m.index(i);
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        String s = in.readAll();
        int length = s.length();
        CircularSuffixArray c = new CircularSuffixArray(s);
        for (char ch : s.toCharArray()) {
            StdOut.println(ch - '0');
        }
        for (int i = 0; i < c.length(); i++) {
            StdOut.println(c.index(i));
        }
    }
}
