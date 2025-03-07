/**
 * Simple class to represent a coordinate pair
 * 
 * @author michaelo48
 * @version 03.07.2025
 */
public class Coordinate {
    private int x;
    private int y;
    
    /**
     * Creates a new coordinate
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the x-coordinate
     * 
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Gets the y-coordinate
     * 
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Checks if this coordinate equals another object
     * 
     * @param obj The object to compare with
     * @return True if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Coordinate other = (Coordinate) obj;
        return x == other.x && y == other.y;
    }
    
    /**
     * String representation of the coordinate
     * 
     * @return String in format "(x, y)"
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}