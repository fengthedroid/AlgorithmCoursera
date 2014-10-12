public class Subset {

  public static void main(String[] args) {
    int k = Integer.parseInt(args[0]);
    RandomizedQueue<String> rq = new RandomizedQueue<String>();
    for (int i = 0; i < k; ++i) {
      rq.enqueue(StdIn.readString());
    }
    int more = k + 1;
    while (!StdIn.isEmpty()) {
      if (StdRandom.uniform(more) < k) {
        rq.dequeue();
        rq.enqueue(StdIn.readString());
      } else {
        StdIn.readString();
      }
      ++more;
    }

    for (int i = 0; i < k; ++i)
      StdOut.println(rq.dequeue());
  }
}