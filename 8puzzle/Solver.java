/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;

public class Solver {
    private int moves = -1;
    private LinkedList<Board> solution;

    private class TreeNode {
        private final Board b;
        private final TreeNode prev;
        private final int priority;
        private final int moves;

        public TreeNode(Board b, TreeNode prev, int moves) {
            this.b = b;
            this.prev = prev;
            this.moves = moves;
            this.priority = moves + b.manhattan();
        }

        public int getPriority() {
            return this.priority;
        }

        public TreeNode getPrev() {
            return this.prev;
        }

        public Board getBoard() {
            return this.b;
        }

        public int getMoves() {
            return this.moves;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("must not be null");

        MinPQ<TreeNode> mPQ = new MinPQ<TreeNode>(Comparator.comparing(TreeNode::getPriority));
        MinPQ<TreeNode> mPQAlt = new MinPQ<TreeNode>(Comparator.comparing(TreeNode::getPriority));
        mPQ.insert(new TreeNode(initial, null, 0));
        mPQAlt.insert(new TreeNode(initial.twin(), null, 0));

        TreeNode currentNode = null;
        
        while (!mPQ.isEmpty()) {
            currentNode = mPQ.delMin();
            TreeNode altNode = mPQAlt.delMin();
            Board currentBoard = currentNode.getBoard();
            Board altBoard = altNode.getBoard();
            if (currentBoard.isGoal())
                break;

            if (altBoard.isGoal())
                return;

            Iterable<Board> neighbors = currentBoard.neighbors();
            for (Board b : neighbors) {
                if (currentNode.getPrev() != null && b.equals(currentNode.getPrev().getBoard()))
                    continue;
                mPQ.insert(new TreeNode(b, currentNode, currentNode.getMoves() + 1));
            }

            neighbors = altBoard.neighbors();
            for (Board b : neighbors) {
                if (altNode.getPrev() != null && b.equals(altNode.getPrev().getBoard()))
                    continue;
                mPQAlt.insert(new TreeNode(b, altNode, altNode.getMoves() + 1));
            }
        }


        if (currentNode != null)
            this.moves = currentNode.getMoves();
        this.solution = new LinkedList<Board>();
        while (currentNode != null) {
            this.solution.addFirst(currentNode.getBoard());
            currentNode = currentNode.getPrev();
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.moves != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
