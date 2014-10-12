import java.util.TreeSet;

public class PointSET {

  private TreeSet<Point2D> tset;

  public PointSET() {
    tset = new TreeSet<Point2D>();
  }

  public boolean isEmpty() {
    return tset.isEmpty();
  } // is the set empty?

  public int size() {
    return tset.size();
  } // number of points in the set

  public void insert(Point2D p) {
    tset.add(p);
  } // add the point to the set (if it is not already in the set)

  public boolean contains(Point2D p) {
    return tset.contains(p);
  } // does the set contain point p?

  public void draw() {
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(.01) ;
    for (Point2D p : tset) {
      p.draw();
    }
  } // draw all points to standard draw

  public Iterable<Point2D> range(RectHV rect) {
    TreeSet<Point2D> result = new TreeSet<>();
    for (Point2D p : tset) {
      if (rect.contains(p))
        result.add(p);
    }
    return result;
  } // all points that are inside the rectangle

  public Point2D nearest(Point2D p) {
    double nearest = Double.MAX_VALUE;
    Point2D nearestP = null;
    for (Point2D thisp : tset) {
      if (p != thisp) {
        double distance = p.distanceSquaredTo(thisp);
        if (distance < nearest) {
          nearest = distance;
          nearestP = thisp;
        }
      }
    }
    return nearestP;
  }

  public static void main(String[] args) {
    String filename = args[0];
    In in = new In(filename);

    StdDraw.show(0);

    // initialize the two data structures with point from standard input
    PointSET brute = new PointSET();
    while (!in.isEmpty()) {
        double x = in.readDouble();
        double y = in.readDouble();
        Point2D p = new Point2D(x, y);
        brute.insert(p);
    }
    StdDraw.show(50);
    brute.draw();
    StdDraw.show(50);
  }

}
