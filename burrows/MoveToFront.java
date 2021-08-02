/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    private static LinkedList<Character> dict;
    private static final int R = 256;
    private static final int LG_R = 8;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        dict = new LinkedList<>();
        for (int i = 0; i < R; i++)
            dict.add((char) i);

        while (!BinaryStdIn.isEmpty()) {
            char curr = BinaryStdIn.readChar();
            int index = dict.indexOf(curr);
            BinaryStdOut.write(index, LG_R);
            dict.remove(index);
            dict.addFirst(curr);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        dict = new LinkedList<>();
        for (int i = 0; i < R; i++)
            dict.add((char) i);

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readInt(LG_R);
            char curr = dict.get(index);
            BinaryStdOut.write(curr);
            dict.remove(index);
            dict.addFirst(curr);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
    }
}
