/*************************************************************************
 * Name: Feng 
 * Email: fw1714@mun.ca
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

  // compare points by slope
  public final Comparator<Point> SLOPE_ORDER = new Comparator<Point>() {
    public int compare(Point p1, Point p2) {
      return Double.compare(slopeTo(p1), slopeTo(p2));
    }
  }; // YOUR
     // DEFINITION
     // HERE

  private final int x; // x coordinate
  private final int y; // y coordinate

  // create the point (x, y)
  public Point(int x, int y) {
    /* DO NOT MODIFY */
    this.x = x;
    this.y = y;
  }

  // plot this point to standard drawing
  public void draw() {
    /* DO NOT MODIFY */
    StdDraw.point(x, y);
  }

  // draw line between this point and that point to standard drawing
  public void drawTo(Point that) {
    /* DO NOT MODIFY */
    StdDraw.line(this.x, this.y, that.x, that.y);
  }

  // slope between this point and that point
  public double slopeTo(Point that) {
    /* YOUR CODE HERE */
    if (that.y == this.y && that.x == this.x) {
      return Double.NEGATIVE_INFINITY;
    } else if (that.y == this.y) {
      return +0.0;
    } else if (that.x == this.x) {
      return Double.POSITIVE_INFINITY;
    } else
      return (that.y - this.y) * 1.0 / (that.x - this.x);
  }

  // is this point lexicographically smaller than that one?
  // comparing y-coordinates and breaking ties by x-coordinates
  public int compareTo(Point that) {
    /* YOUR CODE HERE */
    if (this.y < that.y)
      return -1;
    if (this.y == that.y) {
      if (this.x < that.x)
        return -1;
      if (this.x == that.x)
        return 0;
      else
        return 1;
    } else
      return 1;
  }

  // return string representation of this point
  public String toString() {
    /* DO NOT MODIFY */
    return "(" + x + ", " + y + ")";
  }

  // unit test
  public static void main(String[] args) {
    Point a = new Point(2, 3);
    Point a1 = new Point(2, 3);
    Point b = new Point(4, 6);
    System.out.println("should -1/ " + a.compareTo(b));
    System.out.println("should 0/ " + a.compareTo(a));
    System.out.println("should 0/ " + a.compareTo(a1));
    System.out.println("should 1/ " + b.compareTo(a));
    System.out.println("b slope to a " + a.slopeTo(b));
    System.out
        .println("a1 and b relative to a " + a.SLOPE_ORDER.compare(a1, b));
  }
}
