/**
 * Represents a point with a name and coordinates.
 * 
 * @author CS3114 Student
 * @version 2/27/2025
 */
public class Point {
    private String name;
    private int x;
    private int y;
    
    /**
     * Creates a new Point.
     * 
     * @param name The name of the point
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public Point(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the name of the point.
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the x-coordinate of the point.
     * 
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Gets the y-coordinate of the point.
     * 
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Checks if this point has the same position as another point.
     * 
     * @param other The other point to compare with
     * @return True if both points have the same position
     */
    public boolean samePosition(Point other) {
        return x == other.x && y == other.y;
    }
    
    /**
     * Gets a string representation of the point.
     * 
     * @return A string in the format "(name, x, y)"
     */
    @Override
    public String toString() {
        return "(" + name + ", " + x + ", " + y + ")";
    }
}
