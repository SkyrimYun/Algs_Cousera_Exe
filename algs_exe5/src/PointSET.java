import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
   private TreeSet<Point2D> PTree;

   // construct an empty set of points
   public PointSET() {
      PTree = new TreeSet<Point2D>();
   }

   // is the set empty?
   public boolean isEmpty() {
      return PTree.isEmpty();
   }

   // number of points in the set
   public int size() {
      return PTree.size();
   }

   // add the point to the set (if it is not already in the set)
   public void insert(Point2D p) {
      if (p == null)
         throw new IllegalArgumentException();
      PTree.add(p);
   }

   // does the set contain point p?
   public boolean contains(Point2D p) {
      if (p == null)
         throw new IllegalArgumentException();
      return PTree.contains(p);
   }

   // draw all points to standard draw
   public void draw() {
      for (Point2D p : PTree)
         p.draw();
   }

   // all points that are inside the rectangle (or on the boundary)
   public Iterable<Point2D> range(RectHV rect) {
      if (rect == null)
         throw new IllegalArgumentException();

      TreeSet<Point2D> PinR = new TreeSet<Point2D>();
      for (Point2D p : PTree) {
         if (rect.contains(p))
            PinR.add(p);
      }
      return PinR;
   }

   // a nearest neighbor in the set to point p; null if the set is empty
   public Point2D nearest(Point2D p) {
      if (p == null)
         throw new IllegalArgumentException();

      if (isEmpty())
         return null;

      Point2D nearestPoint = null;

      for (Point2D point : PTree) {
         if (nearestPoint == null) {
            nearestPoint = point;
            continue;
         }

         if (p.distanceSquaredTo(point) < p.distanceSquaredTo(nearestPoint)) {
            nearestPoint = point;
         }
      }

      return nearestPoint;
   }

   public static void main(String[] args) {
   } // unit testing of the methods (optional)
}