/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int LG_R = 8;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int length = s.length();
        for (int i = 0; i < length; i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < length; i++) {
            BinaryStdOut.write(s.charAt((length - 1 + csa.index(i)) % length), LG_R);
        }
        BinaryStdOut.close();
    }

    private static char[] lazyKeyCount(int n, StringBuilder s, int start) {
        char[] a = new char[n];
        int[] next = new int[n];
        final int R = 256;

        // calculate frequencies
        int[] freq = new int[R];
        for (int i = 0; i < n; i++)
            freq[s.charAt(i)]++;

        // calculate cumulative frequencies
        int[] cumm = new int[R];
        for (int i = 1; i < R; i++)
            cumm[i] = cumm[i - 1] + freq[i - 1];

        // sort by first char
        for (int i = 0; i < n; i++) {
            a[cumm[s.charAt(i)]] = s.charAt(i);
            next[cumm[s.charAt(i)]++] = i;
        }

        char[] r = new char[n];
        for (int i = 0; i < n; i++) {
            r[i] = a[start];
            start = next[start];
        }

        return r;
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int initInd = BinaryStdIn.readInt();
        StringBuilder s = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            s.append(BinaryStdIn.readChar(LG_R));
        }
        
        char[] arr = lazyKeyCount(s.length(), s, initInd);
        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(arr[i], LG_R);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        else if (args[0].equals("+"))
            inverseTransform();
    }
}
