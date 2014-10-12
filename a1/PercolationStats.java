public class PercolationStats {
  private double[] openX;

  public PercolationStats(int N, int T) {
    if (N <= 0 || T <= 0)
      throw new IllegalArgumentException();
    // perform T independent computational experiments on an N-by-N grid
    openX = new double[T];
    for (int i = 0; i < T; ++i) {
      Percolation p = new Percolation(N);
      int openCount = 0;
      while (!p.percolates()) {
        int row = StdRandom.uniform(N) + 1;
        int col = StdRandom.uniform(N) + 1;
        if (!p.isOpen(row, col)) {
          p.open(row, col);
          ++openCount;

        }
      }
      // StdOut.println(openCount);
      openX[i] = openCount * 1.0 / (N * N);
    }
  }

  public double mean() {
    // sample mean of percolation threshold
    return StdStats.mean(openX);
  }

  public double stddev() {
    // sample standard deviation of percolation threshold
    return StdStats.stddev(openX);
  }

  public double confidenceLo() {
    // returns lower bound of the 95% confidence interval
    return StdStats.mean(openX) - 1.96 * StdStats.stddev(openX)
        / Math.sqrt(openX.length);
  }

  public double confidenceHi() {
    // returns upper bound of the 95% confidence interval
    return StdStats.mean(openX) + 1.96 * StdStats.stddev(openX)
        / Math.sqrt(openX.length);
  }

  public static void main(String[] args) {
    // test client, described below
    // PercolationStats ps1 = new PercolationStats(200,0);
    // PercolationStats ps2 = new PercolationStats(0,1);
    // T=100
    PercolationStats ps = new PercolationStats(
    Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    StdOut.println(ps.mean());
    StdOut.println(ps.stddev());
    StdOut.println(ps.confidenceLo());
    StdOut.println(ps.confidenceHi());
  }
}