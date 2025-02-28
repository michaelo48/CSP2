import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * SkipList implementation for points.
 * 
 * @author CS3114 Student
 * @version 2/27/2025
 */
public class SkipList {
    private SkipNode head;
    private int level;
    private int size;
    private static final int MAX_LEVEL = 20;
    private Random random;
    
    /**
     * Represents a node in the SkipList.
     */
    private class SkipNode {
        String key;
        Point element;
        SkipNode[] forward;
        
        /**
         * Creates a new SkipNode.
         * 
         * @param key The key (point name)
         * @param element The point element
         * @param level The level of the node
         */
        public SkipNode(String key, Point element, int level) {
            this.key = key;
            this.element = element;
            forward = new SkipNode[level + 1];
            for (int i = 0; i < level + 1; i++) {
                forward[i] = null;
            }
        }
    }
    
    /**
     * Creates a new SkipList.
     */
    public SkipList() {
        head = new SkipNode(null, null, MAX_LEVEL);
        level = 0;
        size = 0;
        random = new Random();
    }
    
    /**
     * Generates a random level for a new node.
     * 
     * @return The generated level
     */
    private int randomLevel() {
        int lvl = 0;
        while (random.nextDouble() < 0.5 && lvl < MAX_LEVEL) {
            lvl++;
        }
        return lvl;
    }
    
    /**
     * Inserts a point into the SkipList.
     * 
     * @param point The point to insert
     */
    public void insert(Point point) {
        String key = point.getName();
        SkipNode[] update = new SkipNode[MAX_LEVEL + 1];
        SkipNode current = head;
        
        // Find position to insert
        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && 
                   current.forward[i].key.compareTo(key) < 0) {
                current = current.forward[i];
            }
            update[i] = current;
        }
        
        // Move to next node
        current = current.forward[0];
        
        // Generate random level for new node
        int newLevel = randomLevel();
        
        // Update list level if needed
        if (newLevel > level) {
            for (int i = level + 1; i <= newLevel; i++) {
                update[i] = head;
            }
            level = newLevel;
        }
        
        // Create new node
        SkipNode newNode = new SkipNode(key, point, newLevel);
        
        // Insert node by updating references
        for (int i = 0; i <= newLevel; i++) {
            newNode.forward[i] = update[i].forward[i];
            update[i].forward[i] = newNode;
        }
        
        size++;
    }
    
    /**
     * Removes a point by name.
     * 
     * @param key The name of the point to remove
     * @return The removed point, or null if no point was removed
     */
    public Point remove(String key) {
        SkipNode[] update = new SkipNode[MAX_LEVEL + 1];
        SkipNode current = head;
        
        // Find position to remove
        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && 
                   current.forward[i].key.compareTo(key) < 0) {
                current = current.forward[i];
            }
            update[i] = current;
        }
        
        // Move to next node
        current = current.forward[0];
        
        // If key found, remove it
        if (current != null && current.key.equals(key)) {
            Point removedPoint = current.element;
            
            // Update references
            for (int i = 0; i <= level; i++) {
                if (update[i].forward[i] == current) {
                    update[i].forward[i] = current.forward[i];
                }
            }
            
            // Update level if needed
            while (level > 0 && head.forward[level] == null) {
                level--;
            }
            
            size--;
            return removedPoint;
        }
        
        return null;
    }
    
    /**
     * Searches for points by name.
     * 
     * @param key The name to search for
     * @return A list of points with the given name
     */
    public List<Point> search(String key) {
        List<Point> results = new ArrayList<>();
        SkipNode current = head;
        
        // Find first node with matching key
        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && 
                   current.forward[i].key.compareTo(key) < 0) {
                current = current.forward[i];
            }
        }
        
        // Move to first potential match
        current = current.forward[0];
        
        // Collect all matches
        while (current != null && current.key.equals(key)) {
            results.add(current.element);
            current = current.forward[0];
        }
        
        return results;
    }
    
    /**
     * Gets a string representation of the SkipList.
     * 
     * @return A string representing the SkipList
     */
    public String dump() {
        StringBuilder sb = new StringBuilder();
        sb.append("SkipList dump:\n");
        
        SkipNode current = head.forward[0];
        while (current != null) {
            sb.append(current.element.toString());
            sb.append(" (level ").append(current.forward.length - 1).append(")\n");
            current = current.forward[0];
        }
        
        return sb.toString();
    }
    
    /**
     * Gets the number of elements in the SkipList.
     * 
     * @return The size
     */
    public int size() {
        return size;
    }
    
    /**
     * Checks if the SkipList is empty.
     * 
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }
}
