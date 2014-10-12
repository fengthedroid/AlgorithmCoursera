import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

  private Item[] a; // array of items
  private int N;

  public RandomizedQueue() {
    // construct an empty randomized queue
    a = (Item[]) new Object[2];
  }

  public boolean isEmpty() {
    // is the queue empty?
    return N == 0;
  }

  public int size() {
    // return the number of items on the queue
    return N;
  }

  // resize the underlying array holding the elements
  private void resize(int capacity) {
    assert capacity >= N;
    Item[] temp = (Item[]) new Object[capacity];
    for (int i = 0; i < N; i++) {
      temp[i] = a[i];
    }
    a = temp;
  }

  public void enqueue(Item item) {
    // add the item
    if (item == null)
      throw new NullPointerException();
    if (N == a.length)
      resize(2 * a.length); // double size of array if necessary
    a[N++] = item;
  }

  public Item dequeue() {
    // delete and return a random item
    if (isEmpty())
      throw new NoSuchElementException("Stack underflow");
    int sample = StdRandom.uniform(N);
    Item item = a[sample];
    a[sample] = a[N - 1];
    a[N - 1] = null; // to avoid loitering
    N--;
    // shrink size of array if necessary
    if (N > 0 && N == a.length / 4)
      resize(a.length / 2);
    return item;
  }

  public Item sample() {
    // return (but do not delete) a random item
    if (isEmpty())
      throw new NoSuchElementException("Stack underflow");
    return a[StdRandom.uniform(N)];
  }

  public Iterator<Item> iterator() {
    // return an independent iterator over items in random order
    return new RandomArrayIterator();
  }

  private class RandomArrayIterator implements Iterator<Item> {

    private int[] shuffleOrder;
    private int index;

    public RandomArrayIterator() {
      shuffleOrder = new int[N];

      for (int i = 0; i < N; ++i) {
        shuffleOrder[i] = i;
      }
      StdRandom.shuffle(shuffleOrder);
      index = 0;
    }

    public boolean hasNext() {
      return index < N;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public Item next() {
      if (!hasNext())
        throw new NoSuchElementException();
      return a[shuffleOrder[index++]];
    }
  }

  public static void main(String[] args) {
    // unit testing
    RandomizedQueue<Integer> s = new RandomizedQueue<>();
    for (int i = 0; i < 5; ++i)
      s.enqueue(i);
    StdOut.println("(" + s.size() + " left on stack)");
    for (int i : s) {
      StdOut.print(i + " / ");
    }
    StdOut.println();
    // for(int i:s){
    // for(int j:s)
    // StdOut.print("/i "+i+" j "+j);
    // }
    // StdOut.println();
    while (!s.isEmpty()) {
      StdOut.print(" s " + s.sample() + " d " + s.dequeue() + " / ");
    }
    StdOut.println("(" + s.size() + " left on stack)");

  }
}