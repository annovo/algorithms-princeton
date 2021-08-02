/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int queueSize = 0;
    private Item[] arrayOfItems;
    private int head = 0;
    private int tail = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        arrayOfItems = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return queueSize == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return queueSize;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("item cant be null");
        arrayOfItems[tail++] = item;
        queueSize++;
        if (queueSize % arrayOfItems.length == 0) resize(2 * arrayOfItems.length);
        if (tail == arrayOfItems.length) tail = 0;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("deque is empty");
        int randomNum = randomIndex();
        Item random = arrayOfItems[randomNum];
        arrayOfItems[randomNum] = arrayOfItems[head];
        arrayOfItems[head++] = null;
        if (head == arrayOfItems.length) head = 0;
        queueSize--;
        if (queueSize > 0 && queueSize == arrayOfItems.length / 4) resize(arrayOfItems.length / 2);
        return random;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("deque is empty");
        Item random = arrayOfItems[randomIndex()];
        return random;
    }

    private int randomIndex() {
        int num = StdRandom.uniform(queueSize);
        return indexOfArray(num);
    }

    private int indexOfArray(int num) {
        return (head + num) >= arrayOfItems.length ? (head + num - arrayOfItems.length) :
               (head + num);
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }

    private class RandomizedIterator implements Iterator<Item> {
        private int i = 0;
        private final Item[] newArr = (Item[]) new Object[queueSize];

        public RandomizedIterator() {
            for (int j = 0; j < queueSize; j++) {
                newArr[j] = arrayOfItems[indexOfArray(j)];
            }

            StdRandom.shuffle(newArr);
        }

        public boolean hasNext() {
            return i != queueSize;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("Queue is empty");
            return newArr[i++];
        }

        public void remove() {
            throw new UnsupportedOperationException("This iteartor doesnt support remove method");
        }
    }

    private void resize(int size) {
        Item[] newArr = (Item[]) new Object[size];
        int pos = 0;
        for (int i = 0; i < queueSize; i++) {
            newArr[pos++] = arrayOfItems[indexOfArray(i)];
        }

        arrayOfItems = newArr;
        head = 0;
        tail = pos;
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
        while (!queue.isEmpty()) {
            StdOut.print("sample: " + queue.sample());
            StdOut.println();
            StdOut.print("dequeue: " + queue.dequeue());
            StdOut.println();
            StdOut.print(queue.size());
            StdOut.println();
        }

    }
}

