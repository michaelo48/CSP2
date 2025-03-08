import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import student.TestableRandom;

/**
 * This class implements SkipList data structure and contains an inner SkipNode
 * class which the SkipList will make an array of to store data.
 * 
 * @author CS Staff
 * 
 * @version 2024-01-22
 * @param <K>
 *            Key
 * @param <V>
 *            Value
 */
public class SkipList<K extends Comparable<K>, V>
    implements Iterable<KVPair<K, V>> {
    private SkipNode head; // First element (Sentinel Node)
    private int size; // number of entries in the Skip List
    private Random rng;

    /**
     * Initializes the fields head, size and level
     */
    public SkipList() {
        head = new SkipNode(null, 0);
        size = 0;
        this.rng = new TestableRandom();
    }


    /**
     * returns a random level (using geometric distribution), minimum of 1
     * 
     * @return an integer representing the random level generated, with a
     *         minimum
     *         value of 1. The level increases with 50% probability each time,
     *         following a geometric distribution.
     */
    public int randomLevel() {
        int level = 1;
        while (rng.nextBoolean())
            level++;
        return level;
    }


    /**
     * Searches for the KVPair using the key which is a Comparable object.
     * 
     * @param key
     *            key to be searched for
     * @return returns the KVPair that matches the key
     */
    public ArrayList<KVPair<K, V>> search(K key) {
        if (key == null) {
            return new ArrayList<>();
        }

        ArrayList<KVPair<K, V>> results = new ArrayList<>();
        SkipNode current = head;

        // Start from the highest level and work down
        for (int i = head.level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].element()
                .key().compareTo(key) < 0) {
                current = current.forward[i];
            }
        }

        // Move to the first node of the bottom level
        current = current.forward[0];

        // Collect all matching keys
        while (current != null && current.element().key().compareTo(key) == 0) {
            results.add(current.element());
            current = current.forward[0];
        }

        return results;
    }


    /**
     * @return the size of the SkipList
     */
    public int size() {
        return size;
    }


    /**
     * Inserts the KVPair in the SkipList at its appropriate spot as designated
     * by its lexicoragraphical order.
     * 
     * @param it
     *            the KVPair to be inserted
     */
    @SuppressWarnings("unchecked")
    public void insert(KVPair<K, V> it) {
        if (it == null) {
            return;
        }

        int newLevel = randomLevel() - 1;
        if (newLevel > head.level) {
            adjustHead(newLevel);
        }

        SkipNode[] update = (SkipNode[])Array.newInstance(
            SkipList.SkipNode.class, head.level + 1);

        SkipNode current = head;

        for (int i = head.level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].element()
                .key().compareTo(it.key()) < 0) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        SkipNode newNode = new SkipNode(it, newLevel);

        for (int i = 0; i <= newLevel; i++) {
            newNode.forward[i] = update[i].forward[i];
            update[i].forward[i] = newNode;
        }

        size++;
    }


    /**
     * Increases the number of levels in head so that no element has more
     * indices than the head.
     * 
     * @param newLevel
     *            the number of levels to be added to head
     */
    public void adjustHead(int newLevel) {
        SkipNode oldHead = head;
        head = new SkipNode(null, newLevel);

        for (int i = 0; i <= oldHead.level; i++) {
            head.forward[i] = oldHead.forward[i];
        }

        for (int i = oldHead.level + 1; i <= newLevel; i++) {
            head.forward[i] = null;
        }
    }


    /**
     * Removes the KVPair that is passed in as a parameter and returns true if
     * the pair was valid and false if not.
     * 
     * @param key
     *            the KVPair to be removed
     * @return returns the removed pair if the pair was valid and null if not
     */

    @SuppressWarnings("unchecked")
    public KVPair<K, V> remove(K key) {
        if (key == null) {
            return null;
        }

        SkipNode[] update = (SkipNode[])Array.newInstance(
            SkipList.SkipNode.class, head.level + 1);
        SkipNode current = head;

        for (int i = head.level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].element()
                .key().compareTo(key) < 0) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        current = current.forward[0];

        if (current != null && current.element().key().compareTo(key) == 0) {
            for (int i = 0; i <= current.level; i++) {
                update[i].forward[i] = current.forward[i];
            }

            size--;

            return current.element();
        }

        return null;
    }


    /**
     * Removes a KVPair with the specified value.
     * 
     * @param val
     *            the value of the KVPair to be removed
     * @return returns true if the removal was successful
     */
    @SuppressWarnings("unchecked")
    public KVPair<K, V> removeByValue(V val) {
        if (val == null) {
            return null;
        }

        SkipNode current = head;
        SkipNode[] update = (SkipNode[])Array.newInstance(
            SkipList.SkipNode.class, head.level + 1);

        for (int i = 0; i <= head.level; i++) {
            update[i] = head;
        }

        current = head.forward[0];
        while (current != null) {
            if (current.element().value().equals(val)) {

                for (int i = 0; i <= current.level; i++) {
                    update[i].forward[i] = current.forward[i];
                }

                size--;

                return current.element();
            }

            for (int i = 0; i <= current.level; i++) {
                update[i] = current;
            }
            current = current.forward[0];
        }

        return null;
    }


    /**
     * Prints out the SkipList in a human readable format to the console.
     */
    public void dump() {
        System.out.println("SkipList dump:");

        System.out.println("Node has depth " + (head.level + 1) + " value null");

        SkipNode current = head.forward[0];
        while (current != null) {
            // We know this is specifically for Point values in this application
            Point p = (Point)current.element().value();
            System.out.println("Node has depth " + (current.level + 1) + " value " 
                + current.element().key() + " " 
                + p.getX() + " " 
                + p.getY());
            current = current.forward[0];
        }

        System.out.println("SkipList size is: " + size);
    }

    /**
     * This class implements a SkipNode for the SkipList data structure.
     * 
     * @author CS Staff
     * 
     * @version 2016-01-30
     */
    private class SkipNode {

        // the KVPair to hold
        private KVPair<K, V> pair;
        // An array of pointers to subsequent nodes
        private SkipNode[] forward;
        // the level of the node
        private int level;

        /**
         * Initializes the fields with the required KVPair and the number of
         * levels from the random level method in the SkipList.
         * 
         * @param tempPair
         *            the KVPair to be inserted
         * @param level
         *            the number of levels that the SkipNode should have
         */
        @SuppressWarnings("unchecked")
        public SkipNode(KVPair<K, V> tempPair, int level) {
            pair = tempPair;
            forward = (SkipNode[])Array.newInstance(SkipList.SkipNode.class,
                level + 1);
            this.level = level;
        }


        /**
         * Returns the KVPair stored in the SkipList.
         * 
         * @return the KVPair
         */
        public KVPair<K, V> element() {
            return pair;
        }

    }


    private class SkipListIterator implements Iterator<KVPair<K, V>> {
        private SkipNode current;

        public SkipListIterator() {
            current = head;
        }


        @Override
        public boolean hasNext() {
            return current.forward[0] != null;
        }


        @Override
        public KVPair<K, V> next() {
            KVPair<K, V> elem = current.forward[0].element();
            current = current.forward[0];
            return elem;
        }

    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new SkipListIterator();
    }

}