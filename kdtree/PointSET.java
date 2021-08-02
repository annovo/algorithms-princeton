/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> ts;

    public PointSET() {
        this.ts = new TreeSet<>();
    }                             // construct an empty set of points

    public boolean isEmpty() {
        return this.ts.isEmpty();
    }                   // is the set empty?

    public int size() {
        return this.ts.size();
    }                  // number of points in the set

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("shouldnt be null");
        this.ts.add(p);
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("shouldnt be null");
        return ts.contains(p);
    }            // does the set contain point p?

    public void draw() {
        for (Point2D p : this.ts) {
            p.draw();
        }
    }           // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("shouldnt be null");
        LinkedList<Point2D> list = new LinkedList<>();
        for (Point2D p : this.ts) {
            if (rect.distanceSquaredTo(p) == 0)
                list.add(p);
        }
        return list;
    }          // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("shouldnt be null");

        if (this.ts.isEmpty())
            return null;

        Point2D best = ts.first();
        for (Point2D q : this.ts) {
            if (best.distanceSquaredTo(p) > q.distanceSquaredTo(p))
                best = q;
        }
        return best;
    }         // a nearest neighbor in the set to point p; null if the set is empty


    public static void main(String[] args) {
        PointSET t = new PointSET();
        Point2D p = new Point2D(0.576172, 0.470703);
        Point2D p2 = new Point2D(0.676172, 0.470703);
        t.insert(p);
        t.insert(p2);
        StdOut.println(t.size());
        StdOut.println(t.contains(p2));
    }
}
