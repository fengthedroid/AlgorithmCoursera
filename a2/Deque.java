import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
  private int size;

  private Node first, last;

  private class Node {
    private Node next, previous;
    private Item item;
  }

  public Deque() {
    // construct an empty deque
    first = null;
    last = null;
    size = 0;
    assert invariant();
  }

  private boolean invariant() {
    if (size == 0) {
      if (first != null || last != null)
        return false;
    } else if (size == 1) {
      if (first != last)
        return false;
      if (first.next != null || first.previous != null)
        return false;
    } else if (first.previous != null || last.next != null)
      return false;

    int numberOfNodes = 0;
    for (Node x = first; x != null; x = x.next) {
      numberOfNodes++;
    }
    if (numberOfNodes != size)
      return false;

    int reverseNumberOfNodes = 0;
    for (Node x = last; x != null; x = x.previous) {
      reverseNumberOfNodes++;
    }
    if (reverseNumberOfNodes != size)
      return false;
    return true;
  }

  public boolean isEmpty() {
    // is the deque empty?
    return first == null;
  }

  public int size() {
    // return the number of items on the deque
    return size;
  }

  public void addFirst(Item item) {
    // insert the item at the front
    if (item == null)
      throw new NullPointerException();
    Node oldfirst = first;
    first = new Node();
    first.item = item;
    first.previous = null;
    first.next = oldfirst;
    if (size != 0)
      oldfirst.previous = first;
    else
      last = first;
    size++;
    assert invariant();
  }

  public void addLast(Item item) {
    // insert the item at the end
    if (item == null)
      throw new NullPointerException();
    Node oldlast = last;
    last = new Node();
    last.item = item;
    last.previous = oldlast;
    last.next = null;
    if (size != 0)
      oldlast.next = last;
    else
      first = last;
    size++;
    assert invariant();
  }

  public Item removeFirst() {
    // delete and return the item at the front
    if (isEmpty())
      throw new NoSuchElementException("Stack underflow");
    Item item = first.item; // save item to return
    if (size == 1) {
      first = null;
      last = null;
    } else {
      first = first.next; // delete first node
      first.previous = null;
    }
    --size;
    assert invariant();
    return item;
  }

  public Item removeLast() {
    // delete and return the item at the end
    if (isEmpty())
      throw new NoSuchElementException("Stack underflow");
    Item item = last.item; // save item to return
    if (size == 1) {
      first = null;
      last = null;
    } else {
      last = last.previous;
      last.next = null;
    }
    --size;
    assert invariant();
    return item;
  }

  public Iterator<Item> iterator() {
    return new ListIterator();
  }

  // an iterator, doesn't implement remove() since it's optional
  private class ListIterator implements Iterator<Item> {
    private Node current = first;

    public boolean hasNext() {
      return current != null;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public Item next() {
      if (!hasNext())
        throw new NoSuchElementException();
      Item item = current.item;
      current = current.next;
      return item;
    }
  }

  public static void main(String[] args) {
    // unit testing
    Deque<Integer> s = new Deque<>();
    for (int i = 0; i < 5; ++i) {
      s.addFirst(i);
    }

    for (int i = 90; i < 95; ++i) {
      s.addLast(i);
    }
    StdOut.println("(" + s.size() + " left on stack)");
    for (int i : s) {
      StdOut.print(i + " / ");
    }
    StdOut.println();
    // for (int i : s) {
    // for (int j : s) {
    // StdOut.print(i + "/" + j + " ");
    // }
    // }
    for (int i = 0; i < 2; i++) {
      s.removeFirst();
      s.removeLast();
      StdOut.println("(" + s.size() + " left on stack)");
    }
    for (int i : s) {
      StdOut.print(i + " / ");
    }
    StdOut.println(s.isEmpty());
    int max = s.size();
    for (int j = 0; j < max; j++) {
      s.removeLast();
      for (int i : s) {
        StdOut.print(i + " / ");
      }
      StdOut.println();
    }
    StdOut.println(s.isEmpty());
  }
}