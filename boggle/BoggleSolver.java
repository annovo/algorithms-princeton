/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.LinkedList;

public class BoggleSolver {
    private int score = 0;

    private class Node {
        private int score = 0;
        private boolean val = false;
        private boolean access = false;
        private Node[] next = new Node[26];
    }

    private class LazyTrie {
        private Node root;

        public LazyTrie() {
        }

        public void put(String s) {
            root = put(s, root, 0);
        }

        private Node put(String s, Node x, int d) {
            if (x == null) x = new Node();
            if (d == s.length()) {
                x.val = true;
                x.score = findScore(s.length());
                return x;
            }
            char c = s.charAt(d);
            x.next[c - 65] = put(s, x.next[c - 65], d + 1);
            return x;
        }

        public int get(String s) {
            return get(s, root, 0);
        }

        private int get(String s, Node x, int d) {
            if (x == null) return 0;
            if (d == s.length()) return x.score;
            return get(s, x.next[s.charAt(d) - 65], d + 1);
        }

        private int findScore(int length) {
            if (length < 3)
                return 0;
            else if (length == 3 || length == 4)
                return 1;
            else if (length == 5)
                return 2;
            else if (length == 6)
                return 3;
            else if (length == 7)
                return 5;
            return 11;
        }
    }

    private final LazyTrie dictB;
    private boolean[][] marked;
    private ArrayList<Node> foundNodes;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictB = new LazyTrie();
        for (String s : dictionary)
            dictB.put(s);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    private void getAllValidWords(BoggleBoard board, LinkedList<String> a, int i, int j,
                                  StringBuilder ns, Node curr) {
        if (i < 0 || j < 0 || i >= board.rows() || j >= board.cols() || marked[i][j])
            return;

        marked[i][j] = true;
        char c = board.getLetter(i, j);
        Node nc = curr.next[c - 65];

        if (c == 'Q') {
            ns.append("QU");
            if (nc != null)
                nc = nc.next['U' - 65];
        }
        else
            ns.append(c);

        boolean end = nc == null;
        if (ns.length() > 2 && !end) {
            String str = ns.toString();
            boolean found = nc.access;
            boolean val = nc.val;
            if (val && !found) {
                a.add(str);
                score += nc.score;
                nc.access = true;
                foundNodes.add(nc);
            }
        }
        if (!end) {
            getAllValidWords(board, a, i + 1, j, ns, nc);
            getAllValidWords(board, a, i, j + 1, ns, nc);
            getAllValidWords(board, a, i + 1, j + 1, ns, nc);
            getAllValidWords(board, a, i - 1, j - 1, ns, nc);
            getAllValidWords(board, a, i, j - 1, ns, nc);
            getAllValidWords(board, a, i - 1, j, ns, nc);
            getAllValidWords(board, a, i - 1, j + 1, ns, nc);
            getAllValidWords(board, a, i + 1, j - 1, ns, nc);
        }
        marked[i][j] = false;
        if (c == 'Q')
            ns.delete(ns.length() - 2, ns.length());
        else
            ns.deleteCharAt(ns.length() - 1);
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        LinkedList<String> res = new LinkedList<>();
        marked = new boolean[board.rows()][board.cols()];
        foundNodes = new ArrayList<>();
        score = 0;
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                getAllValidWords(board, res, i, j, new StringBuilder(), dictB.root);
            }
        }
        for (Node x : foundNodes)
            x.access = false;
        return res;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return dictB.get(word);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard b = new BoggleBoard();
        //int score = 0;
        while (solver.score < 4540) {
            b = new BoggleBoard();
            solver.getAllValidWords(b);
            if (solver.score > 2000)
                StdOut.println("Score = " + solver.score);
        }
        // BoggleBoard board = new BoggleBoard(args[1]);
        // int score = 0;
        // int size = 0;
        // for (String word : solver.getAllValidWords(board)) {
        //     StdOut.println(word);
        //     score += solver.scoreOf(word);
        //     size++;
        // }
        StdOut.println("board = " + b.toString());
        StdOut.println("Score = " + solver.score);
    }
}
