/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int openSites = 0;
    private final int gridN;
    private final WeightedQuickUnionUF union;
    private final WeightedQuickUnionUF oneWayUnion;
    private boolean[][] grid;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Number should be greater then 0");

        gridN = n;
        union = new WeightedQuickUnionUF(gridN * gridN + 2);
        oneWayUnion = new WeightedQuickUnionUF(gridN * gridN + 1);

        grid = new boolean[gridN][gridN];
    }

    public void open(int row, int col) {
        isWithinRange(row, col);

        if (isOpen(row, col))
            return;

        int numberInUnion = getNumberUnion(row, col);
        grid[row - 1][col - 1] = true;
        openSites++;

        if (row == 1) {
            union.union(numberInUnion, 0);
            oneWayUnion.union(numberInUnion, 0);
        }

        if (row == gridN) {
            union.union(numberInUnion, gridN * gridN + 1);
        }

        setConnection(row, col);
    }

    private void setConnection(int r, int c) {
        int currNum = getNumberUnion(r, c);
        if (c < gridN && grid[r - 1][c]) {
            union.union(currNum, getNumberUnion(r, c + 1));
            oneWayUnion.union(currNum, getNumberUnion(r, c + 1));
        }
        if (c - 2 >= 0 && grid[r - 1][c - 2]) {
            union.union(currNum, getNumberUnion(r, c - 1));
            oneWayUnion.union(currNum, getNumberUnion(r, c - 1));
        }
        if (r < gridN && grid[r][c - 1]) {
            union.union(currNum, getNumberUnion(r + 1, c));
            oneWayUnion.union(currNum, getNumberUnion(r + 1, c));
        }

        if (r - 2 >= 0 && grid[r - 2][c - 1]) {
            union.union(currNum, getNumberUnion(r - 1, c));
            oneWayUnion.union(currNum, getNumberUnion(r - 1, c));
        }

    }

    public boolean isOpen(int row, int col) {
        isWithinRange(row, col);
        return grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        isWithinRange(row, col);
        return oneWayUnion.find(getNumberUnion(row, col)) == oneWayUnion.find(0);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return union.find(0) == union.find(gridN * gridN + 1);
    }

    private int getNumberUnion(int r, int c) {
        return gridN * (r - 1) + c;
    }

    private void isWithinRange(int row, int col) {
        if (row <= 0 || row > gridN || col <= 0 || col > gridN)
            throw new IllegalArgumentException("Row and column should be within grid range");
    }

    public static void main(String[] args) {
        Percolation perc = new Percolation(10);
        perc.open(11, 2);
    }
}
