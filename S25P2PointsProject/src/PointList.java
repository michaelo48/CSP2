/**
 * A simple list implementation for storing Point objects
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class PointList {
    private Point[] points;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    
    /**
     * Creates a new PointList with default capacity
     */
    public PointList() {
        points = new Point[DEFAULT_CAPACITY];
        size = 0;
    }
    
    /**
     * Gets the number of points in the list
     * 
     * @return The size
     */
    public int size() {
        return size;
    }
    
    /**
     * Checks if the list is empty
     * 
     * @return True if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Adds a point to the list
     * 
     * @param point The point to add
     */
    public void add(Point point) {
        if (size >= points.length) {
            expandCapacity();
        }
        points[size++] = point;
    }
    
    /**
     * Expands the capacity of the list
     */
    private void expandCapacity() {
        Point[] newArray = new Point[points.length * 2];
        for (int i = 0; i < size; i++) {
            newArray[i] = points[i];
        }
        points = newArray;
    }
    
    /**
     * Gets a point at the specified index
     * 
     * @param index The index
     * @return The point at that index
     */
    public Point get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        return points[index];
    }
    
    /**
     * Removes a point at the specified index
     * 
     * @param index The index
     * @return The removed point
     */
    public Point remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        Point removed = points[index];
        
        // Shift elements to fill the gap
        for (int i = index; i < size - 1; i++) {
            points[i] = points[i + 1];
        }
        
        // Clear the last element and decrement size
        points[--size] = null;
        
        return removed;
    }
    
    /**
     * Adds all points from another list to this list
     * 
     * @param otherList The list to add from
     */
    public void addAll(PointList otherList) {
        for (int i = 0; i < otherList.size(); i++) {
            add(otherList.get(i));
        }
    }
    
    /**
     * Creates a copy of this list
     * 
     * @return A new PointList with the same elements
     */
    public PointList copy() {
        PointList newList = new PointList();
        for (int i = 0; i < size; i++) {
            newList.add(points[i]);
        }
        return newList;
    }
    
    /**
     * Clears all elements from the list
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            points[i] = null;
        }
        size = 0;
    }
}