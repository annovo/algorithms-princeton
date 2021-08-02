/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

public class SAP {
    private final Digraph g;
    private final Map<Integer, Integer> lenghtes;
    private final Map<Integer, Integer> ancestors;
    private boolean[] markedV;
    private boolean[] markedW;
    private int[] distToV;
    private int[] distToW;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("graph cant be null");
        this.g = new Digraph(G);
        this.markedV = new boolean[g.V()];
        this.markedW = new boolean[g.V()];
        this.distToW = new int[g.V()];
        this.distToV = new int[g.V()];
        this.ancestors = new HashMap<>();
        this.lenghtes = new HashMap<>();
    }

    private void clearArrays() {
        for (int i = 0; i < markedV.length; i++)
            markedV[i] = false;

        for (int i = 0; i < markedW.length; i++)
            markedW[i] = false;

        for (int i = 0; i < distToV.length; i++)
            distToV[i] = 0;

        for (int i = 0; i < distToW.length; i++)
            distToW[i] = 0;
    }

    // first returning arg is ancestor and the second one is length
    private int[] bfs(Queue<Integer> vq, Queue<Integer> wq) {
        int[] a = new int[] { -1, -1 };
        int maxDist = Integer.MAX_VALUE;

        while (!vq.isEmpty() || !wq.isEmpty()) {
            if (!vq.isEmpty()) {
                int nv = vq.dequeue();
                for (int adj : g.adj(nv)) {
                    if (markedV[adj])
                        continue;

                    distToV[adj] = distToV[nv] + 1;
                    markedV[adj] = true;
                    if (distToV[adj] > maxDist)
                        break;

                    if (markedW[adj] && (a[1] == -1
                            || a[1] > distToV[adj] + distToW[adj])) {
                        a[0] = adj;
                        a[1] = distToV[adj] + distToW[adj];
                        maxDist = a[1];
                    }
                    vq.enqueue(adj);
                }
            }

            if (!wq.isEmpty()) {
                int nw = wq.dequeue();
                for (int adj : g.adj(nw)) {
                    if (markedW[adj])
                        continue;

                    distToW[adj] = distToW[nw] + 1;
                    markedW[adj] = true;
                    if (distToW[adj] > maxDist)
                        break;

                    if (markedV[adj] && (a[1] == -1
                            || a[1] > distToV[adj] + distToW[adj])) {
                        a[0] = adj;
                        a[1] = distToV[adj] + distToW[adj];
                        maxDist = a[1];
                    }
                    wq.enqueue(adj);
                }
            }
        }

        return a;
    }

    private int[] bfs(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("cant be null");
        clearArrays();
        Queue<Integer> vq = new Queue<>();
        for (Integer nv : v) {
            if (nv == null || nv < 0 || nv >= g.V())
                throw new IllegalArgumentException("vertex is out of range");
            markedV[nv] = true;
            distToV[nv] = 0;
            vq.enqueue(nv);
        }

        Queue<Integer> wq = new Queue<>();
        for (Integer nw : w) {
            if (nw == null || nw < 0 || nw >= g.V())
                throw new IllegalArgumentException("vertex is out of range");
            markedW[nw] = true;
            distToW[nw] = 0;
            wq.enqueue(nw);
        }

        return bfs(vq, wq);
    }

    private int[] bfs(int v, int w) {
        clearArrays();
        Queue<Integer> vq = new Queue<>();
        vq.enqueue(v);
        markedV[v] = true;
        distToV[v] = 0;

        Queue<Integer> wq = new Queue<>();
        wq.enqueue(w);
        markedW[w] = true;
        distToW[w] = 0;

        return bfs(vq, wq);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0 || v >= g.V() || w >= g.V())
            throw new IllegalArgumentException("vertex outside of range");

        if (lenghtes.containsKey(v * g.V() + w))
            return lenghtes.get(v * g.V() + w);

        if (lenghtes.containsKey(w * g.V() + v))
            return lenghtes.get(w * g.V() + v);

        if (v == w)
            return 0;

        int[] res = bfs(v, w);

        ancestors.put(v * g.V() + w, res[0]);
        lenghtes.put(v * g.V() + w, res[1]);

        return res[1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v >= g.V() || w >= g.V())
            throw new IllegalArgumentException("vertex outside of range");

        if (ancestors.containsKey(v * g.V() + w))
            return ancestors.get(v * g.V() + w);

        if (ancestors.containsKey(w * g.V() + v))
            return ancestors.get(w * g.V() + v);

        if (v == w)
            return v;

        int[] res = bfs(v, w);

        ancestors.put(v * g.V() + w, res[0]);
        lenghtes.put(v * g.V() + w, res[1]);

        return res[0];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return bfs(v, w)[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return bfs(v, w)[0];
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        // ArrayList<Integer> a = new ArrayList<Integer>() {
        //     {
        //         add(27963);
        //         add(34371);
        //         add(35575);
        //         add(36401);
        //         add(39059);
        //         add(52562);
        //         add(59382);
        //         add(60190);
        //         add(61845);
        //         add(62060);
        //         add(64561);
        //     }
        // };
        // ArrayList<Integer> b = new ArrayList<Integer>() {
        //     {
        //         add(57130);
        //         add(58938);
        //         add(66460);
        //     }
        // };
        // StdOut.printf("length = %d, ancestor = %d\n",
        //               sap.length(a, b), sap.ancestor(a, b));
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

    }
}
