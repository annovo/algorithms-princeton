/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Item[] arrayOfItems;
    private int dequeSize = 0;
    private int head = 0;
    private int tail = 1;

    public Deque() {
        arrayOfItems = (Item[]) new Object[2];
    }

    public boolean isEmpty() {
        return dequeSize == 0;
    }

    public int size() {
        return dequeSize;
    }

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("item cant be null");
        if (head == tail) resize(2 * arrayOfItems.length);
        arrayOfItems[head--] = item;
        if (head < 0) head = arrayOfItems.length - 1;
        dequeSize++;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("item cant be null");
        if (head == tail) resize(2 * arrayOfItems.length);
        arrayOfItems[tail++] = item;
        if (tail == arrayOfItems.length) tail = 0;
        dequeSize++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("deque is empty");
        head = (head + 1) == arrayOfItems.length ? 0 : ++head;
        Item firstItem = arrayOfItems[head];
        arrayOfItems[head] = null;
        dequeSize--;
        if (dequeSize > 1 && dequeSize == arrayOfItems.length / 4) resize(arrayOfItems.length / 2);
        return firstItem;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("deque is empty");
        tail = (tail - 1) < 0 ? arrayOfItems.length - 1 : --tail;
        Item lastItem = arrayOfItems[tail];
        arrayOfItems[tail] = null;
        dequeSize--;
        if (dequeSize > 1 && dequeSize == arrayOfItems.length / 4) resize(arrayOfItems.length / 2);
        return lastItem;
    }


    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private int current = nextIndex(head);

        private int nextIndex(int num) {
            return (num + 1) == arrayOfItems.length ? 0 : num + 1;
        }

        public boolean hasNext() {
            return arrayOfItems[current] != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("This iteartor doesnt support remove method");
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more elements");
            Item i = arrayOfItems[current];
            current = nextIndex(current);
            return i;
        }
    }

    private void resize(int size) {
        Item[] newArr = (Item[]) new Object[size];
        int pos = 0;
        for (int i = 0; i <= dequeSize; i++) {
            newArr[pos++] = arrayOfItems[indexOfArray(i)];
        }

        arrayOfItems = newArr;
        head = 0;
        tail = pos;
    }

    private int indexOfArray(int num) {
        return (head + num) >= arrayOfItems.length ? (head + num - arrayOfItems.length) :
               (head + num);
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addLast(1);
        deque.addFirst(2);
        deque.addLast(3);
        deque.addLast(4);
        deque.removeFirst();
        deque.addLast(6);
        deque.removeLast();
        deque.removeLast();
        deque.removeFirst();
        deque.addLast(10);
        

    }
}

