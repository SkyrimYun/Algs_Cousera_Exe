import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    private Node first = null;
    private Node last = null;
    private int sz = 0;

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return sz == 0;
    }

    // return the number of items on the deque
    public int size() {
        return sz;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        sz++;

        if (sz == 1) {
            first = new Node();
            first.item = item;
            first.next = null;
            first.prev = null;
            last = first;
        } else {
            Node oldfirst = first;
            first = new Node();
            first.item = item;
            first.next = oldfirst;
            first.prev = null;
            oldfirst.prev = first;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        sz++;

        if (sz == 1) {
            last = new Node();
            last.item = item;
            last.next = null;
            last.prev = null;
            first = last;
        } else {
            Node oldlast = last;
            last = new Node();
            last.item = item;
            last.next = null;
            last.prev = oldlast;
            oldlast.next = last;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (sz == 0)
            throw new NoSuchElementException();

        sz--;
        Item item = first.item;

        if (sz == 0)
            first = last = null;
        else {
            first = first.next;
            first.prev = null;
        }

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (sz == 0)
            throw new NoSuchElementException();

        sz--;
        Item item = last.item;

        if (sz == 0)
            first = last = null;
        else {
            last = last.prev;
            last.next = null;
        }

        return item;
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null)
                throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
    }

}