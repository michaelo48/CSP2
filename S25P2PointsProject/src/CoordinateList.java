/**
 * A simple list implementation for storing unique Coordinate objects
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class CoordinateList {
    private Coordinate[] coordinates;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    
    /**
     * Creates a new CoordinateList with default capacity
     */
    public CoordinateList() {
        coordinates = new Coordinate[DEFAULT_CAPACITY];
        size = 0;
    }
    
    /**
     * Gets the number of coordinates in the list
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
     * Adds a coordinate to the list if it's not already present
     * 
     * @param coord The coordinate to add
     * @return True if added, false if already present
     */
    public boolean add(Coordinate coord) {
        // Check if coordinate already exists
        for (int i = 0; i < size; i++) {
            if (coordinates[i].equals(coord)) {
                return false;
            }
        }
        
        // Add the new coordinate
        if (size >= coordinates.length) {
            expandCapacity();
        }
        coordinates[size++] = coord;
        return true;
    }
    
    /**
     * Adds a coordinate by its x and y values
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return True if added, false if already present
     */
    public boolean add(int x, int y) {
        return add(new Coordinate(x, y));
    }
    
    /**
     * Expands the capacity of the list
     */
    private void expandCapacity() {
        Coordinate[] newArray = new Coordinate[coordinates.length * 2];
        for (int i = 0; i < size; i++) {
            newArray[i] = coordinates[i];
        }
        coordinates = newArray;
    }
    
    /**
     * Gets a coordinate at the specified index
     * 
     * @param index The index
     * @return The coordinate at that index
     */
    public Coordinate get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        return coordinates[index];
    }
    
    /**
     * Checks if the list contains the specified coordinate
     * 
     * @param coord The coordinate to check for
     * @return True if found, false otherwise
     */
    public boolean contains(Coordinate coord) {
        for (int i = 0; i < size; i++) {
            if (coordinates[i].equals(coord)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the list contains coordinates with the specified values
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return True if found, false otherwise
     */
    public boolean contains(int x, int y) {
        return contains(new Coordinate(x, y));
    }
}