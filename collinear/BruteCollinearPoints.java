/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> a;

    public BruteCollinearPoints(Point[] points) {
        this.a = findSegments(points);
    }

    private ArrayList<LineSegment> findSegments(Point[] points) {
        ArrayList<LineSegment> r = new ArrayList<>();
        if (points == null)
            throw new IllegalArgumentException("shouldnt be null");

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            if (p == null)
                throw new IllegalArgumentException("shouldnt be null");
            outerloop:
            for (int j = i + 1; j < points.length; j++) {
                Point p2 = points[j];
                if (points[j] == null)
                    throw new IllegalArgumentException("shouldnt be null");
                if (points[j].compareTo(points[i]) == 0)
                    throw new IllegalArgumentException("points should be unique");
                else if (p2.compareTo(p) < 0) {
                    Point t = p;
                    p = p2;
                    p2 = t;
                }
                for (int k = j + 1; k < points.length; k++) {
                    if (points[k] == null)
                        throw new IllegalArgumentException("shouldnt be null");
                    if (points[k].compareTo(points[i]) == 0 || points[k].compareTo(points[j]) == 0)
                        throw new IllegalArgumentException("points should be unique");
                    else if (p.compareTo(points[k]) > 0)
                        p = points[k];
                    else if (p2.compareTo(points[k]) < 0)
                        p2 = points[k];
                    if (points[i].slopeTo(points[k]) == points[i].slopeTo(points[j])) {
                        for (int h = k + 1; h < points.length; h++) {
                            if (points[h] == null)
                                throw new IllegalArgumentException("shouldnt be null");
                            if (points[h].compareTo(points[i]) == 0
                                    || points[h].compareTo(points[j]) == 0
                                    || points[h].compareTo(points[k]) == 0)
                                throw new IllegalArgumentException("points should be unique");
                            if (points[i].slopeTo(points[k]) == points[i].slopeTo(points[h])) {
                                if (points[h].compareTo(p) < 0)
                                    p = points[h];
                                else if (points[h].compareTo(p2) > 0)
                                    p2 = points[h];
                                r.add(new LineSegment(p, p2));
                                break outerloop;
                            }
                        }
                    }
                }
            }
        }
        return r;
    }

    public int numberOfSegments() {
        return a.size();
    }

    public LineSegment[] segments() {
        LineSegment[] r = new LineSegment[a.size()];
        int i = 0;
        for (LineSegment s : a)
            r[i++] = s;
        return r;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
