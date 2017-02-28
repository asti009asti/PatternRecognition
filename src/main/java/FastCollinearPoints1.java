import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints1 {

    private ArrayList<LineSegment> ls;

    public FastCollinearPoints1(Point[] points) {
        // finds all line segments containing 4 or more points
        if (points == null) {
            throw new java.lang.NullPointerException();
        }
        Point[] pointsAux = new Point[points.length];
        for (int startPoint = 0; startPoint < points.length; startPoint++) {
            if (points[startPoint] == null) {
                throw new java.lang.NullPointerException();
            }
            pointsAux[startPoint] = points[startPoint];
        }

        ls = new ArrayList<LineSegment>();

        for (int i = 0; i < points.length; i++) {
            Arrays.sort(pointsAux, points[i].slopeOrder());

            for (int p = 1; p < pointsAux.length; p++) {
                int count = 1;
                double slope = points[i].slopeTo(pointsAux[p]);
                //StdOut.println(points[i].toString() + points_aux[p].toString() + slope);
                if (slope == Double.NEGATIVE_INFINITY) { throw new IllegalArgumentException(); }
                Point start, end;
                if (points[i].compareTo(pointsAux[p]) < 0) {
                    start = points[i];
                    end = pointsAux[p];
                }
                else {
                    start = pointsAux[p];
                    end = points[i];
                }
                while (p <= pointsAux.length - 2 && slope == points[i].slopeTo(pointsAux[p+1])) {
                    count++;
                    //StdOut.println(points[i].toString() + points_aux[p].toString() + slope);
                    if (pointsAux[p + 1].compareTo(start) < 0) start = pointsAux[p + 1];
                    else if (pointsAux[p + 1].compareTo(end) > 0) end = pointsAux[p + 1];
                    p++;
                }

                if (count >= 3 && !exists(ls, start, end)) {
                    ls.add(new LineSegment(start, end));
                }
            }
        }

        //segments = ls.toArray(new LineSegment[ls.size()]);
    }

    private boolean exists(ArrayList<LineSegment> ls, Point p1, Point p2) {
        LineSegment s = new LineSegment(p1, p2);
        for (LineSegment asegment: ls) {
            if (asegment.toString().equals(s.toString()))
                return true;
        }
        return false;
    }


    public int numberOfSegments() {
        // the number of line segments
        return ls.size();
    }

    public LineSegment[] segments() {
        // the line segments
        return ls.toArray(new LineSegment[ls.size()]);
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
        FastCollinearPoints1 collinear = new FastCollinearPoints1(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
