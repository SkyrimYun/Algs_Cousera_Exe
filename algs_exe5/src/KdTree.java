import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;
import java.util.List;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

   private Node root = null;
   private int size = 0;
   private final boolean xKey = true;

   private class Node {
      private Point2D point;
      private Node left, right;
      private RectHV rect;
      private boolean isVertical;

      public Node(Point2D point, boolean isVertical, Node parent) {
         this.point = point;
         this.isVertical = isVertical;
         size++;

         if (parent == null) {
            this.rect = new RectHV(0, 0, 1, 1);
         } else {
            double minX = parent.rect.xmin();
            double minY = parent.rect.ymin();
            double maxX = parent.rect.xmax();
            double maxY = parent.rect.ymax();

            double result;

            if (isVertical) {
               result = parent.point.y() - point.y();
               if (result > 0) {
                  maxY = parent.point.y();
               } else {
                  minY = parent.point.y();
               }
            } else {
               result = parent.point.x() - point.x();
               if (result > 0) {
                  maxX = parent.point.x();
               } else {
                  minX = parent.point.x();
               }
            }

            this.rect = new RectHV(minX, minY, maxX, maxY);
         }

      }

      public void draw() {
         StdDraw.setPenRadius(0.005);

         if (isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(point.x(), rect.ymin(), point.x(), rect.ymax());
         } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rect.xmin(), point.y(), rect.xmax(), point.y());
         }

         StdDraw.setPenRadius(0.03);
         StdDraw.setPenColor(StdDraw.BLACK);
         point.draw();

         if (left != null) {
            left.draw();
         }

         if (right != null) {
            right.draw();
         }
      }
   }

   // construct an empty set of points
   public KdTree() {
   }

   // is the set empty?
   public boolean isEmpty() {
      return size == 0;
   }

   // number of points in the set
   public int size() {
      return size;
   }

   // add the point to the set (if it is not already in the set)
   public void insert(Point2D p) {
      root = insert(root, null, p, xKey);
   }

   private Node insert(Node x, Node parent, Point2D point, boolean key) {
      if (point == null)
         throw new IllegalArgumentException();

      if (x == null)
         return new Node(point, key, parent);

      double cmp;
      if (key == xKey) {
         cmp = x.point.x() - point.x();
      } else {
         cmp = x.point.y() - point.y();
      }

      if (cmp < 0)
         x.right = insert(x.right, x, point, !key);
      else if (cmp > 0)
         x.left = insert(x.left, x, point, !key);
      else if (cmp == 0) {
         if (x.point.equals(point))
            x.point = point;
         else
            x.right = insert(x.right, x, point, !key);
      }

      return x;
   }

   // does the set contain point p?
   public boolean contains(Point2D p) {

      if (p == null)
         throw new IllegalArgumentException();

      Node x = root;
      boolean key = xKey;

      while (x != null) {
         double cmp;
         if (key == xKey) {
            cmp = x.point.x() - p.x();
         } else {
            cmp = x.point.y() - p.y();
         }

         key = !key;

         if (cmp < 0)
            x = x.right;
         else if (cmp > 0)
            x = x.left;
         else if (cmp == 0) {
            if (x.point.equals(p))
               return true;
            else
               x = x.right;
         }
      }
      return false;
   }

   // draw all points to standard draw
   public void draw() {
      this.root.draw();
   }

   // all points that are inside the rectangle (or on the boundary)
   public Iterable<Point2D> range(RectHV rect) {
      if (rect == null)
         throw new IllegalArgumentException();
      return range(root, rect, new LinkedList<Point2D>());
   }

   private List<Point2D> range(Node x, RectHV rect, List<Point2D> PList) {
      if (x == null)
         return PList;

      if (x.rect.intersects(rect)) {
         if (rect.contains(x.point))
            PList.add(x.point);
         range(x.left, rect, PList);
         range(x.right, rect, PList);
      }
      return PList;
   }

   // a nearest neighbor in the set to point p; null if the set is empty
   public Point2D nearest(Point2D p) {
      if (p == null)
         throw new IllegalArgumentException();

      return nearest(root, p, null);
   }

   private Point2D nearest(Node x, Point2D p, Point2D min) {
      if (x == null)
         return min;

      if (min == null)
         min = x.point;

      if (x.rect.distanceSquaredTo(p) < min.distanceSquaredTo(p)) {

         if (x.point.distanceSquaredTo(p) < min.distanceSquaredTo(p)) {
            min = x.point;
         }

         if (x.isVertical) {
            if (x.point.x() <= p.x()) {
               min = nearest(x.right, p, min);
               min = nearest(x.left, p, min);
            } else {
               min = nearest(x.left, p, min);
               min = nearest(x.right, p, min);
            }

         } else {
            if (x.point.y() <= p.y()) {
               min = nearest(x.right, p, min);
               min = nearest(x.left, p, min);
            } else {
               min = nearest(x.left, p, min);
               min = nearest(x.right, p, min);
            }
         }

      }
      return min;

   }

   public static void main(String[] args) {

      String filename = args[0];
      In in = new In(filename);

      KdTree kdtree = new KdTree();
      while (!in.isEmpty()) {
         double x = in.readDouble();
         double y = in.readDouble();
         Point2D p = new Point2D(x, y);
         kdtree.insert(p);
      }
      Point2D nst = new Point2D(0.291, 0.265);
      nst = kdtree.nearest(nst);
      double x = nst.x();
      double y = nst.y();
      StdOut.printf("%8.6f %8.6f\n", x, y);
   }
}