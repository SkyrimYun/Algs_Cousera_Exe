import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

   private LineSegment[] segs;

   public BruteCollinearPoints(Point[] points) // finds all line segments containing 4 points
   {
      if (points == null) {
         throw new IllegalArgumentException();
      }

      Point[] pointsCopy = new Point[points.length];

      for (int i = 0; i < points.length; i++) {

         if (points[i] == null) {
            throw new IllegalArgumentException();
         }

         pointsCopy[i] = points[i];
      }

      Arrays.sort(pointsCopy);
      List<LineSegment> lineSegments = new LinkedList<LineSegment>();

      Point previousPoint = null;

      for (int i = 0; i < pointsCopy.length; i++) {

         if (previousPoint != null && pointsCopy[i].compareTo(previousPoint) == 0) {
            throw new IllegalArgumentException();
         } else {
            previousPoint = pointsCopy[i];
         }

         for (int j = i + 1; j < pointsCopy.length; j++) {
            for (int k = j + 1; k < pointsCopy.length; k++) {
               for (int l = k + 1; l < pointsCopy.length; l++) {
                  Point p = pointsCopy[i];
                  Point q = pointsCopy[j];
                  Point r = pointsCopy[k];
                  Point s = pointsCopy[l];

                  if (Double.compare(p.slopeTo(q), p.slopeTo(r)) == 0
                        && Double.compare(p.slopeTo(r), p.slopeTo(s)) == 0) {
                     LineSegment segment = new LineSegment(p, s);
                     lineSegments.add(segment);
                  }
               }
            }
         }
      }

      segs = lineSegments.toArray(new LineSegment[lineSegments.size()]);
   }

   public int numberOfSegments() // the number of line segments
   {
      return segs.length;
   }

   public LineSegment[] segments() // the line segments
   {
      return segs;
   }
}