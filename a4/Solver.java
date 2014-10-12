
public class Solver {

  private MinPQ<Node> queue;
  private MinPQ<Node> twinQueue;

  private static class Node implements Comparable<Node> {
    private final Board board;
    Node previous;
    private final int move;

    public Node(Board b, int move_, Node pre) {
      this.board = b;
      this.move = move_;
      this.previous = pre;
    }

    public Iterable<Node> neigbhorNodes() {
      Queue<Node> neiNode = new Queue<Node>();
      for (Board nei : board.neighbors()) {
        if (this.previous != null && nei.equals(this.previous.board))
          continue;
        else
          neiNode.enqueue(new Node(nei, move + 1, this));
      }
      return neiNode;
    }

    public boolean isGoal() {
      return this.board.isGoal();
    }

    @Override
    public int compareTo(Node that) {
      if (this.board.manhattan() + this.move > that.board.manhattan()
          + that.move)
        return 1;
      else if (this.board.manhattan() + this.move < that.board.manhattan()
          + that.move)
        return -1;
      else
        return 0;
      // if (this.board.hamming() + this.move > that.board.hamming() +
      // that.move)
      // return 1;
      // else if (this.board.hamming() + this.move < that.board.hamming()
      // + that.move)
      // return -1;
      // else
      // return 0;
    }
  }

  public Solver(Board initial) {
    // find a solution to the initial board (using the A* algorithm)
    queue = new MinPQ<Node>();
    twinQueue = new MinPQ<Node>();
    queue.insert(new Node(initial, 0, null));
    twinQueue.insert(new Node(initial.twin(), 0, null));
  }

  public boolean isSolvable() {
    // is the initial board solvable?
    while (!queue.min().isGoal() && !twinQueue.min().isGoal()) {
      Node small = queue.delMin();
      for (Node nei : small.neigbhorNodes()) {
        // if (small.previous != null && nei.board.equals(small.previous.board))
        // continue;
        queue.insert(nei);
      }
      Node smallTwin = twinQueue.delMin();
      for (Node nei : smallTwin.neigbhorNodes()) {
        // if (smallTwin.previous != null
        // && nei.board.equals(smallTwin.previous.board))
        // continue;
        twinQueue.insert(nei);
      }
    }
    return queue.min().isGoal();
  }

  public int moves() {
    // min number of moves to solve initial board; -1 if unsolvable
    if (isSolvable())
      return queue.min().move;
    else
      return -1;
  }

  public Iterable<Board> solution() {
    // sequence of boards in a shortest solution; null if unsolvable
    if (isSolvable()) {
      Stack<Board> result = new Stack<Board>();
      Node current = queue.min();
      result.push(current.board);
      while (current.previous != null) {
        result.push(current.previous.board);
        current = current.previous;
      }
      return result;
    } else
      return null;
  }

  public static void main(String[] args) {
    // solve a slider puzzle (given below)
    // create initial board from file
    In in = new In(args[0]);
    int N = in.readInt();
    int[][] blocks = new int[N][N];
    for (int i = 0; i < N; i++)
      for (int j = 0; j < N; j++)
        blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);

    // solve the puzzle
    Solver solver = new Solver(initial);
    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution())
        StdOut.println(board);
    }
  }
}