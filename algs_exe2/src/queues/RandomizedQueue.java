import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] s;
    private int N = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            copy[i] = s[i];
        }
        s = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        if (N == s.length)
            resize(2 * s.length);
        s[N++] = item; // use to index into array; then increment N
    }

    // remove and return a random item
    public Item dequeue() {
        if (N == 0)
            throw new NoSuchElementException();

        int index = StdRandom.uniform(N);
        Item item = s[index];
        s[index] = s[--N];

        if (N > 0 && N == s.length / 4)
            resize(s.length / 2);

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (N == 0)
            throw new NoSuchElementException();

        int index = StdRandom.uniform(N);
        return s[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }

    private class ReverseArrayIterator implements Iterator<Item> {
        private int i = N;
        private Item[] iteratorItems;

        public ReverseArrayIterator() {
            iteratorItems = copyQueue();
            StdRandom.shuffle(iteratorItems);
        }

        private Item[] copyQueue() {
            Item[] copiedItems = (Item[]) new Object[N];
            for (int i = 0; i < N; i++) {
                copiedItems[i] = s[i];
            }
            return copiedItems;
        }

        public boolean hasNext() {
            return i > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return iteratorItems[--i]; // s[--N]: decrement N; then use to index into array
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
    }

}