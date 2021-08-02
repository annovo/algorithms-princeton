/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        int k = Integer.parseInt(args[0]);
        double n = (double) k;

        if (k == 0) return;
        while (!StdIn.isEmpty()) {
            if (queue.size() < k) {
                String str = StdIn.readString();
                queue.enqueue(str);
            }
            else {
                n++;
                String str = StdIn.readString();
                if (StdRandom.bernoulli(k / n)) {
                    queue.dequeue();
                    queue.enqueue(str);
                }
            }
        }
        for (String s : queue) {
            StdOut.print(s);
            StdOut.println();
        }
    }
}
