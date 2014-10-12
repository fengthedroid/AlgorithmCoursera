import java.util.TreeSet;

public class KdTree {
  private Node root;
  private static boolean LEFTRIGHT = true;
  private static boolean TOPBOTTOM = false;

  private static class Node {
    private Point2D p; // the point
    private RectHV rect; // the axis-aligned rectangle corresponding to this
                         // node
    private Node lb; // the left/bottom subtree
    private Node rt; // the right/top subtree
    private boolean leftRight;
    private int N;

    public boolean contains(Point2D p_) {
      if (this.p.equals(p_))
        return true;
      else {
        if ((this.leftRight == LEFTRIGHT && p_.x() < this.p.x())
            || (this.leftRight == TOPBOTTOM && p_.y() < this.p.y())) {
          if (this.lb == null)
            return false;
          else
            return this.lb.contains(p_);
        } else {
          if (this.rt == null)
            return false;
          else
            return this.rt.contains(p_);
        }
      }
    }

    public void insert(Point2D p_) {
      if ((this.leftRight == LEFTRIGHT && p_.x() < this.p.x())
          || (this.leftRight == TOPBOTTOM && p_.y() < this.p.y())) {
        if (this.lb == null) {
          if (this.leftRight)
            this.lb = new Node(p_, new RectHV(this.rect.xmin(),
                this.rect.ymin(), this.p.x(), this.rect.ymax()),
                !this.leftRight);
          else
            this.lb = new Node(p_, new RectHV(this.rect.xmin(),
                this.rect.ymin(), this.rect.xmax(), this.p.y()),
                !this.leftRight);
        } else
          this.lb.insert(p_);
      } else {
        if (this.rt == null) {
          if (this.leftRight)
            this.rt = new Node(p_, new RectHV(this.p.x(), this.rect.ymin(),
                this.rect.xmax(), this.rect.ymax()), !this.leftRight);
          else
            this.rt = new Node(p_, new RectHV(this.rect.xmin(), this.p.y(),
                this.rect.xmax(), this.rect.ymax()), !this.leftRight);
        } else
          this.rt.insert(p_);
      }
      ++N;
    }

    public void draw() {
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setPenRadius(.01);
      this.p.draw();
      if (this.leftRight)
        StdDraw.setPenColor(StdDraw.RED);
      else
        StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.setPenRadius();
      if (this.leftRight)
        StdDraw
            .line(this.p.x(), this.rect.ymin(), this.p.x(), this.rect.ymax());
      else
        StdDraw
            .line(this.rect.xmin(), this.p.y(), this.rect.xmax(), this.p.y());
      if (this.lb != null)
        this.lb.draw();
      if (this.rt != null)
        this.rt.draw();
    }

    public Node(Point2D p_, RectHV rec_, boolean lr_) {
      p = p_;
      rect = rec_;
      N = 1;
      leftRight = lr_;
    }

    public int size() {
      return N;
    }
  }

  public KdTree() {
    root = null;
  }

  public boolean isEmpty() {
    return root == null;
  } // is the set empty?

  public int size() {
    if (root == null)
      return 0;
    else
      return root.size();
  } // number of points in the set

  public boolean contains(Point2D p) {
    if (root == null)
      return false;
    else
      return root.contains(p);
  }

  public void insert(Point2D p) {
    if (root == null)
      root = new Node(p, new RectHV(0, 0, 1, 1), LEFTRIGHT);
    else if (!root.contains(p))
      root.insert(p);
  } // add the point to the set (if it is not already in the set)

  public void draw() {
    if (root != null)
      root.draw();
  } // draw all points to standard draw

  public Iterable<Point2D> range(RectHV rect) {
    TreeSet<Point2D> result = new TreeSet<Point2D>();
    if (root != null)
      range(root, result, rect);
    return result;
  } // all points that are inside the rectangle

  private void range(Node n, TreeSet<Point2D> set, RectHV rect) {
    if (n == null)
      return;
    if (rect.contains(n.p))
      set.add(n.p);
    RectHV line;
    if (n.leftRight)
      line = new RectHV(n.p.x(), 0, n.p.x(), 1);
    else
      line = new RectHV(0, n.p.y(), 1, n.p.y());
    if (!rect.intersects(line)) {
      if (n.leftRight) {
        if (rect.xmax() < n.p.x() && n.lb != null)
          range(n.lb, set, rect);
        else if (n.rt != null)
          range(n.rt, set, rect);
      } else {
        if (rect.ymax() < n.p.y() && n.lb != null)
          range(n.lb, set, rect);
        else if (n.rt != null)
          range(n.rt, set, rect);
      }
    } else {
      range(n.lb, set, rect);
      range(n.rt, set, rect);
    }
  }

  public Point2D nearest(Point2D p) {
    DisP nearest = nearest(root, p, null);
    if (nearest != null)
      return nearest.p;
    else
      return null;
  }

  private static class DisP {
    private Point2D p;
    private double distance;

    public DisP(Point2D p, double i) {
      this.p = p;
      distance = i;
    }

    @Override
    public String toString() {
      return p.toString() + " dist: " + distance;
    }
  }

  private DisP nearest(Node n, Point2D p, DisP min) {
    DisP minSofar = min;
    if (n == null)
      return null;
    DisP childLB = null;
    DisP childRT = null;
    DisP level = new DisP(n.p, n.p.distanceSquaredTo(p));
    double lbDis = Double.MAX_VALUE, rtDis = Double.MAX_VALUE;
    if (min == null || min.distance > level.distance) {
      minSofar = level;
    }
    if (n.lb != null)
      lbDis = n.lb.rect.distanceSquaredTo(p);
    if (n.rt != null)
      rtDis = n.rt.rect.distanceSquaredTo(p);
    if (lbDis < rtDis) {
      if (lbDis < minSofar.distance) {
        childLB = nearest(n.lb, p, minSofar);
        if (childLB != null && childLB.distance < minSofar.distance)
          minSofar = childLB;
      }
      if (rtDis < minSofar.distance) {
        childRT = nearest(n.rt, p, minSofar);
        if (childRT != null && childRT.distance < minSofar.distance)
          minSofar = childRT;
      }
    } else {
      if (rtDis < minSofar.distance) {
        childRT = nearest(n.rt, p, minSofar);
        if (childRT != null && childRT.distance < minSofar.distance)
          minSofar = childRT;
      }
      if (lbDis < minSofar.distance) {
        childLB = nearest(n.lb, p, minSofar);
        if (childLB != null && childLB.distance < minSofar.distance)
          minSofar = childLB;
      }
    }
    return minSofar;
  }

  public static void main(String[] args) {
    KdTree kd = new KdTree();
    kd.insert(new Point2D(0.5, 0.5));
    kd.insert(new Point2D(0.25, 0.25));
    kd.insert(new Point2D(0.75, 0.75));
    kd.insert(new Point2D(0.4, 0.4));
    StdOut.println(kd.root.rect);
    StdOut.println(kd.root.lb.rt.rect);
    
    StdOut.println("\n"+kd.nearest(new Point2D(0.35, 0.35)));

  }

}
