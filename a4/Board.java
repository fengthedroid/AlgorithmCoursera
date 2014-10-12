
public class Board {
  private final char[][] block;

  private int twinR = -1, twinC = -1;
  private int hamming = -1, manhattan = -1;

  public Board(int[][] blocks) {
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    block = new char[blocks.length][blocks.length];
    for (int i = 0; i < blocks.length; ++i) {
      for (int j = 0; j < blocks.length; ++j) {
        block[i][j] = (char) blocks[i][j];
      }
    }
  }

  public int dimension() {
    // board dimension N
    return block.length;
  }

  public int hamming() {
    // number of blocks out of place
    if (this.hamming == -1) {
      int oop = 0;
      for (int i = 0; i < block.length; ++i) {
        for (int j = 0; j < block.length; ++j) {
          if (block[i][j] == 0)
            continue;
          if (block[i][j] != i * block.length + j + 1)
            ++oop;
        }
      }
      this.hamming = oop;
    }
    return this.hamming;
  }

  public int manhattan() {
    // sum of Manhattan distances between blocks and goal
    if (this.manhattan == -1) {
      int distances = 0;
      for (int i = 0; i < block.length; ++i) {
        for (int j = 0; j < block.length; ++j) {
          if (block[i][j] == 0)
            continue;
          if (block[i][j] != i * block.length + j + 1) {
            int row = (block[i][j] - 1) / block.length;
            int col = (block[i][j] - 1) % block.length;
            distances = distances + Math.abs(i - row) + Math.abs(j - col);
          }

        }
      }
      this.manhattan = distances;
    }
    return this.manhattan;
  }

  public boolean isGoal() {
    // is this board the goal board?
    //return this.hamming() == 0;
    return this.manhattan() == 0;
  }

  public Board twin() {
    // a boadr that is obtained by exchanging two adjacent blocks in the same
    // row
    int[][] twinBlock = new int[block.length][block.length];
    for (int i = 0; i < block.length; ++i) {
      for (int j = 0; j < block.length; ++j) {
        twinBlock[i][j] = block[i][j];
      }
    }
    int swap = 0;
    while (this.twinC == -1) {
      int row = StdRandom.uniform(block.length);
      int col = StdRandom.uniform(block.length);
      swap = twinBlock[row][col];
      if (swap != 0 && col - 1 >= 0 && twinBlock[row][col - 1] != 0) {
        twinC = col;
        twinR = row;
      }
    }
    if (this.twinR == -1)
      throw new IllegalStateException();
    swap=twinBlock[twinR][twinC];
    twinBlock[twinR][twinC] = twinBlock[twinR][twinC - 1];
    twinBlock[twinR][twinC - 1] = swap;
    return new Board(twinBlock);
  }

  public boolean equals(Object y) {
    // does this board equal y?
    if (y == this)
      return true;
    if (y == null)
      return false;
    if (y.getClass() != this.getClass())
      return false;
    Board that = (Board) y;
    if(this.block.length!=that.block.length)
      return false;
    for(int i = 0; i<this.block.length; ++i){
      for(int j = 0; j<this.block.length; ++j){
        if(this.block[i][j]!=that.block[i][j]){
          return false;
        }
      }
    }
    return true;
  }

  public Iterable<Board> neighbors() {
    // all neighboring boards
    Queue<Board> neigbors = new Queue<Board>();
    int[][] twinBlock = new int[block.length][block.length];
    int row = -1, col = -1;
    for (int i = 0; i < block.length; ++i) {
      for (int j = 0; j < block.length; ++j) {
        twinBlock[i][j] = block[i][j];
        if (twinBlock[i][j] == 0) {
          row = i;
          col = j;
        }
      }
    }
    if (row == col && col == -1)
      throw new IllegalStateException();
    // up
    if (row - 1 >= 0) {
      twinBlock[row][col] = twinBlock[row - 1][col];
      twinBlock[row - 1][col] = 0;
      neigbors.enqueue(new Board(twinBlock));
      twinBlock[row - 1][col] = twinBlock[row][col];
      twinBlock[row][col] = 0;
    }
    // down
    if (row + 1 < block.length) {
      twinBlock[row][col] = twinBlock[row + 1][col];
      twinBlock[row + 1][col] = 0;
      neigbors.enqueue(new Board(twinBlock));
      twinBlock[row + 1][col] = twinBlock[row][col];
      twinBlock[row][col] = 0;
    }
    // left
    if (col - 1 >= 0) {
      twinBlock[row][col] = twinBlock[row][col - 1];
      twinBlock[row][col - 1] = 0;
      neigbors.enqueue(new Board(twinBlock));
      twinBlock[row][col - 1] = twinBlock[row][col];
      twinBlock[row][col] = 0;
    }
    // right
    if (col + 1 < block.length) {
      twinBlock[row][col] = twinBlock[row][col + 1];
      twinBlock[row][col + 1] = 0;
      neigbors.enqueue(new Board(twinBlock));
      twinBlock[row][col + 1] = twinBlock[row][col];
      twinBlock[row][col] = 0;
    }
    return neigbors;
  }

  public String toString() {
    // string representation of this board (in the output format specified
    // below)
    final StringBuilder s = new StringBuilder();
    s.append(block.length + "\n");
    for (int i = 0; i < block.length; i++) {
      for (int j = 0; j < block.length; j++)
        s.append(String.format("%2d ", (int) block[i][j]));
      s.append("\n");
    }
    return s.toString();
  }

  public static void main(String[] args) {
    // unit tests (not graded)
    In in = new In(args[0]);
    int N = in.readInt();
    int[][] blocks = new int[N][N];
    for (int i = 0; i < N; i++)
      for (int j = 0; j < N; j++)
        blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);
    Board same = new Board(blocks);
    int swap = blocks[N - 1][N - 1];
    blocks[N - 1][N - 1] = blocks[0][0];
    blocks[0][0] = swap;
    Board notsame = new Board(blocks);
    StdOut.println(initial);
    StdOut.println(initial.hamming());
    StdOut.println("manhattan " + initial.manhattan());
    StdOut.println("same " + initial.equals(same));
    StdOut.println("not same " + initial.equals(notsame));
    StdOut.println("is goal? " + initial.isGoal());
    for(int i =0; i<10; ++i)
      StdOut.println(initial.twin());


//    for (Board nei : initial.neighbors()) 
//      StdOut.println("nei: " + nei.toString());
  }
}