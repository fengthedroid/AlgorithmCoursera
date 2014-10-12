public class Percolation {

  private boolean[][] sites;

  private boolean[] top, buttom;

  private boolean isPercolate;

  private WeightedQuickUnionUF wquf;

  public Percolation(int N) {
    if (N <= 0)
      throw new IllegalArgumentException();
    // create N-by-N grid, with all sites blocked
    sites = new boolean[N][N];
    // 1 top 2 buttom
    top = new boolean[N * N];
    buttom = new boolean[N * N];
    wquf = new WeightedQuickUnionUF(N * N);
  }

  private boolean isValidBoundary(int i, int j) {
    return !(i <= 0 || i > sites.length || j <= 0 || j > sites.length);
  }

  private int d2tod1(int i, int j) {
    return (i - 1) * sites.length + j - 1;
  }

  // i->1~N
  public void open(int i, int j) {
    if (!isValidBoundary(i, j))
      throw new IndexOutOfBoundsException();
    sites[i - 1][j - 1] = true;
    if (i == 1) {
      top[wquf.find(d2tod1(i, j))] = true;
    }
    if (i == sites.length) {
      buttom[wquf.find(d2tod1(i, j))] = true;
    }
    boolean newTop = top[wquf.find(d2tod1(i, j))];
    boolean newButtom = buttom[wquf.find(d2tod1(i, j))];
    
    if (isValidBoundary(i - 1, j) && isOpen(i - 1, j)) {
      newTop = newTop || top[wquf.find(d2tod1(i - 1, j))];
      newButtom =  newButtom || buttom[wquf.find(d2tod1(i - 1, j))];
      wquf.union(d2tod1(i, j), d2tod1(i - 1, j));
    }
    if (isValidBoundary(i + 1, j) && isOpen(i + 1, j)) {
      newTop = newTop || top[wquf.find(d2tod1(i + 1, j))];
      newButtom = newButtom || buttom[wquf.find(d2tod1(i + 1, j))];
      wquf.union(d2tod1(i, j), d2tod1(i + 1, j));
    }
    if (isValidBoundary(i, j - 1) && isOpen(i, j - 1)) {
      newTop = newTop || top[wquf.find(d2tod1(i, j - 1))];
      newButtom = newButtom || buttom[wquf.find(d2tod1(i, j - 1))];
      wquf.union(d2tod1(i, j), d2tod1(i, j - 1));
    }
    if (isValidBoundary(i, j + 1) && isOpen(i, j + 1)) {
      newTop = newTop || top[wquf.find(d2tod1(i, j + 1))];
      newButtom = newButtom || buttom[wquf.find(d2tod1(i, j + 1))];
      wquf.union(d2tod1(i, j), d2tod1(i, j + 1));
    }
    if (newTop && newButtom)
      isPercolate = true;
    int index = wquf.find(d2tod1(i, j));
    top[index] = newTop;
    buttom[index] = newButtom;
    // open site (row i, column j) if it is not already
  }

  // i->1~N
  public boolean isOpen(int i, int j) {
    // is site (row i, column j) open?
    if (!isValidBoundary(i, j))
      throw new IndexOutOfBoundsException();
    return sites[i - 1][j - 1];
  }

  // i->1~N
  public boolean isFull(int i, int j) {
    // is site (row i, column j) full?
    if (!isValidBoundary(i, j))
      throw new IndexOutOfBoundsException();
    return top[wquf.find(d2tod1(i, j))];
  }

  public boolean percolates() {
    // does the system percolate?
    return isPercolate;
  }

  public static void main(String[] args) {
    // test client, optional
    Percolation p = new Percolation(5);
    StdOut.println("should not open " + p.isOpen(5, 5));
    StdOut.println(p.wquf.count());
    // p.isOpen(0,0);
    p.open(1, 1);
    p.open(2, 1);
    p.open(2, 2);
    p.open(3, 2);
    p.open(4, 2);
    p.open(4, 3);
    p.open(4, 4);
    p.open(4, 5);
    StdOut.println("should full " + p.isFull(4, 5));
    StdOut.println("should not percolate yet " + p.percolates());
    p.open(5, 2);
    StdOut.println("should full " + p.isFull(5, 2));
    StdOut.println("should percolate " + p.percolates());
    p = new Percolation(20);
    int openCount = 0;
    while (!p.percolates()) {
      int i = StdRandom.uniform(20) + 1;
      int j = StdRandom.uniform(20) + 1;
      if (!p.isOpen(i, j)) {
        p.open(i, j);
        ++openCount;
      }
    }
    StdOut.println(openCount);
  }
}