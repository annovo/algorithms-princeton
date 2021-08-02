/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class KdTree {
    private enum Level {
        HORIZONTAL, VERTICAL;
    }

    private class TreeNode {
        Point2D p;
        TreeNode right;
        TreeNode left;
        Level level;

        public TreeNode(Point2D p, Level level) {
            this.p = p;
            this.level = level;
        }
    }

    private TreeNode root;
    private int count;

    public KdTree() {
        this.count = 0;
    }                             // construct an empty set of points

    public boolean isEmpty() {
        return this.count == 0;
    }                   // is the set empty?

    public int size() {
        return this.count;
    }                  // number of points in the set

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("shouldnt be null");

        if (root == null) {
            this.root = new TreeNode(p, Level.VERTICAL);
            count++;
            return;
        }

        TreeNode d = this.root;
        while (true) {
            Point2D current = d.p;
            if (current.equals(p))
                return;

            Level currLev = d.level;
            Level newL = currLev == Level.HORIZONTAL ? Level.VERTICAL : Level.HORIZONTAL;

            if (currLev == Level.VERTICAL && current.x() > p.x()
                    || currLev == Level.HORIZONTAL && current.y() > p.y()) {
                if (d.left == null) {
                    d.left = new TreeNode(p, newL);
                    break;
                }
                else
                    d = d.left;
            }
            else {
                if (d.right == null) {
                    d.right = new TreeNode(p, newL);
                    break;
                }
                else
                    d = d.right;
            }
        }
        this.count++;
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("shouldnt be null");
        TreeNode d = this.root;

        while (d != null) {
            Point2D current = d.p;
            if (current.equals(p))
                return true;
            if (d.level == Level.VERTICAL) {
                if (current.x() > p.x())
                    d = d.left;
                else d = d.right;
            }
            else {
                if (current.y() > p.y())
                    d = d.left;
                else d = d.right;
            }
        }
        return false;
    }            // does the set contain point p?

    private void draw(TreeNode t, double minX, double maxX, double minY, double maxY, int level) {
        if (t == null)
            return;
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        t.p.draw();

        if (t.level == Level.VERTICAL) {
            StdDraw.setPenRadius(0.001);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(t.p.x(), minY, t.p.x(), maxY);
            draw(t.left, minX, t.p.x(), minY, maxY, level + 1);
            draw(t.right, t.p.x(), maxX, minY, maxY, level + 1);
        }
        else {
            StdDraw.setPenRadius(0.001);
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(minX, t.p.y(), maxX, t.p.y());
            draw(t.left, minX, maxX, minY, t.p.y(), level + 1);
            draw(t.right, minX, maxX, t.p.y(), maxY, level + 1);
        }
    }

    public void draw() {
        TreeNode d = root;
        draw(d, 0, 1, 0, 1, 0);
    }           // draw all points to standard draw

    private RectHV leftNodeRect(TreeNode currNode, RectHV currRect) {
        return currNode.level == Level.VERTICAL ?
               new RectHV(currRect.xmin(), currRect.ymin(), currNode.p.x(), currRect.ymax()) :
               new RectHV(currRect.xmin(), currRect.ymin(), currRect.xmax(), currNode.p.y());
    }

    private RectHV rightNodeRect(TreeNode currNode, RectHV currRect) {
        return currNode.level == Level.VERTICAL ?
               new RectHV(currNode.p.x(), currRect.ymin(), currRect.xmax(), currRect.ymax()) :
               new RectHV(currRect.xmin(), currNode.p.y(), currRect.xmax(), currRect.ymax());
    }

    private void pointsInRect(LinkedList<Point2D> list, TreeNode head, RectHV rect, RectHV curr) {
        if (!rect.intersects(curr) || head == null)
            return;

        if (rect.contains(head.p))
            list.add(head.p);

        pointsInRect(list, head.left, rect, leftNodeRect(head, curr));
        pointsInRect(list, head.right, rect, rightNodeRect(head, curr));
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("shouldnt be null");
        LinkedList<Point2D> list = new LinkedList<>();
        pointsInRect(list, root, rect, new RectHV(0, 0, 1, 1));
        return list;
    }          // all points that are inside the rectangle (or on the boundary)

    private Point2D closestPoint(Point2D query, Point2D best, TreeNode head, RectHV curr) {
        if (head == null || query.distanceSquaredTo(best) < curr.distanceSquaredTo(query))
            return best;
        Point2D temp = head.p.distanceSquaredTo(query) > best.distanceSquaredTo(query) ? best :
                       head.p;
        if (head.level == Level.VERTICAL && query.x() > head.p.x()
                || head.level == Level.HORIZONTAL && query.y() > head.p.y()) {
            temp = closestPoint(query, temp, head.right, rightNodeRect(head, curr));
            temp = closestPoint(query, temp, head.left, leftNodeRect(head, curr));
        }
        else {
            temp = closestPoint(query, temp, head.left, leftNodeRect(head, curr));
            temp = closestPoint(query, temp, head.right, rightNodeRect(head, curr));
        }
        return temp;
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("shouldnt be null");
        if (this.root == null)
            return null;
        return closestPoint(p, root.p, root, new RectHV(0, 0, 1, 1));
    }         // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        KdTree t = new KdTree();
        Point2D p = new Point2D(0.576172, 0.470703);
        Point2D p2 = new Point2D(0.676172, 0.470703);
        t.insert(p);
        t.insert(p2);
        StdOut.println(t.size());
        StdOut.println(t.contains(p2));
    }
}
