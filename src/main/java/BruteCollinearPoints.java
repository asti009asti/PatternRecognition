/**
 * Created by asti009asti on 20/02/2017.
 */
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private ArrayList<LineSegment> ls;
    private Point[] pointsCopy;

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        if (points == null) {
            throw new java.lang.NullPointerException();
        }

        pointsCopy = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new java.lang.NullPointerException();
            }
            pointsCopy[i] = points[i];
        }

        Arrays.sort(pointsCopy);
        Point[] pointsAux = new Point[pointsCopy.length];
        System.arraycopy(pointsCopy, 0, pointsAux, 0, pointsCopy.length);

        for (int i = 0; i < pointsAux.length - 1; i++) {
            if (pointsAux[i].compareTo(pointsAux[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
        ls = new ArrayList<LineSegment>();
        ArrayList<ArrayList<Point>> lsPoints = new ArrayList<ArrayList<Point>>();
        ArrayList<Point> segmentPoints = new ArrayList<Point>();
        for (int p1 = 0; p1 < pointsCopy.length - 3; p1++) {
            for (int p2 = p1 + 1; p2 < pointsCopy.length - 2; p2++) {
                double slope1 = pointsAux[p1].slopeTo(pointsAux[p2]);
                for (int p3 = p2 + 1; p3 < pointsCopy.length - 1; p3++) {
                    double slope2 = pointsAux[p1].slopeTo(pointsAux[p3]);
                    for (int p4 = p3 + 1; p4 < pointsCopy.length; p4++) {
                        double slope3 = pointsAux[p1].slopeTo(pointsAux[p4]);
                        if ((slope1 == slope2) && (slope2 == slope3)) {
                            segmentPoints.add(pointsAux[p1]);
                            segmentPoints.add(pointsAux[p4]);
                            if (!exists(segmentPoints, lsPoints)) {
                                lsPoints.add(segmentPoints);
                            }
                            segmentPoints = new ArrayList<Point>();
                        }
                    }
                }
            }
        }
//        assert Arrays.equals(points, pointsCopy);
        for (ArrayList<Point> aSegment : lsPoints) {
            ls.add(new LineSegment(aSegment.get(0), aSegment.get(1)));
        }
    }

    private boolean exists(ArrayList<Point> currentSegment, ArrayList<ArrayList<Point>> lsPoint) {
        for (ArrayList<Point> aSegment : lsPoint) {
            if ((aSegment.get(0).compareTo(currentSegment.get(0)) == 0) &&
                    (aSegment.get(aSegment.size()-1).compareTo(currentSegment.get(currentSegment.size()-1)) == 0)) {
                return true;
            }
        }
        return false;
    }

    public int numberOfSegments() {
        // the number of line segments
        return this.ls.size();
    }

    public LineSegment[] segments() {
        // the line segments
        LineSegment[] segments = new LineSegment[numberOfSegments()];
        segments = ls.toArray(new LineSegment[numberOfSegments()]);
        return segments;
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

