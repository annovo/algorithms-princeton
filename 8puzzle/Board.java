/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Board {
    private final int[][] tiles;
    private int zeroI;
    private int zeroJ;
    private int humming = 0;
    private int manhattan = 0;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int curr = tiles[i][j];
                this.tiles[i][j] = curr;
                if (curr == 0) {
                    this.zeroI = i;
                    this.zeroJ = j;
                }
                if (curr != 0 && curr != (i * n + j + 1)) {
                    this.humming++;
                    this.manhattan += Math.abs(i - (curr - 1) / n) + Math.abs(j - (curr - 1) % n);
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(n);
        for (int i = 0; i < n; i++) {
            stringBuilder.append("\n");
            for (int j = 0; j < n; j++) {
                stringBuilder.append(" " + this.tiles[i][j]);
            }
        }
        return stringBuilder.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return this.humming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return this.manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.humming == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) return false;
        Board ny = (Board) y;
        if (ny.dimension() != this.dimension() || ny.manhattan != this.manhattan
                || ny.humming != this.humming || this.zeroJ != ny.zeroJ || ny.zeroI != this.zeroI)
            return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != ny.tiles[i][j])
                    return false;
            }
        }
        return true;
    }

    private void swap(int i, int j, int k, int h, int[][] arr) {
        int temp = arr[i][j];
        arr[i][j] = arr[k][h];
        arr[k][h] = temp;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = new LinkedList<Board>();

        if (zeroI > 0) {
            swap(zeroI, zeroJ, zeroI - 1, zeroJ, tiles);
            neighbors.add(new Board(tiles));
            swap(zeroI - 1, zeroJ, zeroI, zeroJ, tiles);
        }
        if (zeroI + 1 < n) {
            swap(zeroI, zeroJ, zeroI + 1, zeroJ, tiles);
            neighbors.add(new Board(tiles));
            swap(zeroI + 1, zeroJ, zeroI, zeroJ, tiles);
        }
        if (zeroJ > 0) {
            swap(zeroI, zeroJ, zeroI, zeroJ - 1, tiles);
            neighbors.add(new Board(tiles));
            swap(zeroI, zeroJ - 1, zeroI, zeroJ, tiles);
        }
        if (zeroJ + 1 < n) {
            swap(zeroI, zeroJ, zeroI, zeroJ + 1, tiles);
            neighbors.add(new Board(tiles));
            swap(zeroI, zeroJ + 1, zeroI, zeroJ, tiles);
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] b = new int[n][n];
        boolean swapped = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                b[i][j] = tiles[i][j];
                if (!swapped && j > 0 && b[i][j] != 0 && b[i][j - 1] != 0) {
                    swap(i, j, i, j - 1, b);
                    swapped = true;
                }
            }
        }
        return new Board(b);
    }

    public static void main(String[] args) {
        int[][] a = new int[][] { { 1, 6, 4 }, { 0, 7, 8 }, { 2, 3, 5 } };
        Board board = new Board(a);
        StdOut.println(board.toString());
        StdOut.println(board.twin().toString());
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        board.neighbors().forEach(b -> StdOut.println(b.toString()));
    }
}
