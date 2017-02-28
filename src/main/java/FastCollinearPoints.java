import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class FastCollinearPoints {

    private ArrayList<LineSegment> ls;
    private Point[] pointsCopy;

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
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

        ls = new ArrayList<LineSegment>();
        ArrayList<Double> slopesChecked = new ArrayList<Double>();
        ArrayList<Point> pointsChecked = new ArrayList<>();

        for (int i = 0; i < pointsCopy.length; i++) {
            Arrays.sort(pointsAux, i, pointsCopy.length, pointsCopy[i].slopeOrder());

            if ((pointsAux.length > 1) && (i < pointsCopy.length - 1) &&
                    (pointsCopy[i].slopeTo(pointsAux[i+1]) == Double.NEGATIVE_INFINITY)) {
                throw new IllegalArgumentException();
            }

            ArrayList<Point> segmentPoints = new ArrayList<Point>();
            int count = 0;
            double prevSlope = pointsCopy[i].slopeTo(pointsAux[pointsAux.length - 1]);

            for (int indexAux = pointsAux.length - 1; indexAux > i; indexAux--) {
                double nextSlope = pointsCopy[i].slopeTo(pointsAux[indexAux - 1]);
                if (!slopesChecked.contains(prevSlope) || !matches(prevSlope, pointsChecked, pointsAux[indexAux])) {
                    if (prevSlope == nextSlope) {
                        segmentPoints.add(pointsAux[indexAux]);
                        count = count + 1;
                    }
                    else if (count >= 2) {
                        slopesChecked.add(prevSlope);
                        segmentPoints.add(pointsAux[indexAux]);
                        segmentPoints.add(pointsCopy[i]);
                        Collections.sort(segmentPoints);
                        ls.add(new LineSegment(segmentPoints.get(0), segmentPoints.get(segmentPoints.size()-1)));
                        segmentPoints = new ArrayList<Point>();
                        count = 0;
                    }
                    else {
                        segmentPoints = new ArrayList<Point>();
                        count = 0;
                    }
                }
                prevSlope = nextSlope;
            }
            pointsChecked.add(pointsCopy[i]);
        }
//        assert Arrays.equals(points, pointsCopy);
    }


    private boolean matches(double slope, ArrayList<Point> pointsChecked, Point currentPoint) {
        for (Point p: pointsChecked) {
            if (currentPoint.slopeTo(p) == slope)
                return true;
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
//        for (int i = 0; i < segments.length; i++) {
//            segments[i] = ls.get(i);
//        }
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

        StdDraw.show();

    }

}
