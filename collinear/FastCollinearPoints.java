/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> a;

    public FastCollinearPoints(Point[] p) {
        if (p == null)
            throw new IllegalArgumentException("shouldnt be null");
        for (int i = 0; i < p.length; i++) {
            if (p[i] == null)
                throw new IllegalArgumentException("cant be null");
        }

        a = findSegments(p);
    }

    private boolean checkPoints(Point start, Point finish, ArrayList<Point> sp,
                                ArrayList<Point> fp) {
        for (int i = 0; i < sp.size(); i++) {
            if (start.compareTo(sp.get(i)) == 0 && finish.compareTo(fp.get(i)) == 0)
                return false;
        }
        return true;
    }

    private ArrayList<LineSegment> findSegments(Point[] p) {
        ArrayList<LineSegment> r = new ArrayList<>();
        Point[] collinear = new Point[p.length];

        for (int i = 0; i < p.length; i++)
            collinear[i] = p[i];

        ArrayList<Point> sp = new ArrayList<>();
        ArrayList<Point> fp = new ArrayList<>();

        for (int i = 0; i < p.length - 1; i++) {
            Point ref = p[i];

            Arrays.sort(collinear, ref.slopeOrder());
            double slope = collinear[0].slopeTo(ref);
            int pointNum = 0;
            Point start = ref;
            Point finish = ref;
            for (int j = 1; j < collinear.length; j++) {
                if (ref.compareTo(collinear[j]) == 0)
                    throw new IllegalArgumentException("have to be unique");
                if (slope != collinear[j].slopeTo(ref)) {
                    slope = collinear[j].slopeTo(ref);
                    if (pointNum >= 3 && checkPoints(start, finish, sp, fp)) {
                        r.add(new LineSegment(start, finish));
                        sp.add(start);
                        fp.add(finish);
                    }

                    pointNum = 1;
                    start = collinear[j].compareTo(ref) < 0 ? collinear[j] : ref;
                    finish = collinear[j].compareTo(ref) > 0 ? collinear[j] : ref;
                }
                else {
                    pointNum++;
                    if (collinear[j].compareTo(start) < 0)
                        start = collinear[j];
                    else if (collinear[j].compareTo(finish) > 0)
                        finish = collinear[j];
                }
            }
            if (pointNum >= 3 && checkPoints(start, finish, sp, fp))
                r.add(new LineSegment(start, finish));
            sp.add(start);
            fp.add(finish);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
