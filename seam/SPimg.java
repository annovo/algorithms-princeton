/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Stack;

public class SPimg {
    private static final boolean HORIZONTAL = true;
    private double[] distTo;
    private int[] pathTo;
    private final IndexMinPQ<Double> pq;
    private double finalPower = Double.POSITIVE_INFINITY;
    private int finalPixel = 0;

    public SPimg(double[] energy, int width, boolean dir) {
        this.distTo = new double[energy.length];
        this.pathTo = new int[energy.length];

        pq = new IndexMinPQ<>(energy.length);
        if (dir == HORIZONTAL)
            horizontalSeam(width, energy);
        else
            verticalSeam(width, energy);
    }

    private void verticalSeam(int width, double[] energy) {
        for (int i = 0; i < width; i++) {
            pq.insert(i, energy[i]);
            distTo[i] = energy[i];
            pathTo[i] = -1;
        }

        while (!pq.isEmpty()) {
            int v = pq.delMin();
            if (v + width < energy.length) {
                relax(v + width, v, energy[v + width]);
                if (v % width > 0)
                    relax(v + width - 1, v, energy[v + width - 1]);
                if (v % width + 1 < width)
                    relax(v + width + 1, v, energy[v + width + 1]);
            }
            else if (finalPower > distTo[v]) {
                finalPower = distTo[v];
                finalPixel = v;
            }
        }
    }

    private void horizontalSeam(int width, double[] energy) {
        int height = energy.length / width;
        for (int j = 0; j < height; j++) {
            int i = j * width;
            pq.insert(i, energy[i]);
            distTo[i] = energy[i];
            pathTo[i] = -1;
        }

        while (!pq.isEmpty()) {
            int v = pq.delMin();
            if (v % width + 1 < width) {
                relax(v + 1, v, energy[v + 1]);
                if (v / width > 0)
                    relax(v - width + 1, v, energy[v - width + 1]);
                if (v / width + 1 < height)
                    relax(v + width + 1, v, energy[v + width + 1]);
            }
            else if (finalPower > distTo[v]) {
                finalPower = distTo[v];
                finalPixel = v;
            }
        }
    }

    private void relax(int w, int v, double energy) {
        if (distTo[w] == 0 || distTo[w] > distTo[v] + energy) {
            distTo[w] = distTo[v] + energy;
            pathTo[w] = v;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else pq.insert(w, distTo[w]);
        }
    }

    public Stack<Integer> getPath(int width, boolean dir) {
        Stack<Integer> s = new Stack<>();
        for (int t = finalPixel; t != -1; t = pathTo[t]) {
            if (dir == HORIZONTAL)
                s.push(t / width);
            else
                s.push(t % width);
        }
        return s;
    }

    public double getPower() {
        return finalPower;
    }

    // empty because i want to
    public static void main(String[] args) {

    }
}
